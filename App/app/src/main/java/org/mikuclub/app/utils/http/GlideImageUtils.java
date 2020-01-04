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
                        .error(R.drawable.error_grey_16x9)
                        .transition(withCrossFade())

                        .into(imageView);

        }

        public static void getSquareImg(Context context, ImageView imageView, String url)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_1x1)
                        .error(R.drawable.error_grey_1x1)
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
                        .error(R.drawable.error_grey_16x9)
                        .transition(withCrossFade())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)

                        .into(imageView);

        }

        /**
         * 设置预先加载缩微图的版本
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
                                        .load(thumbnailUrl).centerInside()
                        )
                        .error(R.drawable.error_grey_16x9)
                        .transition(withCrossFade())
                        //    .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);

        }

        public static void getForZoomImageView(Context context, ImageView imageView, String url, String thumbnailUrl)
        {

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_1x1)
                        .thumbnail(
                                Glide.with(context)
                                        .load(thumbnailUrl).centerInside()
                        )
                        .error(R.drawable.error_grey_1x1)
                        .transition(withCrossFade())

                        .into(imageView);

        }



}
