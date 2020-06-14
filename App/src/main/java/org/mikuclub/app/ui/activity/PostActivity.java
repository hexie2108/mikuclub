package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.adapter.viewPager.PostViewPagerAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.response.SinglePost;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.fragments.PostMainFragment;
import org.mikuclub.app.ui.fragments.windows.DownloadFragment;
import org.mikuclub.app.ui.fragments.windows.SharingFragment;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ScreenUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

/**
 * 文章页
 * post page
 */
public class PostActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 4;
        public static final String INTENT_POST = "post";
        public static final String INTENT_POST_ID = "pos_id";

        /* 变量 local variable */
        private Post post;
        private int postId;
        //碎片适配器
        private PostViewPagerAdapter postViewPagerAdapter;
        private SharingFragment sharingWindowsFragment;

        private PostDelegate delegate;

        /* 组件 views */
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
        //创建进度条弹窗
        AlertDialog progressDialog;
        AlertDialog confirmDialog;


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
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //隐藏标题
                        actionBar.setDisplayShowTitleEnabled(false);
                }


                postId = getIntent().getIntExtra(INTENT_POST_ID, 0);
                //获取文章数据
                post = (Post) getIntent().getSerializableExtra(INTENT_POST);
                //如果是通过完整post数据方式启动本活动
                if (post != null)
                {
                        //直接初始化
                        setup();
                }
                //准备通过post id 获取 文章
                else
                {
                        //准备通过id请求文章
                        prepareGetPost();
                }

        }

        @Override
        protected void onStart()
        {
                super.onStart();
                //如果没有文章数据 , 但是有文章id
                if (post == null && postId != 0)
                {
                        //发送请求
                        getPostData();
                }
        }

        /**
         * 准备请求文章
         * prepare request to get post
         */
        private void prepareGetPost()
        {
                //创建请求代理扔
                delegate = new PostDelegate(TAG);
                //创建进度条弹窗
                progressDialog = AlertDialogUtils.createProgressDialog(this, true, true);
                //弹窗确认按钮点击事件监听
                DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
                        //重试请求
                        getPostData();
                };
                //弹窗取消按钮点击事件监听
                DialogInterface.OnClickListener negativeClickListener = (dialog, which) -> {
                        //关闭当前页面
                        finish();
                };
                //创建重试弹窗
                confirmDialog = AlertDialogUtils.createConfirmDialog(this, ResourcesUtils.getString(R.string.post_get_by_id_error_message), null, true, true, ResourcesUtils.getString(R.string.retry), positiveClickListener, ResourcesUtils.getString(R.string.cancel), negativeClickListener);

        }


        /**
         * 发送请求
         * send request to get post
         */
        private void getPostData()
        {

                //显示进度条
                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //获取文章数据
                                post = ParserUtils.fromJson(response, SinglePost.class).getBody();
                                //初始化页面
                                setup();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                //弹出确认窗口 允许用户重试
                                confirmDialog.show();
                        }

                        @Override
                        public void onHttpError()
                        {
                                onError(null);
                        }

                        @Override
                        public void onFinally()
                        {
                                //隐藏进度条弹窗
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                //隐藏进度条弹窗
                                progressDialog.dismiss();
                        }
                };
                delegate.getPost(httpCallBack, postId);

        }

        /**
         * 初始化本活动
         * init page
         */
        private void setup()
        {
                initSliders();

                initViewPager();

                initDownButton();

                //根据appBar高度更改标题栏图标颜色
                changeHomeIconColorListener();
        }


        /**
         * 初始化图片幻灯片
         * init slider view
         **/
        private void initSliders()
        {
                //获取图片地址列表
                List<String> imagesSrc = post.getMetadata().getImages_src();
                //重新显示指示器
                textIndicator.setVisibility(View.VISIBLE);

                PageBean bean = new PageBean();
                //如果只有一张图关闭循环
                if (imagesSrc.size() == 1)
                {
                        bean.isAutoCycle = false;
                        bean.isAutoLoop = false;
                        bean.loopMaxCount = 2;
                }
                bean.transFormer = BannerTransType.UNKNOWN;
                sliderViewPager.addPageBean(bean);

                sliderViewPager.addIndicator(textIndicator);
                sliderViewPager.setPageListener(R.layout.slider_view_item_post, imagesSrc, new PageHelperListener<String>()
                {

                        @Override
                        public void bindView(View view, String itemSrc, int position)
                        {

                                //加载图片
                                ImageView imageView = view.findViewById(R.id.item_image);

                                String thumbnailSrc=null;
                                //确保有微缩图
                                if(post.getMetadata().getImages_thumbnail_src().size()>position){
                                        thumbnailSrc = post.getMetadata().getImages_thumbnail_src().get(position);
                                }

                                //加载图片 (先加载缩微图 之后加载原图)
                                GlideImageUtils.getWithThumbnail(PostActivity.this, imageView, itemSrc, thumbnailSrc);

                                view.setOnClickListener(v -> {
                                        //启动单独的图片查看页面
                                        ImageActivity.startAction(PostActivity.this, post.getMetadata().getImages_full_src(), post.getMetadata().getImages_thumbnail_src(), position);
                                });

                        }
                });


        }


        /**
         * 初始化 文章主体fragment
         * init view pager
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
                                        tab.setText(ResourcesUtils.getString(R.string.description));
                                }
                                else if (position == 1)
                                {
                                        tab.setText(ResourcesUtils.getString(R.string.comment));
                                }
                        }).attach();

                //屏蔽点击事件, 防止在菜单栏容器上的点击 激活下层幻灯片图片的事件
                tabsMenuBox.setOnClickListener(v -> {
                });

        }

        /**
         * 检测是否有下载地址, 有的话绑定动作, 没有的话隐藏
         * check if there is download link
         * in positive case, set click listener on download button
         * otherwise hide download button
         */
        private void initDownButton()
        {
                //检测 是否没有下载链接
                if (!GeneralUtils.listIsNullOrHasEmptyElement(post.getMetadata().getDown()) || !GeneralUtils.listIsNullOrHasEmptyElement(post.getMetadata().getDown2()))
                {
                        //显示下载按钮
                        postDownloadButton.setVisibility(View.VISIBLE);
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
         * 设置折叠工具栏 在横屏状态下的 初始高度
         * 根据appbar高度来更改标题栏图标的颜色
         */
        public void changeHomeIconColorListener()
        {
                //如果当前是横屏状态
                if(ScreenUtils.isHorizontal(PostActivity.this)){
                        //改变折叠工具栏的显示高度
                        int height = (int)  (getResources().getDisplayMetrics().heightPixels * GlobalConfig.HEIGHT_PERCENTAGE_OF_COLLAPSING_TOOLBAR_HORIZONTAL); //设置高度为屏幕百分比
                        ScreenUtils.setViewSize(postCollapsingToolbarLayout, 0, height);
                }

                //根据折叠状态更改标题栏图标颜色
                appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                        Drawable backButtonIcon = toolbar.getNavigationIcon();
                        //确保 标题栏返回键的图标不是null, 避免后续操作出现空指针错误
                        if (backButtonIcon != null)
                        {
                                //如果菜单栏被折叠 超过一定高度
                                if ((postCollapsingToolbarLayout.getHeight() + verticalOffset) < (1.15 * ViewCompat.getMinimumHeight(postCollapsingToolbarLayout)))
                                {
                                        backButtonIcon.setColorFilter(getResources().getColor(R.color.defaultTextColor), PorterDuff.Mode.SRC_ATOP);
                                }
                                else
                                {
                                        backButtonIcon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                                }
                        }


                });
        }


        /**
         * 启动分享窗口
         * start the sharing windows
         */
        public void startSharingWindowsFragment()
        {
                //启动分享窗口
                sharingWindowsFragment = SharingFragment.startAction();
                sharingWindowsFragment.show(getSupportFragmentManager(), sharingWindowsFragment.getClass().toString());
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


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode,
                                        @Nullable Intent data)
        {

                super.onActivityResult(requestCode, resultCode, data);
                //如果不是空的
                if (sharingWindowsFragment != null)
                {
                        //回调分享子碎片的相同方法
                        sharingWindowsFragment.onActivityResult(requestCode, resultCode, data);
                }
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         * 提供完整post数据
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

        /**
         * 启动本活动的静态方法
         * static method to start current activity2
         * 只提供post id
         *
         * @param context
         * @param postId
         */
        public static void startAction(Context context, int postId)
        {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra(INTENT_POST_ID, postId);
                context.startActivity(intent);
        }

        public Post getPost()
        {
                return post;
        }

        public PostMainFragment getPostMainFragment()
        {
                return postViewPagerAdapter.getPostMainFragment();
        }

}
