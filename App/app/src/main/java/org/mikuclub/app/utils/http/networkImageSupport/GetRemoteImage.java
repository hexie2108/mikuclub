package org.mikuclub.app.utils.http.networkImageSupport;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import mikuclub.app.R;

public class GetRemoteImage
{
        /**
         * 获取网络图片 默认方式 (灰色透明加载图)
         *
         * @param networkImageView
         * @param url
         */
        public static void get(NetworkImageView networkImageView, String url)
        {
                //获取图片加载器 (使用自定义缓存策略, 首先检查本地缓存, 找不到才会通过网络请求)
                ImageLoader imageLoader = ImageFileLoader.getImageLoader();

                //设置加载时的图案
                networkImageView.setDefaultImageResId(R.drawable.loop_grey_16x9);
                //设置加载错误时的图片
                networkImageView.setErrorImageResId(R.drawable.error_white);
                //请求图片
                networkImageView.setImageUrl(url, imageLoader);

        }

        /**
         * 获取网络图片 ZoomNetworkImageView版本 (白灰色透明加载图)
         *
         * @param networkImageView
         * @param url
         */
        public static void get(ZoomNetworkImageView networkImageView, String url)
        {
                //获取图片加载器 (使用自定义缓存策略, 首先检查本地缓存, 找不到才会通过网络请求)
                ImageLoader imageLoader = ImageFileLoader.getImageLoader();

                //设置加载时的图案
                networkImageView.setDefaultImageResId(R.drawable.loop_white);
                //设置加载错误时的图片
                networkImageView.setErrorImageResId(R.drawable.error_white);
                //请求图片
                networkImageView.setImageUrl(url, imageLoader);

        }

}
