package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.mikuclub.app.javaBeans.resources.UserLogin;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.storage.MessageUtils;
import org.mikuclub.app.utils.storage.UserUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
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
        private Posts posts;
        //用户信息
        UserLogin userLogin;

        /*组件*/
        private DrawerLayout drawer;
        private NavigationView leftNavigationView;
        private BottomNavigationView bottomNavigationView;

        private TextView searchInput;
        private FloatingActionButton floatingActionButton;
        //侧边栏头部
        private ImageView userAvatar;
        private TextView userName;
        private TextView userEmail;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);

                Toolbar toolbar = findViewById(R.id.toolbar);

                drawer = findViewById(R.id.home_drawer_layout);
                leftNavigationView = findViewById(R.id.home_left_navigation_view);
                //获取侧边栏头部布局
                View header = leftNavigationView.getHeaderView(0);
                userAvatar = header.findViewById(R.id.user_avatar);
                userName = header.findViewById(R.id.user_name);
                userEmail = header.findViewById(R.id.user_email);

                bottomNavigationView = findViewById(R.id.home_bottom_bar);
                searchInput = findViewById(R.id.search_input);
                floatingActionButton = findViewById(R.id.list_floating_action_button);

                //从intent里读取上个活动传送来的数据
                stickyPosts = (Posts) getIntent().getSerializableExtra(INTENT_STICKY_POST_LIST);
                posts = (Posts) getIntent().getSerializableExtra(INTENT_POST_LIST);

                //替换原版标题栏
                setSupportActionBar(toolbar);

                initTopSearchBar();

                initBottomMenu();

                initLeftNavigationVIew();

                //检查登陆状态
                checkLoginStatus();


        }


        /**
         * 初始化 顶部搜索栏
         */
        private void initTopSearchBar()
        {
                //绑定点击监听器到搜索栏
                searchInput.setOnClickListener(v -> {
                        //启动搜索页面
                        SearchActivity.startAction(HomeActivity.this);
                });
        }

        /**
         * 初始化底部导航栏
         */
        private void initBottomMenu()
        {
                //在其他分页 点击返回后 会回到主页
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home, R.id.navigation_category, R.id.navigation_message)
                        .setDrawerLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController(this, R.id.home_navigation);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(bottomNavigationView, navController);



                //获取未读消息数量
                int unreadMessageCount = MessageUtils.getPrivateMessageCount() + MessageUtils.getReplyCommentCount();
                //如果未读消息大于0
                if (unreadMessageCount > 0)
                {
                        //在消息图标右上角显示提醒气泡
                        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_message);
                        //设置气泡数字
                        badge.setNumber(unreadMessageCount);
                        //设置气泡位置偏移
                        badge.setVerticalOffset(5);
                        //设置气泡背景颜色
                        badge.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }


        }

        /**
         * 初始化侧边栏, 绑定item动作监听
         */
        private void initLeftNavigationVIew()
        {

                //绑定侧边栏菜单动作监听
                leftNavigationView.setNavigationItemSelectedListener(item -> {

                        switch (item.getItemId())
                        {
                                case R.id.item_login:
                                        //启动登录页
                                        LoginActivity.startActionForResult(this);
                                        break;
                                case R.id.item_logout:
                                        //删除用户登陆信息
                                        UserUtils.logout();
                                        //更新侧边栏用户信息和菜单
                                        setLogoutUserInfoAndMenu();
                                        ToastUtils.shortToast("已登出");
                                        break;
                                case R.id.item_report:
                                        //启动问题反馈页
                                        ReposrtActivity.startAction(this);
                        }
                        //关闭侧边栏
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                });

        }


        /**
         * 检测用户登陆状态
         * 如果从未登陆过, 就什么都不做
         * 如果是登陆令牌过期, 则跳转到登陆页面
         * 如果登陆令牌有效, 则在主页侧边栏显示用户信息和替换默认侧边栏菜单
         */
        private void checkLoginStatus()
        {
                //如果用户有登陆
                if (UserUtils.isLogin())
                {

                        LogUtils.v("已登陆用户");
                        setLoggingUserInfoAndMenu();

                }
                // 如果没登陆过
                else
                {
                        LogUtils.v("未登录用户");
                        setLogoutUserInfoAndMenu();
                }

        }

        /**
         * 设置未登陆用户的信息和菜单
         */
        private void setLogoutUserInfoAndMenu()
        {

                userAvatar.setImageResource(R.drawable.person);
                userName.setText("点击头像登陆");
                userEmail.setVisibility(View.GONE);

                //头像绑定点击监听器
                userAvatar.setOnClickListener(v -> {
                        //启动登录页
                        LoginActivity.startActionForResult(this);
                });

                //替换菜单
                leftNavigationView.getMenu().clear();
                leftNavigationView.inflateMenu(R.menu.home_left_drawer_menu_logout);

        }

        /**
         * 设置已登陆用户的信息和菜单
         */
        private void setLoggingUserInfoAndMenu()
        {

                //获取用户数据
                userLogin = UserUtils.getUser();
                //设置头像
                GlideImageUtils.getSquareImg(this, userAvatar, userLogin.getAvatar_urls());
                //设置头像的动作监听器
                userAvatar.setOnClickListener(null);
                //设置名称
                userName.setText(userLogin.getUser_display_name());
                //设置+显示邮箱
                userEmail.setText(userLogin.getUser_email());
                userEmail.setVisibility(View.VISIBLE);
                //替换菜单
                leftNavigationView.getMenu().clear();
                leftNavigationView.inflateMenu(R.menu.home_left_drawer_menu_logged);

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
                super.onActivityResult(requestCode, resultCode, data);
                //判断请求id
                switch (requestCode)
                {
                        //如果是登陆页面返回的请求结果
                        case LoginActivity.REQUEST_CODE:
                                //如果结果是成功
                                if (resultCode == RESULT_OK)
                                {
                                        //在侧边栏显示已登陆用户信息 和 登陆用户菜单
                                        setLoggingUserInfoAndMenu();
                                }
                                else
                                {
                                        //如果未成功登陆, 设置未登陆菜单
                                        setLogoutUserInfoAndMenu();
                                }
                                break;
                }
        }

        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
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

        public Posts getPosts()
        {
                return posts;
        }
}
