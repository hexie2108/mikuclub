package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.mikuclub.app.adapters.AuthorAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.controller.AuthorPostController;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.delegates.UserDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.storage.UserUtils;
import org.mikuclub.app.utils.custom.MyGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.http.Request;

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
 */
public class AuthorActivity extends AppCompatActivity
{
        /*静态变量*/
        public static final int TAG = 9;
        public static final String INTENT_AUTHOR_ID = "author_id";
        public static final String INTENT_AUTHOR_NAME= "author_name";

        /*变量*/

        //数据请求代理人
        private PostDelegate delegate;
        private UserDelegate userDelegate;
        private AuthorPostController controller;
        //列表适配器
        private AuthorAdapter recyclerViewAdapter;
        //列表数据
        private List<Post> recyclerDataList;

        private int authorId;
        private String authorName;

        /*组件*/
        private RecyclerView recyclerView;
        //下拉刷新布局
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
                authorId =  getIntent().getIntExtra(INTENT_AUTHOR_ID, 0);
                authorName =  getIntent().getStringExtra(INTENT_AUTHOR_NAME);

                //创建数据请求 代理人
                delegate = new PostDelegate(TAG);
                userDelegate = new UserDelegate(TAG);
                recyclerDataList = new ArrayList<>();

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setTitle(authorName);
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
         * 初始化 空的文章列表
         */
        private void initRecyclerView()
        {

                recyclerViewAdapter = new AuthorAdapter(recyclerDataList, this);

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
                //设置查询参数
                PostParameters parameters = new PostParameters();
                parameters.setAuthor(new ArrayList<>(Arrays.asList(authorId)));
                //如果未登陆, 排除魔法区
                if(!UserUtils.isLogin()){
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
                controller.getAuthor(authorId);

        }


        /**
         * 为父活动上的浮动按钮点击事件绑定动作
         */
        private void initFloatingActionButton()
        {
                floatingActionButton.setOnClickListener(v -> {
                        controller.openJumPageAlertDialog();
                });
        }


        //监听标题栏菜单动作
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


        /**静态 启动本活动的方法
         *
         * @param context
         * @param author_id
         * @param author_name
         */
        public static void startAction(Context context, int author_id, String author_name)
        {
                Intent intent = new Intent(context, AuthorActivity.class);
                intent.putExtra(INTENT_AUTHOR_ID, author_id);
                intent.putExtra(INTENT_AUTHOR_NAME, author_name);
                context.startActivity(intent);
        }


}
