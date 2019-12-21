package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.utils.http.GetRemoteImage;
import org.mikuclub.app.utils.http.Request;
import org.mikuclub.app.view.AnimatedNetworkImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import mikuclub.app.R;

/**
 * 文章页
 */
public class PostActivity extends AppCompatActivity
{
        public static final int TAG = 4;

        private Post post;

        private CollapsingToolbarLayout postCollapsingToolbarLayout;

        //标题栏幻灯片
        private BannerViewPager sliderViewPager;
        private TextIndicator textIndicator;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                //去除默认标题栏
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().setStatusBarColor(Color.TRANSPARENT);

                setContentView(R.layout.activity_post);

                postCollapsingToolbarLayout = findViewById(R.id.post_collapsing_toolbar_layout);
                Toolbar toolbar = findViewById(R.id.post_toolbar);

                //获取幻灯片组件
                sliderViewPager = findViewById(R.id.post_slider_viewpager);
                textIndicator = findViewById(R.id.post_slider_indicator);

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

                post = (Post) getIntent().getSerializableExtra("post");

                initSliders();

        }

        /**
         * 初始化图片幻灯片
         **/
        private void initSliders()
        {
                //获取图片数量
                int imageNumber = post.getMetadata().getImages_src().size();
                PageBean bean;
                //如果图片大于1, 就用默认配置
                if (imageNumber>1)
                {
                        bean = new PageBean.Builder<String>()
                                .data(post.getMetadata().getImages_src())
                                .indicator(textIndicator)
                                .builder();
                }
                //只有1张图, 关闭幻灯片循环功能
                else
                {
                        bean = new PageBean.Builder<String>()
                                .useCode(true)
                                .cycle(false)
                                .data(post.getMetadata().getImages_src())
                                .indicator(textIndicator)
                                .builder();
                }

                sliderViewPager.setPageListener(bean, R.layout.post_slider_view_item, new PageHelperListener<String>()
                {
                        @Override
                        public void getItemView(View view, final String imageSrc)
                        {
                                //加载图片
                                NetworkImageView imageView = view.findViewById(R.id.item_image);
                                GetRemoteImage.get(imageView, imageSrc);

                                view.setOnClickListener(new View.OnClickListener()
                                {
                                        @Override
                                        public void onClick(View v)
                                        {
                                                //启动单独的图片查看页面
                                                ImageActivity.startAction(PostActivity.this, (ArrayList<String>) post.getMetadata().getImages_src());

                                        }
                                });

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

        /**
         * 静态 启动本活动的方法
         * @param context
         * @param post
         */
        public static void startAction(Context context, Post post)
        {

                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
        }


}
