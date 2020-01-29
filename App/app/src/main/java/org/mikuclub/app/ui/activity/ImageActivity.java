package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.ortiz.touchview.TouchImageView;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

public class ImageActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final String INTENT_IMAGES_SRC = "images_src";
        public static final String INTENT_POSITION = "position";

        /* 变量 local variable */
        //当前页面需要的图片URL地址
        List<String> imagesSrc;
        int position;

        /* 组件 views */
        //幻灯片组件
        private BannerViewPager sliderViewPager;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                //让系统状态栏透明
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                //
                setContentView(R.layout.activity_image);

                //绑定组件
                sliderViewPager = findViewById(R.id.image_slider_viewpager);

                //获取图片地址的列表
                imagesSrc = getIntent().getStringArrayListExtra(INTENT_IMAGES_SRC);
                position = getIntent().getIntExtra(INTENT_POSITION, 0);

                initSliders();
        }


        /**
         * 初始化图片查看器 (幻灯片)
         * Init the picture viewer (slideshow)
         **/
        private void initSliders()
        {

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

                sliderViewPager.setCurrentPosition(position);
                sliderViewPager.setPageListener(R.layout.slider_view_item_image, imagesSrc, new PageHelperListener<String>()
                {
                        @Override
                        public void bindView(View view, String imageSrc, int position)
                        {
                                //加载图片
                                TouchImageView imageView = view.findViewById(R.id.item_image);
                                //设置最大放大等级
                                imageView.setMaxZoom(2);
                                GlideImageUtils.getForZoomImageView(ImageActivity.this, imageView, imageSrc, GeneralUtils.getThumbnailSrcByImageSrc(imageSrc));
                        }
                });

        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param imagesSrc
         */
        public static void startAction(Context context, List<String> imagesSrc, int position)
        {
                ArrayList<String> list = new ArrayList<>(imagesSrc);

                Intent intent = new Intent(context, ImageActivity.class);
                intent.putStringArrayListExtra(INTENT_IMAGES_SRC, list);
                intent.putExtra(INTENT_POSITION, position);
                context.startActivity(intent);
        }


}
