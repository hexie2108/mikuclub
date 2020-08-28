package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.javaBeans.response.Posts;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.storage.MessagePreferencesUtils;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.ui.fragments.HomeCategoriesFragment;
import org.mikuclub.app.ui.fragments.HomeMainFragment;
import org.mikuclub.app.ui.fragments.HomeMessageFragment;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.file.FileUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.Request;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import mikuclub.app.R;

/**
 * 主页
 * home page
 */
public class HomeActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 2;
        public static final String INTENT_STICKY_POST_LIST = "sticky_post_list";
        public static final String INTENT_POST_LIST = "post_list";
        private static final String TAG_HOME_MAIN_FRAGMENT = "homeMainFragment";
        private static final String TAG_HOME_CATEGORIES_FRAGMENT = "homeCategoriesFragment";
        private static final String TAG_HOME_MESSAGE_FRAGMENT = "homeMessageFragment";

        /* 变量 local variable */

        private Posts stickyPosts;
        private Posts posts;
        //用户信息
        UserLogin userLogin;

        private Fragment homeMainFragment;
        private Fragment homeCategoriesFragment;
        private Fragment homeMessageFragment;
        //当前激活的fragment
        private Fragment currentActiveFragment;
        //获取碎片管理器
        private FragmentManager fm;

        //确认是否真要退出, 屏蔽用户第一次的退出点击防止是误碰
        private boolean doYouReallyWantExit = false;


        /* 组件 views */
        private DrawerLayout drawer;
        private NavigationView leftNavigationView;
        private BottomNavigationView bottomNavigationView;

        private TextView searchInput;
        private FloatingActionButton floatingActionButton;
        //侧边栏头部
        private ImageView userAvatar;
        private TextView userName;
        private TextView userEmail;

        //消息提示气泡
        private BadgeDrawable messaggeCountbadge;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);

                //绑定组件
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
                fm = getSupportFragmentManager();


                //替换原版标题栏
                setSupportActionBar(toolbar);

                initTopSearchBar();

                initBottomMenu();

                initLeftNavigationView();

                //检查登陆状态
                checkLoginStatus();

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

        }


        /**
         * 初始化 顶部搜索栏
         * init top search bar view
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
         * 初始化底部导航菜单栏,
         * 加载显示第一个分页
         * 如果有未读消息, 显示气泡提醒
         * init bottom navigation view
         */
        private void initBottomMenu()
        {
                bottomNavigationView.setOnNavigationItemSelectedListener
                        (item -> {

                                switch (item.getItemId())
                                {
                                        case R.id.navigation_home:
                                                changeFragment(homeMainFragment, TAG_HOME_MAIN_FRAGMENT);
                                                break;
                                        case R.id.navigation_category:
                                                changeFragment(homeCategoriesFragment, TAG_HOME_CATEGORIES_FRAGMENT);
                                                break;
                                        case R.id.navigation_message:
                                                changeFragment(homeMessageFragment, TAG_HOME_MESSAGE_FRAGMENT);
                                                messaggeCountbadge.setVisible(false);
                                                break;
                                }
                                return true;
                        });
                //创建主页第一个碎片
                changeFragment(homeMainFragment, TAG_HOME_MAIN_FRAGMENT);

                //获取未读消息数量
                int unreadMessageCount = MessagePreferencesUtils.getPrivateMessageCount() + MessagePreferencesUtils.getReplyCommentCount();

                //在消息图标右上角显示提醒气泡
                messaggeCountbadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_message);

                //如果未读消息大于0
                if (unreadMessageCount > 0)
                {
                        //设置气泡数字
                        messaggeCountbadge.setNumber(unreadMessageCount);
                        //设置气泡位置偏移
                        messaggeCountbadge.setVerticalOffset(5);
                        //设置气泡背景颜色
                        messaggeCountbadge.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                else{
                        messaggeCountbadge.setVisible(false);
                }


        }

        /**
         * 创建和切换fragment分页
         * 通过隐藏和显示的切换方式, 避免重复attach和detach
         * Create and toggle fragment pagination
         * By switching between hide and show, avoid repeated attach and detach
         *
         * @param fragment
         */
        private void changeFragment(Fragment fragment, String fragmentTag)
        {
                LogUtils.w("切换 FRAGMENT");
                //创建新fragment
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                //如果当前有在显示fragment
                if (currentActiveFragment != null)
                {
                        //隐藏当前fragment
                        fragmentTransaction = fragmentTransaction.hide(currentActiveFragment);
                }
                //如果是空的 说明是首次创建
                else
                {
                        //遍历所有旧fragment
                        for (Fragment eachFragment : fm.getFragments())
                        {
                                //删除之前的每个旧fragment
                                fragmentTransaction = fragmentTransaction.remove(eachFragment);
                        }
                }

                //如果对应的fragment还未生成, 或者fragment是切换到分类页的情况
                // (因为通过显示/隐藏方式会造成分类页fragment在登陆后不显示魔法分类的问题, 所以每次切换到分类页就重新生成新的fragment)
                if (fragment == null || fragmentTag.equals(TAG_HOME_CATEGORIES_FRAGMENT))
                {
                        //根据tag创建对应fragment
                        switch (fragmentTag)
                        {
                                case TAG_HOME_MAIN_FRAGMENT:
                                        homeMainFragment = new HomeMainFragment();
                                        fragment = homeMainFragment;
                                        break;
                                case TAG_HOME_CATEGORIES_FRAGMENT:
                                        //如果分类页已经创建过
                                        if (fragment != null)
                                        {
                                                //删除之前的旧分类页
                                                fragmentTransaction = fragmentTransaction.remove(fragment);
                                        }
                                        homeCategoriesFragment = new HomeCategoriesFragment();
                                        fragment = homeCategoriesFragment;
                                        break;
                                case TAG_HOME_MESSAGE_FRAGMENT:
                                        homeMessageFragment = new HomeMessageFragment();
                                        fragment = homeMessageFragment;
                                        break;
                        }
                        //添加并显示新fragment
                        fragmentTransaction = fragmentTransaction.add(R.id.home_navigation, fragment, fragmentTag);
                }
                //如果已生成过
                else
                {
                        fragmentTransaction = fragmentTransaction.show(fragment);
                }
                //更新当前fragment
                currentActiveFragment = fragment;
                //提交fragment变更
                fragmentTransaction.commit();

                //如果最后显示的fragment 是 主页
                if (currentActiveFragment == homeMainFragment)
                {
                        //显示按钮
                        floatingActionButton.setVisibility(View.INVISIBLE);
                }
                //如果是其他分页
                else
                {
                        //隐藏按钮
                        floatingActionButton.setVisibility(View.GONE);
                }

        }


        /**
         * 初始化侧边栏
         * 绑定菜单item的点击事件监听器
         * init navigation view on left sidebar
         */
        private void initLeftNavigationView()
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
                                        UserPreferencesUtils.logout();
                                        //更新侧边栏用户信息和菜单
                                        setLogoutUserInfoAndMenu();
                                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.logout_notice));
                                        break;
                                case R.id.item_sponsor:
                                        //启动文章页 赞助
                                        PostActivity.startAction(this, GlobalConfig.SPONSOR_POST_ID);
                                        break;
                                case R.id.item_shopping:
                                        //启动淘宝 (启动apk 或者 启动浏览器)
                                        HttpUtils.startWebViewIntent(this, GlobalConfig.ThirdPartyApplicationInterface.TAOBAO_APK_URL, GlobalConfig.ThirdPartyApplicationInterface.TAOBAO_WEB_URL);
                                        break;
                                case R.id.item_report:
                                        //启动问题反馈页
                                        ReportActivity.startAction(this);
                                        break;

                                case R.id.item_settings:
                                        //启动配置页
                                        SettingsActivity.startAction(this);
                                        break;
                                case R.id.item_user_profile:
                                        //启动个人信息页
                                        UserProfileActivity.startActionFroResult(this);
                                        break;
                                case R.id.item_post_manage:
                                        //启动投稿管理
                                        PostManageActivity.startAction(this);
                                        break;
                                case R.id.item_submit_post:
                                        PostSubmitActivity.startAction(this);
                                        break;

                                case R.id.item_user_favorite:
                                        FavoriteActivity.startAction(this);
                                        break;

                                case R.id.item_user_history:
                                        HistoryActivity.startAction(this);
                                        break;
                        }
                        //关闭侧边栏
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                });

        }


        /**
         * 检测用户登陆状态
         * 根据用户登陆状态, 加载不同的侧边栏菜单
         * check user login status
         * Load different sidebar based on user login status
         */
        private void checkLoginStatus()
        {
                //如果用户有登陆
                if (UserPreferencesUtils.isLogin())
                {
                        LogUtils.v("登陆用户");
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
         * Set up information and menus for non-login users
         */
        private void setLogoutUserInfoAndMenu()
        {

                userAvatar.setImageResource(R.drawable.person);
                userName.setText(ResourcesUtils.getString(R.string.login_by_avatar));
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
         * Set up information and menus for logged users
         */
        private void setLoggingUserInfoAndMenu()
        {
                //获取用户数据
                userLogin = UserPreferencesUtils.getUser();

                //设置头像
                GlideImageUtils.getSquareImg(this, userAvatar, userLogin.getAvatar_urls());
                //设置头像的动作监听器
                userAvatar.setOnClickListener(v -> {
                        //启动个人信息页
                        UserProfileActivity.startActionFroResult(this);
                });
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

                checkLoginStatus();
        }

        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }

        @Override
        protected void onDestroy()
        {
                super.onDestroy();

                //清空缓存文件夹
                FileUtils.clearCacheDirectory(this);

        }

        @Override
        public void onBackPressed()
        {
                //如果侧边栏有开启
                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                        //关闭侧边栏
                        drawer.closeDrawer(GravityCompat.START);
                }
                //如果当前页面不是主页的碎片, 就屏蔽退出键, 切换显示主页碎片
                else if (currentActiveFragment != homeMainFragment)
                {
                        //切换分页到主页
                        changeFragment(homeMainFragment, TAG_HOME_MAIN_FRAGMENT);
                        //让主页菜单图标变成选中状态
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
                //屏蔽第一次退出点击, 防止是误碰
                else if (!doYouReallyWantExit)
                {
                        doYouReallyWantExit = true;
                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.exit_confirm));
                }
                else
                {
                        super.onBackPressed();
                }

        }


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

        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param stickyPostList
         * @param postList
         */
        public static void startAction(Context context, Posts stickyPostList, Posts postList)
        {

                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra(INTENT_STICKY_POST_LIST, stickyPostList);
                intent.putExtra(INTENT_POST_LIST, postList);
                context.startActivity(intent);

        }
}
