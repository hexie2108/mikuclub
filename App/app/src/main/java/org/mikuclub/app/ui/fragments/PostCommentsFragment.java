package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.CommentsAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.CommentsDelegate;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章页 评论页面碎片
 */
public class PostCommentsFragment extends Fragment
{


        //文章列表
        private RecyclerView recyclerView;
        private CommentsAdapter recyclerViewAdapter;
        private List<Comment> recyclerDataList;

        private ConstraintLayout postCommentsSendBox;

        //数据请求代理人
        private CommentsDelegate delegate;

        //获取文章数据
        private Post post;

        //信号标 是否要加载新数据  在评论页 需要默认就开启
        private boolean wantMore = true;


        public PostCommentsFragment()
        {
                // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.fragment_post_comments, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                //绑定组件
                recyclerView = view.findViewById(R.id.post_comments_recycler_view);
                postCommentsSendBox = view.findViewById(R.id.post_comments_send_box);

                //从活动中获取文章数据
                post = ((PostActivity) getActivity()).getPost();
                //创建数据请求 代理人
                delegate = new CommentsDelegate(((PostActivity) getActivity()).TAG);

                recyclerDataList = new ArrayList<>();


                //初始化列表
                initRecyclerView();
        }




        @Override
        public void onStart()
        {
                super.onStart();

                //每次访问该页面的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                getMore();


        }

        /**
         * 初始化 评论列表
         */
        private void initRecyclerView()
        {
                //配置recyclerView
                recyclerViewAdapter = new CommentsAdapter(recyclerDataList, getActivity());
                recyclerView.setAdapter(recyclerViewAdapter);

                //创建加载布局
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS * 2);

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
                        HttpCallBack httpCallBack = new HttpCallBack()
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
                                        else{
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
                        //LogUtils.e("offset "+  offset);
                        //委托代理人发送请求
                        delegate.getCommentsListByPostId(post.getId(), 0, offset, httpCallBack);
                }
        }



}
