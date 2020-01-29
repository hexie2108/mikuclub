package org.mikuclub.app.utils.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.mikuclub.app.utils.HttpUtils;

import mikuclub.app.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * GLIDE远程图片加载类
 * GLIDE Remote image loader
 */
public class GlideImageUtils
{

        /**
         * 默认加载方式
         * 16x9 灰色占位图
         * @param context
         * @param imageView
         * @param url
         */
        public static void get(Context context, ImageView imageView, String url)
        {
                //确保URL格式正确
                url = HttpUtils.checkAndAddHttpsProtocol(url);

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_16x9)
                        .error(R.drawable.error_grey_16x9)
                        .transition(withCrossFade())

                        .into(imageView);

        }

        /**
         * 加载正方形图片的方式
         * 1x1 灰色占位符
         * @param context
         * @param imageView
         * @param url
         */
        public static void getSquareImg(Context context, ImageView imageView, String url)
        {
                //确保URL格式正确
                url = HttpUtils.checkAndAddHttpsProtocol(url);

                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.loop_grey_1x1)
                        .error(R.drawable.error_grey_1x1)
                        .transition(withCrossFade())
                        .into(imageView);

        }

        /**
         * 预先加载缩微图的的方式
         * 16x9 微缩图灰色占位图 + 缩微图
         * @param context
         * @param imageView
         * @param url
         * @param thumbnailUrl
         */
        public static void getWithThumbnail(Context context, ImageView imageView, String url, String thumbnailUrl)
        {
                //确保URL格式正确
                url = HttpUtils.checkAndAddHttpsProtocol(url);
                thumbnailUrl = HttpUtils.checkAndAddHttpsProtocol(thumbnailUrl);

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

        /**
         * 在图片查看器里加载图片的方式
         * 1x1 微缩图灰色占位图 + 缩微图
         * @param context
         * @param imageView
         * @param url
         * @param thumbnailUrl
         */
        public static void getForZoomImageView(Context context, ImageView imageView, String url, String thumbnailUrl)
        {
                //确保URL格式正确
                url = HttpUtils.checkAndAddHttpsProtocol(url);
                thumbnailUrl = HttpUtils.checkAndAddHttpsProtocol(thumbnailUrl);

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
