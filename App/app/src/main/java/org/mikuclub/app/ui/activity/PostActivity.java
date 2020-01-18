package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


import com.google.android.material.tabs.TabLayoutMediator;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.adapters.viewPager.PostViewPagerAdapter;
import org.mikuclub.app.javaBeans.resources.base.Post;
import org.mikuclub.app.ui.fragments.PostMainFragment;
import org.mikuclub.app.ui.fragments.windows.DownloadFragment;
import org.mikuclub.app.ui.fragments.windows.SharingFragment;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

/**
 * 文章页
 */
public class PostActivity extends AppCompatActivity
{
        /*静态变量*/
        public static final int TAG = 4;
        public static final String INTENT_POST = "post";

        /*变量*/
        private Post post;
        private List<String> imagesSrc;

        //碎片适配器
        private PostViewPagerAdapter postViewPagerAdapter;
        private SharingFragment sharingWindowsFragment;


        /*组件*/
        private AppBarLayout appBarLayout;
        private CollapsingToolbarLayout postCollapsingToolbarLayout;
        //标题栏
        private Toolbar toolbar;
        //标题栏幻灯片
        private BannerViewPager sliderViewPager;
        //标题栏幻灯片指示器
        private TextIndicator textIndicator;
        //分页菜单栏的容器
        private LinearLayout tabsMenuBox;
        //分页菜单栏
        private TabLayout tabsMenu;
        //分页显示器
        private ViewPager2 viewPager;
        private FloatingActionButton postDownloadButton;




        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                //让系统状态栏透明
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                //
                setContentView(R.layout.activity_post);

