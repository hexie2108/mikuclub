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

import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

public class ImageActivity extends AppCompatActivity
{

        /* 静态变量 Static variable */
        public static final String INTENT_IMAGES_FULL_SRC = "images_full_src";
        public static final String INTENT_IMAGES_THUMBNAIL_SRC = "images_thumbnail_src";
        public static final String INTENT_POSITION = "position";

        /* 变量 local variable */
        //当前页面需要的图片URL地址
        private List<String> imagesFullSrc;
        private List<String> imagesThumbnailSrc;
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

                //获取原图地址列表
                imagesFullSrc = getIntent().getStringArrayListExtra(INTENT_IMAGES_FULL_SRC);
                //获取微缩图地址列表
                imagesThumbnailSrc= getIntent().getStringArrayListExtra(INTENT_IMAGES_THUMBNAIL_SRC);
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
                if (imagesFullSrc.size() == 1)
                {
                        bean.isAutoCycle = false;
                        bean.isAutoLoop = false;
                        bean.loopMaxCount = 2;
                }
                bean.transFormer = BannerTransType.UNKNOWN;
                sliderViewPager.addPageBean(bean);

                sliderViewPager.setCurrentPosition(position);
                sliderViewPager.setPageListener(R.layout.slider_view_item_image, imagesFullSrc, new PageHelperListener<String>()
                {
                        @Override
                        public void bindView(View view, String imageSrc, int position)
                        {
                                //加载图片
                                TouchImageView imageView = view.findViewById(R.id.item_image);
                                //设置最大放大等级
                                imageView.setMaxZoom(2);
                                String thumbnailSrc=null;
                                //确保有微缩图地址可以获取
                                if(imagesThumbnailSrc != null && imagesThumbnailSrc.size()>position){
                                        thumbnailSrc = imagesThumbnailSrc.get(position);
                                }

                                GlideImageUtils.getForZoomImageView(ImageActivity.this, imageView, imageSrc, thumbnailSrc);
                        }
                });
        }

        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param imagesFullSrc  原始图片地址列表
         * @param imagesThumbnailSrc 微缩图地址列表
         */
        public static void startAction(Context context, List<String> imagesFullSrc, List<String> imagesThumbnailSrc, int position)
        {
                Intent intent = new Intent(context, ImageActivity.class);

                ArrayList<String> imagesFullSrcArrayList = new ArrayList<>(imagesFullSrc);
                intent.putStringArrayListExtra(INTENT_IMAGES_FULL_SRC, imagesFullSrcArrayList);

                //如果微缩图列表有提供
                if(imagesThumbnailSrc != null){
                        ArrayList<String>   imagesThumbnailSrcArrayList = new ArrayList<>(imagesThumbnailSrc);
                        intent.putStringArrayListExtra(INTENT_IMAGES_THUMBNAIL_SRC, imagesThumbnailSrcArrayList);
                }

                intent.putExtra(INTENT_POSITION, position);
                context.startActivity(intent);
        }

}