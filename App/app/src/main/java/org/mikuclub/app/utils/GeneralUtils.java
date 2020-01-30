package org.mikuclub.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.text.StringEscapeUtils;
import org.mikuclub.app.config.GlobalConfig;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 通用实用方法集
 * Common utility class
 */
public class GeneralUtils
{
        /**
         * 把DP转换成PX
         *
         * @param dp
         * @param context
         * @return
         */
        public static float convertDpToPixel(float dp, Context context)
        {
                Resources resources = context.getResources();
                DisplayMetrics metrics = resources.getDisplayMetrics();
                float px = dp * (metrics.densityDpi / 160f);
                return px;
        }

        /**
         * 设置布局的高度占比
         *
         * @param context
         * @param view
         * @param percentage 屏幕高度百分比
         */
        public static void setMaxHeightOfLayout(Context context, View view, float percentage)
        {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                int height = (int) (context.getResources().getDisplayMetrics().heightPixels * percentage);//屏幕高的60%
                layoutParams.height = height;
                view.setLayoutParams(layoutParams);
        }


        /**
         * 检测列表是否为空 或者 列表包含的第一个元素是空字符串或者0
         *
         * @param list
         * @return 是否是空
         */
        public static boolean listIsNullOrHasEmptyElement(List list)
        {
                boolean output = true;
                //如果不是空
                if (list != null && !list.isEmpty())
                {
                        //获取第一个元素
                        Object element = list.get(0);
                        //如果是字符串
                        if (element instanceof String)
                        {
                                //检测是否是空字符串
                                output = ((String) element).trim().isEmpty();
                        }
                        //如果是整数
                        else if (element instanceof Integer)
                        {
                                //如果是0 返回 true
                                output = (((int) element) == 0 ? true : false);
                        }
                        //其他情况返回否
                        else
                        {
                                output = false;
                        }

                }

                return output;
        }


        /**
         * 根据图片地址 获取对应的微缩图地址
         *
         * @param imageSrc
         * @return
         */
        public static String getThumbnailSrcByImageSrc(String imageSrc)
        {
                StringBuilder sb = new StringBuilder(imageSrc);
                //在最后后点号位置 插入 "-500x280" 来获取对应的缩微图
                sb.insert(sb.lastIndexOf("."), GlobalConfig.THUMBNAIL_SIZE);
                return sb.toString();
        }


        /**
         * 按照特定格式 把日期转换输出成字符串
         *
         * @param date
         * @return 字符串格式的日期
         */
        public static String DateToString(Date date)
        {
                String dateString = new SimpleDateFormat(GlobalConfig.DISPLAY_DATE_FORMAT).format(date);
                return dateString;
        }


        /**
         * 恢复 文本内 被html转义的特殊符号
         *
         * @param text
         * @return
         */
        public static String unescapeHtml(String text)
        {
                return StringEscapeUtils.unescapeHtml4(text);
        }


        /**
         * 修正乱码字符串, 忽视错误
         *
         * @param text
         * @return
         */
        public static String fixStringEncoding(String text)
        {
                String newText = "";
                try
                {
                        newText = new String(text.getBytes("ISO-8859-1"), "UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                        e.printStackTrace();
                }

                return newText;
        }


}
