package org.mikuclub.app.ui.fragments.windows;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.adapter.CommentAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.CommentController;
import org.mikuclub.app.delegate.CommentDelegate;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.ScreenUtils;
import org.mikuclub.app.utils.custom.MyListOnScrollListener;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章页 评论回复窗口碎片
 * Post page: comment replay windows fragment
 */
public class CommentRepliesFragment extends BottomSheetDialogFragment
{
        /* 静态变量 Static variable */
        public static final int TAG = 5;
        public static final String BUNDLE_COMMENT = "comment";

        /* 变量 local variable */
        //数据请求代理人
        private CommentDelegate delegate;
        //数据控制器
        private CommentController controller;
        //列表适配器
        private CommentAdapter.RepliesAdapter recyclerViewAdapter;
        //列表数据
        private List<Comment> recyclerDataList;
        //获取评论数据
        private Comment comment;

        /* 组件 views */
        //父评论主体
        private ConstraintLayout item;
        private ImageView itemAvatarImg;
        private TextView itemName;
        private TextView itemDate;
        private TextView itemContent;
        private TextView itemCountReplies;
        private RecyclerView recyclerView;
        private Button returnButton;

        //用户评论发送框
        private ImageView avatarImage;
        private TextInputLayout inputLayout;
        private TextInputEditText input;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
                return inflater.inflate(R.layout.fragment_comment_replies_windows, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {

                super.onViewCreated(view, savedInstanceState);

                //绑定组件
                item = view.findViewById(R.id.item_comment);
                itemAvatarImg = view.findViewById(R.id.item_avatar_img);
                itemName = view.findViewById(R.id.item_name);
                itemDate = view.findViewById(R.id.item_date);
                itemContent = view.findViewById(R.id.item_content);
                itemCountReplies = view.findViewById(R.id.item_count_replies);
                recyclerView = view.findViewById(R.id.post_comments_replies_recycler_view);
                returnButton = view.findViewById(R.id.return_button);

                avatarImage = view.findViewById(R.id.comment_input_avatar_img);
                inputLayout = view.findViewById(R.id.input_layout);
                input = view.findViewById(R.id.input);

                //获取传递的数据
                if(getArguments()!=null){
                        comment = (Comment) getArguments().getSerializable(BUNDLE_COMMENT);
                }

                //创建数据请求 代理人
                delegate = new CommentDelegate(PostActivity.TAG);

                //初始化变量
                recyclerDataList = new ArrayList<>();

                //初始化父评论
                initParentComment();
                //初始化列表
                initRecyclerView();
                //初始化控制器
                initController();
                //初始化评论输入框
                initCommentInput();


                //绑定返回按钮
                returnButton.setOnClickListener(v -> {
                        //关闭窗口
                        CommentRepliesFragment.this.dismiss();
                });

                //动态调整布局高度
                ScreenUtils.setHeightForWindowsFragment(getActivity(), view);

        }


        /**
         * 初始化评论发送框
         * init comment input form
         */
        private void initCommentInput()
        {
                controller.initCommentInput(avatarImage, inputLayout, input, null);
                //设置默认回复对象
                controller.changeParentComment(comment, true);
        }

        /**
         * 初始化父评论
         * init parent comment
         */
        private void initParentComment()
        {
                //为视图设置数据
                itemName.setText(comment.getAuthor_name());
                //生成时间格式
                String dateString = GeneralUtils.DateToString(comment.getDate());
                itemDate.setText(dateString);
                //加载远程图片
                GlideImageUtils.getSquareImg(getActivity(), itemAvatarImg, comment.getAuthor_avatar_urls().getSize96());

                //显示解析过html的内容
                HttpUtils.parseHtmlDefault(getActivity(), comment.getContent().getRendered(), itemContent);

                item.setOnClickListener(v -> {
                        //原始父评论点击的话 就恢复为默认回复对象
                        controller.changeParentComment(comment, false);
                });
        }

        /**
         * 初始化 RecyclerView
         * init RecyclerView list
         */
        private void initRecyclerView()
        {
                //创建数据适配器
                recyclerViewAdapter = new CommentAdapter.RepliesAdapter(recyclerDataList, getActivity());

                //创建列表主布局
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(RecyclerView.VERTICAL);

                //创建列表滑动监听器
                MyListOnScrollListener listener = new MyListOnScrollListener(recyclerViewAdapter, layoutManager)
                {
                        @Override
                        public void onExecute()
                        {
                                //加载更多
                                controller.getMore();
                        }
                };

                //配置列表, (hasNestedScrollingEnabled 为否, 是因为要禁用窗口滑动)
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS * 2, false, false, listener);
        }


        /**
         * 初始化控制器
         * init request controller
         */
        private void initController()
        {
                //设置查询参数
                CommentParameters parameters = new CommentParameters();

                //生成子回复id列表
                ArrayList<Integer> parentList = new ArrayList<>();
                parentList.add(comment.getId());
                if (!GeneralUtils.listIsNullOrHasEmptyElement(comment.getMetadata().getComment_reply_ids()))
                {
                        parentList.addAll(comment.getMetadata().getComment_reply_ids());
                }
                //设置参数
                parameters.setPost(new ArrayList<>(Collections.singletonList(comment.getPost())));
                parameters.setParent(parentList);
                parameters.setOrder(GlobalConfig.Post.Order.ASC);


                //创建数据控制器
                controller = new CommentController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setParameters(parameters);

                //设置数据
                controller.setPostId(comment.getPost());

                //第一次请求数据
                controller.getMore();
                //给适配器提供 controller 来实现点击事件
                recyclerViewAdapter.setController(controller);

        }


        /**
         * 禁止浮动页面滑动
         * Disable sliding of windows
         *
         * @param dialog
         * @param style
         */
        @Override
        public void setupDialog(Dialog dialog, int style)
        {
                ScreenUtils.disableDraggingOfBottomSheetDialogFragment(dialog);
        }

        @Override
        public void onStop()
        {
                //取消本碎片相关的所有网络请求
                Request.cancelRequest(TAG);

                super.onStop();
        }

        /**
         * 本碎片的静态启动方法
         * static method to start current fragment
         *
         * @param comment
         * @return
         */
        public static CommentRepliesFragment startAction(Comment comment)
        {
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_COMMENT, comment);
                CommentRepliesFragment fragment = new CommentRepliesFragment();
                fragment.setArguments(bundle);
                return fragment;
        }


}
