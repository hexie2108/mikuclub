package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;


import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.http.Request;

import mikuclub.app.R;

/**
 * la classe Activity gestisce solo la interfaccia utente
 */
public class HomeActivity extends AppCompatActivity
{
        public static final int TAG = 2;


        private BottomNavigationView bottomNavigationView;
        private DrawerLayout drawer;
        private AppBarConfiguration mAppBarConfiguration;

        //搜索栏
        private TextView searchInput;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);

                //替换原版标题栏
                Toolbar toolbar = findViewById(R.id.home_toolbar);
                setSupportActionBar(toolbar);

                bottomNavigationView =findViewById(R.id.home_bottom_bar);
                drawer= findViewById(R.id.home_drawer_layout);
                searchInput = findViewById(R.id.search_input);

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


                initBottomMenu();



        }

        /**
         * 配置底部导航栏
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



        @Override
        protected void onStop()
        {

                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);

                super.onStop();



        }

        /**
         * 加载自定义菜单
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
         * @param context
         * @param stickyPostList
         * @param postList
         */
        public static void startAction(Context context, Posts stickyPostList, Posts postList){

                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("sticky_post_list", stickyPostList);
                intent.putExtra("post_list", postList);
                context.startActivity(intent);
        }


}
