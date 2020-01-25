package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.mikuclub.app.adapters.CommentsAdapter;
import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.PrivateMessagesAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.adapters.viewPager.MessageViewPagerAdapter;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.controller.PostController;
import org.mikuclub.app.controller.PrivateMessageController;
import org.mikuclub.app.delegates.MessageDelegate;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.resources.base.Post;
import org.mikuclub.app.javaBeans.resources.base.PrivateMessage;
import org.mikuclub.app.ui.activity.CategoryActivity;
import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.storage.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

/**
 * 主页活动-分类碎片
 */
public class HomeMessagePrivateFragment extends Fragment
{

        /*变量*/
        //数据请求代理人
        private MessageDelegate delegate;
        //数据控制器
        private PrivateMessageController controller;
        //列表适配器
        private PrivateMessagesAdapter recyclerViewAdapter;
        //列表数据
        private List<PrivateMessage> recyclerDataList;

        /*组件*/
        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;
        private RecyclerView recyclerView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_home_message_private, container, false);
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                //绑定组件
                recyclerView = view.findViewById(R.id.recycler_view);
                swipeRefresh = view.findViewById(R.id.swipe_refresh);

                //创建数据请求 代理人
                delegate = new MessageDelegate(((HomeActivity) getActivity()).TAG);
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
        public void onStart()
        {

                super.onStart();

                //每次开始的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                controller.getMore();
        }

        /**
         * 初始化列表
         */
        private void initRecyclerView()
        {
                //创建数据适配器
                recyclerViewAdapter = new PrivateMessagesAdapter(recyclerDataList, getActivity());
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
                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS * 2, true, true, listener);
        }

        /**
         * 配置上拉刷新动作
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
         */
        private void initController()
        {

                //创建数据控制器
                controller = new PrivateMessageController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setSwipeRefresh(swipeRefresh);
        }
}
