package org.mikuclub.app.ui.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.ortiz.touchview.TouchImageView;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.type.BannerTransType;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
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
        private int position;
        private String imageUrlToSave;

        private long downloadID;
        //创建广播接收者
        private BroadcastReceiver onDownloadComplete = new BroadcastReceiver()
        {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                        //Fetching the download id received with the broadcast
                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        //Checking if the received broadcast is for our enqueued download by matching download id
                        if (downloadID == id)
                        {
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.image_download_success));
                        }
                }
        };


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

                //注册广播接收者, 下载完成后触发
                registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


                //绑定组件
                sliderViewPager = findViewById(R.id.image_slider_viewpager);

                //获取原图地址列表
                imagesFullSrc = getIntent().getStringArrayListExtra(INTENT_IMAGES_FULL_SRC);
                //获取微缩图地址列表
                imagesThumbnailSrc = getIntent().getStringArrayListExtra(INTENT_IMAGES_THUMBNAIL_SRC);
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

                sliderViewPager.setCurrentPosition(position);

                sliderViewPager.addPageBean(bean);

                sliderViewPager.setPageListener(R.layout.slider_view_item_image, imagesFullSrc, new PageHelperListener<String>()
                {
                        @Override
                        public void bindView(View view, String imageSrc, int position)
                        {
                                //加载图片
                                TouchImageView imageView = view.findViewById(R.id.item_image);
                                MaterialButton saveToPhoneButton = view.findViewById(R.id.save_to_phone);
                                //绑定点击动作
                                saveToPhoneButton.setOnClickListener(v -> {

                                        startDownload(imageSrc);

                                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.image_download_start));
                                        //注销按钮
                                        saveToPhoneButton.setEnabled(false);
                                });

                                //设置最大放大等级
                                imageView.setMaxZoom(2);
                                String thumbnailSrc = null;
                                //确保有微缩图地址可以获取
                                if (imagesThumbnailSrc != null && imagesThumbnailSrc.size() > position)
                                {
                                        thumbnailSrc = imagesThumbnailSrc.get(position);
                                }

                                //更新可以通过按钮保存的图片地址
                                imageUrlToSave = imageSrc;

                                GlideImageUtils.getForZoomImageView(ImageActivity.this, imageView, imageSrc, thumbnailSrc);
                        }

                });
        }

        /**
         * 启动下载图片
         * @param url
         */
        private void startDownload(String url)
        {

                String fileName = System.currentTimeMillis() / 1000 + ".jpg";
                //Uri fileUri = FileUtils.createImageFileUri(ImageActivity.this);
                //LogUtils.e("初音"+fileUri.toString());
                /*
                Create a DownloadManager.Request with all the information necessary to start the download
                 */
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                        .setTitle(ResourcesUtils.getString(R.string.app_name)+ResourcesUtils.getString(R.string.image_download))// Title of the Download Notification
                        .setDescription(ResourcesUtils.getString(R.string.app_name)+ResourcesUtils.getString(R.string.image_download))// Description of the Download Notification
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)// Visibility of the download Notification
                        //.setDestinationUri(fileUri)// Uri of the destination file
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/mikuclub/" + fileName);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                        request.setRequiresCharging(false);// Set if charging is required to begin the download
                }

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if (downloadManager != null)
                {
                        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
                }
        }


        @Override
        public void onDestroy()
        {
                super.onDestroy();
                //注销广播接收者
                unregisterReceiver(onDownloadComplete);
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param imagesFullSrc      原始图片地址列表
         * @param imagesThumbnailSrc 微缩图地址列表
         */
        public static void startAction(Context context, List<String> imagesFullSrc, List<String> imagesThumbnailSrc, int position)
        {
                Intent intent = new Intent(context, ImageActivity.class);

                ArrayList<String> imagesFullSrcArrayList = new ArrayList<>(imagesFullSrc);
                intent.putStringArrayListExtra(INTENT_IMAGES_FULL_SRC, imagesFullSrcArrayList);

                //如果微缩图列表有提供
                if (imagesThumbnailSrc != null)
                {
                        ArrayList<String> imagesThumbnailSrcArrayList = new ArrayList<>(imagesThumbnailSrc);
                        intent.putStringArrayListExtra(INTENT_IMAGES_THUMBNAIL_SRC, imagesThumbnailSrcArrayList);
                }

                intent.putExtra(INTENT_POSITION, position);
                context.startActivity(intent);
        }

}