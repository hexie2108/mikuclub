package org.mikuclub.app.ui.fragments.windows;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.mikuclub.app.adapters.CommentRepliesAdapter;
import org.mikuclub.app.adapters.CommentsAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.CommentsDelegate;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;
import org.mikuclub.app.utils.ScreenUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

/**
 * 文章页 评论回复窗口碎片
 */
public class CommentRepliesFragment extends BottomSheetDialogFragment
{

        public static final int TAG = 5;

        //文章列表
        private RecyclerView recyclerView;
        private CommentsAdapter recyclerViewAdapter;
        private List<Comment> recyclerDataList;
        //父评论主体
        private ConstraintLayout item;
        private ImageView itemAvatarImg;
        private TextView itemName;
        private TextView itemDate;
        private TextView itemContent;
        private TextView itemCountReplies;

        //数据请求代理人
        private CommentsDelegate delegate;

        //获取评论数据
        private Comment comment;

        //信号标 是否要加载新数据  在评论页 需要默认就开启
        private boolean wantMore = true;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {

                // 为fragment加载主布局
                View root = inflater.inflate(R.layout.fragment_comment_replies_windows, container, false);
                return root;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {

                super.onViewCreated(view, savedInstanceState);

                //获取传递的数据
                comment = (Comment) getArguments().getSerializable("comment");

                //绑定组件
                recyclerView = view.findViewById(R.id.post_comments_replies_recycler_view);
                //管理器绑定各项 视图
                item = view.findViewById(R.id.item_comment);

                itemAvatarImg = view.findViewById(R.id.item_avatar_img);
                itemName = view.findViewById(R.id.item_name);
                itemDate = view.findViewById(R.id.item_date);
                itemContent = view.findViewById(R.id.item_content);
                itemCountReplies = view.findViewById(R.id.item_count_replies);

                //创建数据请求 代理人
                delegate = new CommentsDelegate(((PostActivity) getActivity()).TAG);
                recyclerDataList = new ArrayList<>();

                //初始化父评论
                initParentComment();

                //初始化列表
                initRecyclerView();

                //动态调整布局高度
                final View myView = view;
                myView.post(new Runnable()
                {
                        @Override
                        public void run()
                        {
                                GeneralUtils.setMaxHeightOfLayout(getActivity(), myView, GlobalConfig.HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS);
                        }
                });
        }


        @Override
        public void onStart()
        {
                super.onStart();
                //每次访问该页面的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                getMore();
        }

        private void initParentComment()
        {
                //为视图设置数据
                itemName.setText(comment.getAuthor_name());
                //生成时间格式
                String dateString = new SimpleDateFormat("yy-MM-dd HH:mm").format(comment.getDate());
                itemDate.setText(dateString);
                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(comment.getAuthor_avatar_urls().getSize96());
                //加载远程图片
                GlideImageUtils.getSquareImg(getActivity(), itemAvatarImg, avatarSrc);

                //获取评论内容
                String htmlContent = comment.getContent().getRendered();
                //移除内容外层P标签
                htmlContent = HttpUtils.removeHtmlMainTag(htmlContent, "<p>", "</p>");
                //解析 内容html
                HttpUtils.parseHtml(getActivity(), htmlContent, itemContent, new OnTagClickListener()
                {
                        //设置 点击图片tag的动作
                        @Override
                        public void onImageClick(Context context, List<String> imagesSrc, int position)
                        {
                        }

                        //设置点击链接tag的动作
                        @Override
                        public void onLinkClick(Context context, String url)
                        {
                                // link click
                                Uri uri = Uri.parse(HttpUtils.checkAndAddHttpsProtocol(url));
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(uri);
                                getActivity().startActivity(intent);
                        }
                });
        }


        /**
         * 初始化 评论列表
         */
        private void initRecyclerView()
        {
                //配置recyclerView
                recyclerViewAdapter = new CommentRepliesAdapter(recyclerDataList, getActivity());
                recyclerView.setAdapter(recyclerViewAdapter);


                //创建加载布局
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS * 2);
                //禁止内嵌滑动, 帮助禁止浮动页面滑动
                recyclerView.setNestedScrollingEnabled(false);

                //绑定滑动事件
                recyclerView.addOnScrollListener(new MyListOnScrollListener(recyclerViewAdapter, linearLayoutManager)
                {
                        //只有满足位置条件才会触发方法
                        @Override
                        public void onExecute()
                        {
                                //加载更多
                                getMore();
                        }
                });

                if (comment.getMetadata().getCount_replies() == 0)
                {
                        //没有回复, 关闭自动加载
                        wantMore = false;
                        recyclerView.setVisibility(View.INVISIBLE);
                }

        }

        /*
        加载更多
         */
        private void getMore()
        {
                //检查信号标
                if (wantMore)
                {
                        //关闭信号标
                        wantMore = false;
                        WrapperCallBack wrapperCallBack = new WrapperCallBack()
                        {
                                //成功的情况
                                @Override
                                public void onSuccess(String response)
                                {

                                        //解析数据
                                        Comments commentList = Parser.comments(response);
                                        //加载数据
                                        recyclerDataList.addAll(commentList.getBody());
                                        //通知更新
                                        recyclerViewAdapter.notifyItemInserted(recyclerDataList.size());
                                        //如果返回的文章等于规定数量, 正常
                                        if (commentList.getBody().size() == GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS)
                                        {
                                                //重新开启信号标
                                                wantMore = true;
                                        }
                                        else
                                        {
                                                //没有更多内容了
                                                //调用错误处理方法
                                                onError();
                                        }
                                }

                                //请求结果包含错误的情况
                                //结果主体为空, 无更多内容
                                @Override
                                public void onError()
                                {
                                        recyclerViewAdapter.setNotMoreError(true);
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        recyclerViewAdapter.setInternetError(true, new View.OnClickListener()
                                        {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                        //重置请求状态
                                                        wantMore = true;
                                                        //重置错误显示
                                                        recyclerViewAdapter.setInternetError(false, null);
                                                        getMore();
                                                }
                                        });
                                        LogUtils.e("错误");
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //取消请求的情况
                                @Override
                                public void onCancel()
                                {
                                        wantMore = true;
                                }
                        };
                        //获取当前 数据长度
                        int offset = recyclerDataList.size();
                        //LogUtils.e("offset " + offset);
                        //委托代理人发送请求
                        delegate.getCommentsListByPostId(comment.getPost(), comment.getId(), offset, wrapperCallBack);
                }
        }

        /**
         * 禁止浮动页面滑动
         *
         * @param dialog
         * @param style
         */
        @Override
        public void setupDialog(Dialog dialog, int style)
        {
                ScreenUtils.disableDraggingOfBottomSheetDialogFragment(dialog);
        }


        /**
         * 本碎片的静态启动方法
         *
         * @param comment
         * @return
         */
        public static CommentRepliesFragment startAction(Comment comment)
        {
                Bundle bundle = new Bundle();
                bundle.putSerializable("comment", comment);
                CommentRepliesFragment fragment = new CommentRepliesFragment();
                fragment.setArguments(bundle);
                return fragment;
        }


        @Override
        public void onStop()
        {
                //取消本碎片相关的所有网络请求
                Request.cancelRequest(TAG);

                super.onStop();
        }
}
