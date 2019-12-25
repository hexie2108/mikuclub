package org.mikuclub.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

public class HttpUtils
{
        //html图片的最大宽度, 初试为0, 在第一次运行中计算
        private static int htmlImageMaxWidth = 0;

        /**
         * 检测地址是否包含 https协议名, 没有的话就加上
         * @param url
         * @return
         */
        public static String checkAndAddHttpsProtocol(String url){

                String new_url = url;
                if (url.charAt(0) == '/'){
                        new_url = "https:"+url;
                }
                return  new_url;
        }

        /**
         *
         * @param context
         * @param htmlString
         * @param textView
         * @param onTagClickListener 超链接和图片tag点击事件监听
         */
        public static void parseHtml(final Context context, String htmlString, TextView textView, final OnTagClickListener onTagClickListener){

                if(htmlImageMaxWidth==0){
                        //要扣除左右两边的填充边距
                        int paddingDistance = (int) GeneralUtils.convertDpToPixel( context.getResources().getDimension(R.dimen.normal) , context);
                        //扣除屏幕的 px宽度
                        htmlImageMaxWidth = ScreenUtils.getScreenWidth(context) - paddingDistance;
                }

                HtmlText.from(htmlString).setImageLoader(new HtmlImageLoader()
                {
                        @Override
                        public void loadImage(String url, final HtmlImageLoader.Callback callback)
                        {
                                Glide.with(context)
                                        .asBitmap()
                                        .load(url)
                                        .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(Bitmap resource,
                                                                            Transition<? super Bitmap> transition) {
                                                        callback.onLoadComplete(resource);
                                                }
                                                @Override
                                                public void onLoadFailed(Drawable errorDrawable) {
                                                        callback.onLoadFailed();
                                                }
                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) { }

                                        });
                        }

                        @Override
                        public Drawable getDefaultDrawable()
                        {
                                return ContextCompat.getDrawable(context, R.drawable.loop_grey_16x9);
                        }

                        @Override
                        public Drawable getErrorDrawable()
                        {
                                return ContextCompat.getDrawable(context, R.drawable.baseline_error_grey_16x9);
                        }

                        @Override
                        public int getMaxWidth()
                        {
                                return htmlImageMaxWidth;
                        }

                        @Override
                        public boolean fitWidth()
                        {
                                return false;
                        }
                }).setOnTagClickListener(onTagClickListener).into(textView);

        }


}
