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
                /*如果是连 斜杠//都没有 , 有bug 会导致b站链接无法正常解析
                else if (Character.isLetter(firstElement) && Character.toLowerCase(firstElement) != 'h')
                {
                        new_url = "https://" + url;
                }*/
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
         * 创建隐式intent 启动第三方应用
         *
         * @param context
         * @param url          主要地址
         * @param SecondaryUrl 备用地址
         */
        public static void startWebViewIntent(Context context, String url, String SecondaryUrl)
        {
                //检测url头部是否正常
                url = HttpUtils.checkAndAddHttpsProtocol(url);

                //创建intent
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                //设置主要地址
                intent.setData(Uri.parse(url));

                //如果主要地址 不能被正常解析 (没有安装第三方应用), 而第二个地址不是null 而且不是空
                if (intent.resolveActivity(context.getPackageManager()) == null && SecondaryUrl != null && !SecondaryUrl.isEmpty())
                {
                        //使用备用地址
                        intent.setData(Uri.parse(SecondaryUrl));
                }

                //第二次检查, 确保只有在能被解析的情况下 才尝试启动
                if (intent.resolveActivity(context.getPackageManager()) != null)
                {
                        //启动
                        context.startActivity(intent);
                }
                //否则 消息框提示
                else
                {
                        ToastUtils.shortToast("无法找到相关联的应用");
                }


        }



        /**
         * 第三方解析html方法, 支持给链接和图片 设置动作监听
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
                                return ContextCompat.getDrawable(context, R.drawable.error_black);
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


        /**
         * 默认解析html方法, 给链接添加点击事件监听, 图片没有监听
         * @param context
         * @param htmlString
         * @param textView
         */
        public static void parseHtmlDefault(final Context context, String htmlString, TextView textView){
                HttpUtils.parseHtml(context, htmlString, textView, new OnTagClickListener()
                {
                        //设置 点击图片tag的动作
                        @Override
                        public void onImageClick(Context context, List<String> imagesSrc, int position)
                        {
                        }

                        //设置点击链接tag的动作
                        @Override
                        public void onLinkClick(Context context, String url)
                        {
                                startWebViewIntent(context, url, null);
                        }
                });
        }


}