                appBarLayout = findViewById(R.id.post_app_bar);
                postCollapsingToolbarLayout = findViewById(R.id.post_collapsing_toolbar_layout);
                toolbar = findViewById(R.id.post_toolbar);
                //获取幻灯片组件
                sliderViewPager = findViewById(R.id.post_slider_viewpager);
                textIndicator = findViewById(R.id.post_slider_indicator);
                //获取分页菜单
                tabsMenu = findViewById(R.id.post_tabs_menu);
                tabsMenuBox = findViewById(R.id.post_tabs_menu_box);
                //获取分页显示器
                viewPager = findViewById(R.id.post_view_pager);
                postDownloadButton = findViewById(R.id.post_download_button);

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //隐藏标题
                        actionBar.setDisplayShowTitleEnabled(false);
                }

                //获取文章数据
                post = (Post) getIntent().getSerializableExtra(INTENT_POST);
                //获取图片地址列表
                imagesSrc = post.getMetadata().getImages_src();

                initSliders();

                initViewPager();

                initDownButton();

                //根据appBar高度更改标题栏图标颜色
               changeHomeIconColorListener();
                //屏蔽点击事件, 防止在菜单栏容器上的点击 激活下层幻灯片图片的事件
                tabsMenuBox.setOnClickListener(v -> {
                });

        }

        /**
         * 初始化图片幻灯片
         **/
        private void initSliders()
        {
                //创建幻灯片构造器
                PageBean.Builder builder = new PageBean.Builder<String>();
                //如果只有1张图
                if (imagesSrc.size() == 1)
                {
                        //关闭幻灯片循环
                        builder = builder
                                .useCode(true)
                                .cycle(false);
                }
                PageBean bean = builder
                        .data(imagesSrc)
                        .indicator(textIndicator)
                        .builder();

                sliderViewPager.setPageListener(bean, R.layout.slider_view_item_post, (PageHelperListener<String>) (view, itemSrc) -> {
                        //加载图片
                        ImageView imageView = view.findViewById(R.id.item_image);

                        String thumbnailSrc;
                        //如果是第一张图
                        if (itemSrc.equals(imagesSrc.get(0)))
                        {
                                //直接使用预设的缩微图
                                thumbnailSrc = post.getMetadata().getThumbnail_src().get(0);
                        }
                        //后续图片
                        else
                        {
                                //计算缩微图地址
                                thumbnailSrc = GeneralUtils.getThumbnailSrcByImageSrc(itemSrc);
                        }
                        //加载图片 (先加载缩微图 之后加载原图)
                        GlideImageUtils.getWithThumbnail(PostActivity.this, imageView, itemSrc, thumbnailSrc);

                        view.setOnClickListener(v -> {

                                //找到当前图片地址的列表位置
                                int position = imagesSrc.indexOf(itemSrc);
                                //新建列表
                                ArrayList<String> newImagesSrc = new ArrayList<>();
                                //截取当前位置和后续位置的地址
                                newImagesSrc.addAll(imagesSrc.subList(position, imagesSrc.size()));
                                //然后再添加 开头位置 到 当前位置-1 的地址
                                newImagesSrc.addAll(imagesSrc.subList(0, position));
                                //以此达到重建新列表的目标

                                //启动单独的图片查看页面
                                ImageActivity.startAction(PostActivity.this, newImagesSrc);
                        });
                });

        }


        /**
         * 初始化 文章主体fragment
         */
        private void initViewPager()
        {
                postViewPagerAdapter = new PostViewPagerAdapter(this);

                viewPager.setAdapter(postViewPagerAdapter);
                //设置分页菜单名称
                new TabLayoutMediator(tabsMenu, viewPager,
                        (tab, position) -> {
                                if (position == 0)
                                {
                                        tab.setText("描述");
                                }
                                else if (position == 1)
                                {
                                        tab.setText("评论");
                                }
                        }).attach();

        }

        /**
         * 检测是否有下载地址, 有的话绑定动作, 没有的话隐藏
         */
        private void initDownButton()
        {
                //检测 是否没有下载链接
                if (!GeneralUtils.listIsNullOrHasEmptyElement(post.getMetadata().getDown()) || !GeneralUtils.listIsNullOrHasEmptyElement(post.getMetadata().getDown2()))
                {
                        //绑定点击监听器
                        postDownloadButton.setOnClickListener(v -> {
                                //启动下载
                                DownloadFragment fragment = DownloadFragment.startAction();
                                fragment.show(getSupportFragmentManager(), fragment.getClass().toString());
                        });

                }
                else
                {
                        //没有任何一个下载链接
                        //隐藏下载按钮
                        postDownloadButton.setVisibility(View.GONE);
                }

        }

        /**
         * 根据appbar高度来更改标题栏图标的颜色
         */
        public void changeHomeIconColorListener()
        {
                //根据折叠状态更改标题栏图标颜色
                appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

                        if ((postCollapsingToolbarLayout.getHeight() + verticalOffset) < (1.15 * ViewCompat.getMinimumHeight(postCollapsingToolbarLayout)))
                        {
                                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.defaultTextColor), PorterDuff.Mode.SRC_ATOP);
                        }
                        else
                        {
                                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                        }
                });
        }



        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();

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



        public Post getPost()
        {
                return post;
        }

        public PostViewPagerAdapter getPostViewPagerAdapter()
        {
                return postViewPagerAdapter;
        }


        /**
         * 启动分享窗口
         */
        public void startSharingWindowsFragment(){
                //启动分享窗口
                sharingWindowsFragment = SharingFragment.startAction();
                sharingWindowsFragment.show(getSupportFragmentManager(), sharingWindowsFragment.getClass().toString());
        }

        public PostMainFragment getPostMainFragment()
        {
                return postViewPagerAdapter.getPostMainFragment();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {

                super.onActivityResult(requestCode, resultCode, data);
                //回调子碎片的相同方法
                sharingWindowsFragment.onActivityResult(requestCode, resultCode, data);
        }


        /**
         * 静态 启动本活动的方法
         *
         * @param context
         * @param post
         */
        public static void startAction(Context context, Post post)
        {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra(INTENT_POST, post);
                context.startActivity(intent);
        }

}
