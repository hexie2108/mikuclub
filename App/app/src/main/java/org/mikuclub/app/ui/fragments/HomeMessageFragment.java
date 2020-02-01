package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.mikuclub.app.adapter.viewPager.MessageViewPagerAdapter;
import org.mikuclub.app.storage.MessagePreferencesUtils;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.ui.activity.LoginActivity;
import org.mikuclub.app.ui.activity.base.MyFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

/**
 * 主页-消息分页
 * Home page: message fragment
 */
public class HomeMessageFragment extends MyFragment
{


        /* 变量 local variable */
        //第一次查看
        private boolean isFirstView = true;
        //第一次创建
        private boolean isFirstCreated = true;
        private MessageViewPagerAdapter viewPagerAdapter;

        /* 组件 views */
        //分页菜单栏
        private TabLayout tabsMenuLayout;
        //分页管理器
        private ViewPager2 viewPager;
        //提示信息
        private TextView infoText;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_home_message, container, false);
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                viewPager = view.findViewById(R.id.view_pager);
                tabsMenuLayout = view.findViewById(R.id.tabs_menu);
                infoText = view.findViewById(R.id.info_text);


        }


        @Override
        public void onStart()
        {
                super.onStart();
                checkUserLogin();
        }

        /**
         * 根据用户登陆状态 决定输出内容
         * Determine the output content according to the user login status
         */
        private void checkUserLogin(){
                //如果用户有登陆
                if (UserPreferencesUtils.isLogin())
                {
                        //如果是第一次创建
                        if (isFirstCreated)
                        {
                                isFirstCreated = false;
                                //显示分页和分页菜单
                                viewPager.setVisibility(View.VISIBLE);
                                tabsMenuLayout.setVisibility(View.VISIBLE);
                                //隐藏提示信息
                                infoText.setVisibility(View.GONE);
                                //初始化 分页显示器
                                initViewPager();
                        }
                }
                else
                {
                        //如果是第一次访问
                        if (isFirstView)
                        {
                                isFirstView = false;
                                //启动登陆页面
                                LoginActivity.startActionForResult(getActivity());
                        }
                        else
                        {
                                //隐藏分页和分页菜单
                                viewPager.setVisibility(View.GONE);
                                tabsMenuLayout.setVisibility(View.GONE);
                                //显示提示信息
                                infoText.setVisibility(View.VISIBLE);
                        }
                }
        }


        /**
         * 初始化 分页显示器
         * init view pager
         */
        private void initViewPager()
        {
                viewPagerAdapter = new MessageViewPagerAdapter(this);
                viewPager.setAdapter(viewPagerAdapter);


                new TabLayoutMediator(tabsMenuLayout, viewPager,
                        (tab, position) -> {
                                //第一个分页位
                                if (position == 0)
                                {
                                        //私信页面
                                        tab.setText("我的私信");
                                }
                                //后续分页位
                                else
                                {
                                        //评论回复
                                        tab.setText("评论回复");
                                }
                        }).attach();

                //如果未读私信大于0
                if (MessagePreferencesUtils.getPrivateMessageCount() > 0)
                {
                        //在菜单右上角创建提醒气泡
                        BadgeDrawable badge = tabsMenuLayout.getTabAt(0).getOrCreateBadge();
                        //设置气泡数字
                        badge.setNumber(MessagePreferencesUtils.getPrivateMessageCount());
                        //设置气泡背景颜色
                        badge.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                //如果未读评论大于0
                if (MessagePreferencesUtils.getReplyCommentCount() > 0)
                {
                        //在菜单右上角创建提醒气泡
                        BadgeDrawable badge = tabsMenuLayout.getTabAt(1).getOrCreateBadge();
                        //设置气泡数字
                        badge.setNumber(MessagePreferencesUtils.getReplyCommentCount());
                        //设置气泡背景颜色
                        badge.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
        }


}
