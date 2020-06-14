package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.PostAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.PostController;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Category;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.CategoryActivity;
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
 * 分类页: 单独分类的碎片
 * category page: single category fragment
 */
public class CategoryFragment extends Fragment
{
        /* 静态变量 Static variable */
        private static final String BUNDLE_CATEGORY = "category";

        /* 变量 local variable */
        //数据请求代理人
        private PostDelegate delegate;
        //数据控制器
        private PostController controller;
        //列表适配器
        private PostAdapter recyclerViewAdapter;
        //列表数据
        private List<Post> recyclerDataList;
        //当前页面需要的分类数据
        private Category category;

        /* 组件 views */
        private SwipeRefreshLayout swipeRefresh;
        private RecyclerView recyclerView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.recycler_view, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);


                //绑定组件
                recyclerView = view.findViewById(R.id.recycler_view);
                swipeRefresh = view.findViewById(R.id.swipe_refresh);

                //获取传递的数据
                if(getArguments() != null){
                        category = (Category) getArguments().getSerializable(BUNDLE_CATEGORY);
                }

                //创建数据请求 代理人
                delegate = new PostDelegate(CategoryActivity.TAG);
                //初始化变量
                recyclerDataList = new ArrayList<>();

                //初始化列表
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
                //初始化 活动上的浮动按钮
                initFloatingActionButton();
        }


        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {

                //创建数据适配器
                recyclerViewAdapter = new PostAdapter(getActivity(), recyclerDataList);

                //创建列表网格布局
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberColumn);
                //让最后一个组件(尾部) 占据2个列
                layoutManager.setSpanSizeLookup(new MyGridLayoutSpanSizeLookup(recyclerDataList, numberColumn, false));

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
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE * 2, true, true, listener);
        }

        /**
         * 初始化 下拉刷新组件
         * 绑定刷新动作监听器
         * init swipe refresh layout
         * set refresh listener
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
                //参数里 需要的分类id
                parameters.setCategories(new ArrayList<>(Collections.singletonList(category.getId())));

                //创建数据控制器
                controller = new PostController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setSwipeRefresh(swipeRefresh);
                controller.setParameters(parameters);

                //第一次请求数据
                controller.getMore();
        }


        /**
         * 初始化浮动按钮
         * 绑定点击事件监听器
         * init floating action button
         * set click listener (显示跳转弹窗)
         */
        private void initFloatingActionButton()
        {
                ((CategoryActivity) getActivity()).getFloatingActionButton().setOnClickListener(v -> {
                        controller.openJumPageAlertDialog();
                });

        }


        /**
         * 本碎片的静态启动方法
         * static method to start current fragment
         *
         * @param category
         * @return
         */
        public static CategoryFragment startAction(Category category)
        {
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_CATEGORY, category);
                CategoryFragment fragment = new CategoryFragment();
                fragment.setArguments(bundle);
                return fragment;
        }

}
