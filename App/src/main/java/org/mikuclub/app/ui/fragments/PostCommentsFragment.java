package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.adapter.CommentAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.CommentController;
import org.mikuclub.app.delegate.CommentDelegate;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyListOnScrollListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章页 评论页面碎片
 * post page : comment list fragment
 */
public class PostCommentsFragment extends Fragment
{


        /* 变量 local variable */
        //数据请求代理人
        private CommentDelegate delegate;
        //数据控制器
        private CommentController controller;
        //列表适配器
        private CommentAdapter recyclerViewAdapter;

        private List<Comment> recyclerDataList;
        //当前页面的文章数据
        private Post post;



        /* 组件 views */
        //文章列表
        private RecyclerView recyclerView;
        private ImageView avatarImage;
        private TextInputLayout inputLayout;
        private TextInputEditText input;
        private CheckBox checkBoxNotifyAuthor;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_post_comments, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                //绑定组件
                recyclerView = view.findViewById(R.id.post_comments_recycler_view);
                avatarImage=view.findViewById(R.id.comment_input_avatar_img);
                inputLayout = view.findViewById(R.id.input_layout);
                input = view.findViewById(R.id.input);
                checkBoxNotifyAuthor = view.findViewById(R.id.check_box_notify_author);


                //从活动中获取文章数据
                post = ((PostActivity) getActivity()).getPost();
                //创建数据请求 代理人
                delegate = new CommentDelegate(PostActivity.TAG);
                //初始化变量
                recyclerDataList = new ArrayList<>();


                //初始化列表
                initRecyclerView();
                //初始化控制器
                initController();
                //初始化评论输入框
                initCommentInput();

                //第一次请求数据
                controller.getMore();
        }



        /**
         * 初始化评论框
         * init comment input  form
         */
        private void initCommentInput(){
                controller.initCommentInput(avatarImage, inputLayout, input, checkBoxNotifyAuthor);
        }


        /**
         * 初始化 RecyclerView
         * init RecyclerView list
         */
        private void initRecyclerView()
        {
                //创建数据适配器
                recyclerViewAdapter = new CommentAdapter(getActivity(), recyclerDataList);
                //如果评论数为0  修改默认错误信息
                if(GeneralUtils.listIsNullOrHasEmptyElement(post.getMetadata().getCount_comments())){
                        recyclerViewAdapter.setNotMoreErrorMessage("目前还没有评论哦");
                }

                //创建列表主布局
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(RecyclerView.VERTICAL);

                //创建列表滑动监听器
                MyListOnScrollListener listener = new MyListOnScrollListener(recyclerViewAdapter, layoutManager){
                        @Override
                        public void onExecute()
                        {
                                //加载更多
                                controller.getMore();
                        }
                };
                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS * 2, false, true, listener);
        }

        /**
         * 初始化控制器
         * init request controller
         */
        private void initController(){

                //设置查询参数
                CommentParameters parameters = new CommentParameters();
                parameters.setPost(new ArrayList<>(Collections.singletonList(post.getId())));
                //过滤子回复
                parameters.setParent(new ArrayList<>(Collections.singletonList(0)));

                //创建数据控制器
                controller = new CommentController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setParameters(parameters);
                //设置数据
                controller.setPostId(post.getId());
                controller.setParentCommentId(0);
                controller.setAuthorId(post.getAuthor());
        }



}
