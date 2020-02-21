package org.mikuclub.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.text.StringEscapeUtils;
import org.mikuclub.app.config.GlobalConfig;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用实用方法集
 * Common utility class
 */
public class GeneralUtils
{
        // 中文字符的正则
        private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";

        /**
         * 全角字符转半角
         * @param text input string
         * @return the converted String
         */
        public static String fullSymbolToHalf(String text)
        {
                String output = text;
                if (!text.isEmpty())
                {
                        char[] charArray = text.toCharArray();
                        for (int i = 0; i < charArray.length; i++)
                        {
                                if (charArray[i] == 12288)
                                {
                                        charArray[i] = ' ';
                                }
                                else if (charArray[i] >= ' ' &&
                                        charArray[i] <= 65374)
                                {
                                        charArray[i] = (char) (charArray[i] - 65248);
                                }
                        }
                        output = new String(charArray);
                }

                return output;
        }



        /**
         * 去除字符串里的中文字符
         */
        public static String replaceChineseCharacters(String text)
        {
                String output = text;
                if (!text.isEmpty())
                {
                        // 加载正则表达式
                        Pattern pat = Pattern.compile(REGEX_CHINESE);
                        Matcher mat = pat.matcher(text);
                        output = mat.replaceAll("");
                }
                return output;

        }


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
                                output = ((int) element == 0);
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
         * 检测email字符串是否是个有效的邮箱地址
         *
         * @param emailText
         * @return
         */
        public final static boolean isValidEmail(CharSequence emailText)
        {
                Boolean isValid = false;
                if (emailText != null)
                {
                        isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
                }

                return isValid;
        }


        /**
         * 检测URL字符串是否是个有效的链接地址
         *
         * @param urlText
         * @return
         */
        public final static boolean isValidUrl(String urlText)
        {
                Boolean isValid = false;
                if (Patterns.WEB_URL.matcher(urlText).matches())
                {
                        isValid = true;
                }

                return isValid;
        }




        /**
         * 修正乱码字符串, 忽视错误
         *
         * @param text
         * @return
         */
        public static String fixStringEncoding(String text)
        {
                return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }


}
