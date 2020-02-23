package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.HomePrivateMessageAdapter;
import org.mikuclub.app.utils.custom.MyListOnScrollListener;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.HomePrivateMessageController;
import org.mikuclub.app.delegate.MessageDelegate;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

/**
 * 主页-消息分页-私信列表分页
 * home page: message fragment : private message list fragment
 */
public class HomeMessagePrivateFragment extends Fragment
{

        public static final int TAG = 12;

        /* 变量 local variable */
        //数据请求代理人
        private MessageDelegate delegate;
        //数据控制器
        private HomePrivateMessageController controller;
        //列表适配器
        private HomePrivateMessageAdapter recyclerViewAdapter;
        //列表数据
        private List<PrivateMessage> recyclerDataList;

        /* 组件 views */
        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;
        private RecyclerView recyclerView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_home_message_page, container, false);
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                //绑定组件
                recyclerView = view.findViewById(R.id.recycler_view);
                swipeRefresh = view.findViewById(R.id.swipe_refresh);

                //创建数据请求 代理人
                delegate = new MessageDelegate(TAG);
                //初始化变量
                recyclerDataList = new ArrayList<>();

                //初始化列表
                initRecyclerView();

                //配置下拉刷新
                initSwipeRefresh();

                //初始化控制器
                initController();
        }


        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {
                //创建数据适配器
                recyclerViewAdapter = new HomePrivateMessageAdapter(getActivity(), recyclerDataList);
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
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE_OF_MESSAGE * 2, true, true, listener);
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

                //创建数据控制器
                controller = new HomePrivateMessageController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setSwipeRefresh(swipeRefresh);

                //第一次请求数据
                controller.getMore();
        }


        @Override
        public void onStop()
        {
                //取消本碎片相关的所有网络请求
                Request.cancelRequest(TAG);

                super.onStop();
        }


}
