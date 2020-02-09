package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mikuclub.app.adapter.PostManageAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.PostController;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyListOnScrollListener;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

public class PostManageActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 16;

        /* 变量 local variable */
        //数据请求代理人
        private PostDelegate delegate;
        private PostController controller;

        //列表适配器
        private PostManageAdapter recyclerViewAdapter;
        //列表数据
        private List<Post> recyclerDataList;


        /* 组件 views */
        //列表
        private RecyclerView recyclerView;
        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;
        private FloatingActionButton floatingActionButton;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_post_manage);

                //绑定组件
                Toolbar toolbar = findViewById(R.id.toolbar);
                recyclerView = findViewById(R.id.recycler_view);
                swipeRefresh = findViewById(R.id.swipe_refresh);
                floatingActionButton = findViewById(R.id.list_floating_action_button);


                //创建数据请求 代理人
                delegate = new PostDelegate(TAG);
                //创建空列表
                recyclerDataList = new ArrayList<>();

                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                }

                //加载文章列表
                initRecyclerView();
                //配置下拉刷新
                initSwipeRefresh();
                //初始化控制器
                initController();
                //绑定浮动按钮
                initFloatingActionButton();

        }

        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {


                //创建适配器
                recyclerViewAdapter = new PostManageAdapter(recyclerDataList,  this, delegate);

                //创建列表主布局
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE * 2, true, true, listener);

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

                //参数设置当前用户自己发布的文章
                parameters.setAuthor(new ArrayList<>(Collections.singletonList(UserPreferencesUtils.getUser().getId())));
                //添加除了公开以外的文章状态
                parameters.setStatus(new ArrayList<>(Arrays.asList(GlobalConfig.Post.Status.POST_MANAGE_STATUS)));

                //创建数据控制器
                controller = new PostController(this);
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setSwipeRefresh(swipeRefresh);
                controller.setParameters(parameters);
                //设置跳转后的列表位置
                controller.setScrollPositionAfterRefresh(0);

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
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item)
        {
                switch (item.getItemId())
                {
                        //如果点了返回键
                        case android.R.id.home:
                                //结束当前活动页
                                finish();
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }

        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, PostManageActivity.class);
                context.startActivity(intent);
        }
}
