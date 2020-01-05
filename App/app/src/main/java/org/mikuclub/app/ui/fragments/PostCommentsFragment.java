package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.CommentsAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.controller.CommentController;
import org.mikuclub.app.delegates.CommentDelegate;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;

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


        /*变量*/
        //数据请求代理人
        private CommentDelegate delegate;
        //数据控制器
        private CommentController controller;
        //列表适配器
        private CommentsAdapter recyclerViewAdapter;

        private List<Comment> recyclerDataList;
        //当前页面的文章数据
        private Post post;


        /*组件*/
        //文章列表
        private RecyclerView recyclerView;
        private ConstraintLayout postCommentsSendBox;


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
                postCommentsSendBox = view.findViewById(R.id.post_comments_send_box);

                //从活动中获取文章数据
                post = ((PostActivity) getActivity()).getPost();
                //创建数据请求 代理人
                delegate = new CommentDelegate(((PostActivity) getActivity()).TAG);
                //初始化变量
                recyclerDataList = new ArrayList<>();

                //初始化列表
                initRecyclerView();
                //初始化控制器
                initController();
        }

        @Override
        public void onStart()
        {
                super.onStart();
                //每次访问该页面的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                controller.getMore();
        }


        /**
         * 初始化 评论列表
         */
        private void initRecyclerView()
        {
                //创建数据适配器
                recyclerViewAdapter = new CommentsAdapter(recyclerDataList, getActivity());
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
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS * 2, true, true, listener);
        }

        /**
         * 初始化控制器
         */
        private void initController(){
                //设置查询参数
                CommentParameters parameters = new CommentParameters();
                parameters.setPost(post.getId());
                //过滤评论的子回复
                parameters.setParent(0);

                //创建数据控制器
                controller = new CommentController(getActivity(), delegate, recyclerView, parameters);
        }



}