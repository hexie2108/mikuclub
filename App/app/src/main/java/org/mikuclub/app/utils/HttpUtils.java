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
         * 检测地址是否是没添加 http协议的链接, 没有的话就加上
         *
         * @param url
         * @return
         */
        public static String checkAndAddHttpsProtocol(String url)
        {
                //去除左右空格
                String new_url = url.trim();
                char firstElement = url.charAt(0);
                //如果只是没有添加http头部
                if (firstElement == '/')
                {
                        new_url = "https:" + url;
                }
                //如果是连 斜杠//都没有
                else if (Character.isLetter(firstElement) && Character.toLowerCase(firstElement) != 'h')
                {
                        new_url = "https://" + url;
                }
                return new_url;
        }

        /**
         * 如果字符串是被包含在一个主体html TAG中, 就移除它
         *
         * @param text
         * @param tagStart
         * @param tagEnd
         * @return
         */
        public static String removeHtmlMainTag(String text, String tagStart, String tagEnd)
        {
                text = text.trim();
                //LogUtils.e(text + " " + text.length());
                //如果字符串开头tag匹配
                if (text.indexOf(tagStart) == 0)
                {
                        //获取结尾tag位置
                        int endIndex = text.lastIndexOf(tagEnd);
                        //如果也找到了结尾tag, 而且离结尾不远 才进行剪切操作 (5个字符范围内)
                        if (endIndex != -1 && endIndex > text.length() - tagEnd.length() - 5)
                        {
                                //截取字符串
                                text = text.substring(tagStart.length(), endIndex);
                        }
                        //   LogUtils.e(text + " " + text.length());


                }
                return text;
        }


        /**
         * @param context
         * @param htmlString
         * @param textView
         * @param onTagClickListener 超链接和图片tag点击事件监听
         */
        public static void parseHtml(final Context context, String htmlString, TextView textView, final OnTagClickListener onTagClickListener)
        {

                if (htmlImageMaxWidth == 0)
                {
                        //要扣除左右两边的填充边距
                        //int paddingDistance = (int) GeneralUtils.convertDpToPixel( context.getResources().getDimension(R.dimen.normal) , context);
                        //扣除屏幕的 px宽度
                        //htmlImageMaxWidth = ScreenUtils.getScreenWidth(context) - paddingDistance;

                        htmlImageMaxWidth = ScreenUtils.getScreenWidth(context);
                }

                HtmlText.from(htmlString).setImageLoader(new HtmlImageLoader()
                {
                        @Override
                        public void loadImage(String url, final HtmlImageLoader.Callback callback)
                        {
                                Glide.with(context)
                                        .asBitmap()
                                        .load(url)
                                        .into(new CustomTarget<Bitmap>()
                                        {
                                                @Override
                                                public void onResourceReady(Bitmap resource,
                                                                            Transition<? super Bitmap> transition)
                                                {
                                                        callback.onLoadComplete(resource);
                                                }

                                                @Override
                                                public void onLoadFailed(Drawable errorDrawable)
                                                {
                                                        callback.onLoadFailed();
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder)
                                                {
                                                }

                                        });
                        }

                        @Override
                        public Drawable getDefaultDrawable()
                        {
                                //return ContextCompat.getDrawable(context, R.drawable.loop_grey_16x9);
                                return null;
                        }

                        @Override
                        public Drawable getErrorDrawable()
                        {
                                return ContextCompat.getDrawable(context, R.drawable.baseline_error_black);
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
