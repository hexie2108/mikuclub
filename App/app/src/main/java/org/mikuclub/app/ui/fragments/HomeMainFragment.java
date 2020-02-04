package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mikuclub.app.adapter.HomeListAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.PostController;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.Posts;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.custom.MyListOnScrollListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

/**
 * 主页-首页碎片
 * Home page: main fragment
 * */
public class HomeMainFragment extends Fragment
{
        /* 变量 local variable */
        //数据请求代理人
        private PostDelegate delegate;
        private PostController controller;

        //列表适配器
        private HomeListAdapter recyclerViewAdapter;
        //列表数据
        private List<Post> recyclerDataList;
        //当前页面需要的数据
        private Posts stickyPosts;
        private Posts posts;


        /* 组件 views */
        //列表
        private RecyclerView recyclerView;
        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;
        private FloatingActionButton floatingActionButton;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_home_main, container, false);

        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);


                recyclerView = view.findViewById(R.id.recycler_view);
                swipeRefresh = view.findViewById(R.id.swipe_refresh);
                floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();

                //创建数据请求 代理人
                delegate = new PostDelegate(HomeActivity.TAG);
                //从父活动里读取数据
                stickyPosts = ((HomeActivity) getActivity()).getStickyPosts();
                posts = ((HomeActivity) getActivity()).getPosts();
                recyclerDataList = posts.getBody();

                //加载文章列表
                initRecyclerView();

                //配置下拉刷新
                initSwipeRefresh();

                //初始化控制器
                initController();
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
                super.onActivityCreated(savedInstanceState);
                initFloatingActionButton();
        }

        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {
                //创建适配器
                recyclerViewAdapter = new HomeListAdapter(recyclerDataList, stickyPosts.getBody(), getActivity());

                //创建列表网格布局
                //设置行数
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberColumn);
                //让最后一个组件(尾部) 占据2个列
                layoutManager.setSpanSizeLookup(new MyGridLayoutSpanSizeLookup(recyclerDataList, numberColumn, true));

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
                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE * 2, false, true, listener);

        }

        /**
         * 初始化 下拉刷新组件
         * 绑定刷新动作监听器
         * * init swipe refresh layout
         * * set refresh listener
         */
        private void initSwipeRefresh()
        {
                //设置进度条颜色
                swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
                //绑定动作
                swipeRefresh.setOnRefreshListener(() -> {
                        //获取最新文章
                        controller.refreshPosts(1);
                });
        }

        /**
         * 初始化控制器
         * init request controller
         */
        private void initController()
        {
                //设置查询参数
                PostParameters parameters = new PostParameters();

                //参数里 需要排除的分类id
                parameters.setCategories_exclude(new ArrayList<>(Collections.singletonList(GlobalConfig.CATEGORY_ID_MOFA)));

                //创建数据控制器
                controller = new PostController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setSwipeRefresh(swipeRefresh);
                controller.setParameters(parameters);

                //设置总页数
                controller.setTotalPage(posts.getHeaders().getTotalPage());
                //设置当前页数
                controller.setCurrentPage(1);

                //第一次请求数据

                controller.getMore();

        }

        /**
         * 初始化浮动按钮
         * 绑定点击事件监听器
         * init floating action button
         * set click listener
         */
        private void initFloatingActionButton()
        {
                floatingActionButton.setOnClickListener(
                        v ->   controller.openJumPageAlertDialog()
                );
        }

        @Override
        public void onPause()
        {
                super.onPause();


        }

        @Override
        public void onResume()
        {
                super.onResume();
        }
}
