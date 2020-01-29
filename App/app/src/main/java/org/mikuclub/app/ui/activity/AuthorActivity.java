package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mikuclub.app.adapter.AuthorAdapter;
import org.mikuclub.app.utils.custom.MyListOnScrollListener;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.controller.AuthorPostController;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.delegate.UserDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.http.Request;
import org.mikuclub.app.utils.storage.UserUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

/**
 * 搜索页面
 * search page
 */
public class AuthorActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 9;
        public static final String INTENT_AUTHOR = "author";

        /* 变量 local variable */

        //数据请求代理人
        private PostDelegate delegate;
        private UserDelegate userDelegate;
        //请求控制器
        private AuthorPostController controller;
        //列表适配器
        private AuthorAdapter recyclerViewAdapter;
        //列表数据
        private List<Post> recyclerDataList;

        private Author author;

        /* 组件 views */
        private RecyclerView recyclerView;
        private SwipeRefreshLayout swipeRefresh;
        private FloatingActionButton floatingActionButton;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_author);
                //绑定组件
                Toolbar toolbar = findViewById(R.id.toolbar);
                recyclerView = findViewById(R.id.recycler_view);
                swipeRefresh = findViewById(R.id.swipe_refresh);
                floatingActionButton = findViewById(R.id.list_floating_action_button);

                //获取作者数据
                author = (Author) getIntent().getSerializableExtra(INTENT_AUTHOR);

                //创建数据请求 代理人
                delegate = new PostDelegate(TAG);
                userDelegate = new UserDelegate(TAG);
                recyclerDataList = new ArrayList<>();

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //设置标题栏标题
                        actionBar.setTitle(author.getName());
                }

                //初始化列表
                initRecyclerView();
                //配置下拉刷新
                initSwipeRefresh();
                //初始化 活动上的浮动按钮
                initFloatingActionButton();
                //初始化控制器
                initController();

        }

        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {

                recyclerViewAdapter = new AuthorAdapter(recyclerDataList, this);
                recyclerViewAdapter.setAuthor(author);

                //创建网格布局
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(this, numberColumn);
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
         * init swipeRefresh view
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
                parameters.setAuthor(new ArrayList<>(Arrays.asList(author.getAuthor_id())));
                //如果未登陆, 排除魔法区
                if (!UserUtils.isLogin())
                {
                        parameters.setCategories_exclude(new ArrayList<>(Arrays.asList(GlobalConfig.CATEGORY_ID_MOFA)));
                }

                //创建数据控制器
                controller = new AuthorPostController(this);
                controller.setDelegate(delegate);
                controller.setUserDelegate(userDelegate);

                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setSwipeRefresh(swipeRefresh);
                controller.setParameters(parameters);

                //第一次请求数据
                controller.getMore();
                //第一次获取作者信息
                controller.getAuthor(author.getAuthor_id());

        }


        /**
         * 初始化浮动按钮
         * 绑定点击事件监听器
         * init floating action button
         * set click listener
         */
        private void initFloatingActionButton()
        {
                floatingActionButton.setOnClickListener(v -> {
                        controller.openJumPageAlertDialog();
                });
        }


        /**
         * 监听标题栏菜单动作
         * listen toolbar item click event
         *
         * @param item
         * @return
         */
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


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param author
         */
        public static void startAction(Context context, Author author)
        {
                Intent intent = new Intent(context, AuthorActivity.class);
                intent.putExtra(INTENT_AUTHOR, author);
                context.startActivity(intent);
        }


}
