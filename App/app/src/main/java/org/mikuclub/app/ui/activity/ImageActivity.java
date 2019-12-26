package org.mikuclub.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ortiz.touchview.TouchImageView;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.TextIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity
{

        //储存图片URL地址
        List<String> imagesSrc;

        //幻灯片组件
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

                setContentView(R.layout.activity_image);



                //获取幻灯片组件
                sliderViewPager = findViewById(R.id.image_slider_viewpager);


                //获取图片地址的列表
                imagesSrc = getIntent().getStringArrayListExtra("images_src");

                initSliders();
        }


        /**
         * 初始化图片查看器 (幻灯片)
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

                sliderViewPager.setPageListener(bean, R.layout.image_slider_view_item, new PageHelperListener<String>()
                {
                        @Override
                        public void getItemView(View view, final String imageSrc)
                        {
                                //加载图片
                                TouchImageView imageView = view.findViewById(R.id.item_image);
                                //设置最大放大等级
                                //imageView.setMaxZoom(2);
                                GlideImageUtils.getForZoomImageView(ImageActivity.this, imageView, imageSrc);

                        }

                });


        }



        /**
         * 静态 启动本活动的方法
         *
         * @param context
         * @param imagesSrc
         */
        public static void startAction(Context context, ArrayList<String> imagesSrc)
        {

                Intent intent = new Intent(context, ImageActivity.class);
                intent.putStringArrayListExtra("images_src", imagesSrc);

                context.startActivity(intent);
        }

}
