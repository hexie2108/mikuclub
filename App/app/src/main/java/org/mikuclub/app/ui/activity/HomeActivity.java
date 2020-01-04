package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;


import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.http.Request;

import mikuclub.app.R;

/**
 * 主页
 */
public class HomeActivity extends AppCompatActivity
{
        /*静态变量*/
        public static final int TAG = 2;
        public static final String INTENT_STICKY_POST_LIST = "sticky_post_list";
        public static final String INTENT_POST_LIST = "post_list";

        /*变量*/
        private AppBarConfiguration mAppBarConfiguration;
        private Posts stickyPosts;
        private Posts postList;

        /*组件*/
        private BottomNavigationView bottomNavigationView;
        private DrawerLayout drawer;
        private TextView searchInput;
        private FloatingActionButton floatingActionButton;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);


                Toolbar toolbar = findViewById(R.id.home_toolbar);
                bottomNavigationView = findViewById(R.id.home_bottom_bar);
                drawer = findViewById(R.id.home_drawer_layout);
                searchInput = findViewById(R.id.search_input);
                floatingActionButton = findViewById(R.id.list_floating_action_button);


                //从intent里读取上个活动传送来的数据
                stickyPosts = (Posts) getIntent().getSerializableExtra(INTENT_STICKY_POST_LIST);
                postList = (Posts) getIntent().getSerializableExtra(INTENT_POST_LIST);

                //替换原版标题栏
                setSupportActionBar(toolbar);

                initTopSearchBar();

                initBottomMenu();

        }

        @Override
        protected void onStop()
        {

                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }

        /**
         * 初始化底部导航栏
         */
        private void initBottomMenu()
        {
                //在其他分页 点击返回后 会回到主页
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home, R.id.navigation_category, R.id.navigation_create)
                        .setDrawerLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController(this, R.id.home_navigation);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        /**
         * 初始化 顶部搜索栏
         */
        private void initTopSearchBar()
        {
                //绑定点击监听器到搜索栏
                searchInput.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                //启动搜索页面
                                SearchActivity.startAction(HomeActivity.this);
                        }
                });
        }

        /**
         * 加载自定义菜单
         *
         * @param menu
         * @return
         */
        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
                return super.onCreateOptionsMenu(menu);
        }

        /**
         * 修正返回键动作
         *
         * @return
         */
        @Override
        public boolean onSupportNavigateUp()
        {
                NavController navController = Navigation.findNavController(this, R.id.home_navigation);
                return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                        || super.onSupportNavigateUp();
        }

        /**
         * 静态 启动本活动的方法
         *
         * @param context
         * @param stickyPostList
         * @param postList
         */
        public static void startAction(Context context, Posts stickyPostList, Posts postList)
        {

                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("sticky_post_list", stickyPostList);
                intent.putExtra("post_list", postList);
                context.startActivity(intent);

        }

        /**
         * 获取浮动按钮组件
         *
         * @return
         */
        public FloatingActionButton getFloatingActionButton()
        {
                return floatingActionButton;
        }

        public Posts getStickyPosts()
        {
                return stickyPosts;
        }
        public Posts getPostList()
        {
                return postList;
        }
}
