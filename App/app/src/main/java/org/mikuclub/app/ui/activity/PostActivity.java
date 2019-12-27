package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


import com.google.android.material.tabs.TabLayoutMediator;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.adapters.PostFragmentViewPagerAdapter;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import mikuclub.app.R;

/**
 * 文章页
 */
public class PostActivity extends AppCompatActivity
{
        public static final int TAG = 4;

        private Post post;

        private CollapsingToolbarLayout postCollapsingToolbarLayout;

        //分页管理器
        private ViewPager2 postViewPager;
        //分页菜单栏
        private TabLayout postTabsMenuLayout;

        //标题栏幻灯片
        private BannerViewPager sliderViewPager;
        private TextIndicator textIndicator;
        private FloatingActionButton postDownloadButton;

        private List<String> imagesSrc;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                //去除默认标题栏
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().setStatusBarColor(Color.TRANSPARENT);

                setContentView(R.layout.activity_post);

                Toolbar toolbar = findViewById(R.id.post_toolbar);
                postCollapsingToolbarLayout = findViewById(R.id.post_collapsing_toolbar_layout);
                //获取幻灯片组件
                sliderViewPager = findViewById(R.id.post_slider_viewpager);
                textIndicator = findViewById(R.id.post_slider_indicator);
                postViewPager = findViewById(R.id.post_view_pager);
                postTabsMenuLayout = findViewById(R.id.post_tabs_menu);
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
                post = (Post) getIntent().getSerializableExtra("post");
                //获取图片地址列表
                imagesSrc = post.getMetadata().getImages_src();

                initSliders();

                initFragment();


                //检测 下载链接不存在 或者 为空
                if(post.getMetadata().getDown() == null || post.getMetadata().getDown().get(0).trim() ==""){
                        //隐藏下载按钮
                        postDownloadButton.setVisibility(View.GONE);
                }


        }

        /**
         * 初始化图片幻灯片
         **/
        private void initSliders()
        {

                PageBean.Builder builder = new PageBean.Builder<String>();
                //如果只有1张图
                if (imagesSrc.size() == 1)
                {
                        //关闭循环
                        builder = builder
                                .useCode(true)
                                .cycle(false);
                }
                PageBean bean = builder
                        .data(imagesSrc)
                        .indicator(textIndicator)
                        .builder();


                sliderViewPager.setPageListener(bean, R.layout.post_slider_view_item, new PageHelperListener<String>()
                {

                        @Override
                        public void getItemView(View view, final String itemSrc)
                        {
                                //加载图片
                                ImageView imageView = view.findViewById(R.id.item_image);
                                //如果是第一张图
                                if (itemSrc.equals(imagesSrc.get(0)))
                                {
                                        String thumbnailSrc = post.getMetadata().getThumbnail_src().get(0);
                                        GlideImageUtils.getWithThumbnail(PostActivity.this, imageView, itemSrc, thumbnailSrc);
                                }
                                else
                                {
                                        GlideImageUtils.get(PostActivity.this, imageView, itemSrc);
                                }

                                view.setOnClickListener(new View.OnClickListener()
                                {
                                        @Override
                                        public void onClick(View v)
                                        {

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
                                                ImageActivity.startAction(PostActivity.this, newImagesSrc );

                                        }
                                });

                        }
                });


        }


        /**
         * 初始化 文章主体fragment
         */
        private void initFragment(){


                postViewPager.setAdapter(new PostFragmentViewPagerAdapter(this));

                new TabLayoutMediator(postTabsMenuLayout, postViewPager,
                        new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                        if(position==0){
                                                tab.setText("描述");
                                        }
                                        else if(position == 1){
                                                tab.setText("评论");
                                        }
                                }
                        }).attach();

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

        /**
         * 静态 启动本活动的方法
         *
         * @param context
         * @param post
         */
        public static void startAction(Context context, Post post)
        {

                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
        }


        public Post getPost()
        {
                return post;
        }
}
