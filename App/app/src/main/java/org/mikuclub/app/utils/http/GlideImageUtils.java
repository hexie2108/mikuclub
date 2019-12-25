package org.mikuclub.app.utils.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import mikuclub.app.R;

public class GlideImageUtils
{

        /**
         * 默认请求方式
         * @param context
         * @param imageView
         * @param url
         */
        public static void get(Context context, ImageView imageView, String url)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_16x9)
                        .error(R.drawable.baseline_error_grey_16x9)
                        .transition(withCrossFade())

                        .into(imageView);

        }

        public static void getSquareImg(Context context, ImageView imageView, String url)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_1x1)
                        .error(R.drawable.baseline_error_grey_1x1)
                        .transition(withCrossFade())
                        .into(imageView);

        }

        /**
         * 关闭缓存的版本
         * @param context
         * @param imageView
         * @param url
         */
        public static void getWithoutCache(Context context, ImageView imageView, String url)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_16x9)
                        .error(R.drawable.baseline_error_grey_16x9)
                        .transition(withCrossFade())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)

                        .into(imageView);

        }

        /**
         * 设置加载缩微图的版本
         *
         * @param context
         * @param imageView
         * @param url
         * @param thumbnailUrl
         */
        public static void getWithThumbnail(Context context, ImageView imageView, String url, String thumbnailUrl)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_16x9)
                        .thumbnail(
                                Glide.with(context)
                                        .load(thumbnailUrl)
                        )
                        .error(R.drawable.baseline_error_grey_16x9)
                        .transition(withCrossFade())
                        //     .skipMemoryCache(true)
                        //    .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);

        }

        public static void getForZoomImageView(Context context, ImageView imageView, String url)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_white_16x9)
                        .error(R.drawable.baseline_error_white_16x9)
                        .transition(withCrossFade())
                        //   .skipMemoryCache(true)
                        //     .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);

        }










        /*deprecated*/

        /**
         * 获取网络图片 默认方式 (灰色透明加载图)
         * @param networkImageView
         * @param url

        public static void get(NetworkImageView networkImageView, String url)
        {
        //获取图片加载器 (使用自定义缓存策略, 首先检查本地缓存, 找不到才会通过网络请求)
        ImageLoader imageLoader = ImageFileLoader.getImageLoader();

        //设置加载时的图案
        networkImageView.setDefaultImageResId(R.drawable.loop_grey_16x9);
        //设置加载错误时的图片
        networkImageView.setErrorImageResId(R.drawable.error_outline);
        //请求图片
        networkImageView.setImageUrl(url, imageLoader);

        }

        /**
         * 获取网络图片 ZoomNetworkImageView版本 (白灰色透明加载图)
         * @param networkImageView
         * @param url

        public static void get(ZoomNetworkImageView networkImageView, String url)
        {
        //获取图片加载器 (使用自定义缓存策略, 首先检查本地缓存, 找不到才会通过网络请求)
        ImageLoader imageLoader = ImageFileLoader.getImageLoader();

        //设置加载时的图案
        networkImageView.setDefaultImageResId(R.drawable.loop_white_16x9);
        //设置加载错误时的图片
        networkImageView.setErrorImageResId(R.drawable.error_outline);
        //请求图片
        networkImageView.setImageUrl(url, imageLoader);

        }
         */
}
