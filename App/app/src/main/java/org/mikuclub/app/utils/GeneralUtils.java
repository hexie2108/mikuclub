package org.mikuclub.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.configs.GlobalConfig;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 通用数据处理函数集
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
         * 计算 下个请求应该设置的页数
         *
         * @param offset 当前已拥有的数据数量
         * @return numberPerPage 每页的数据数量
         */
        public static int getNextPageNumber(int offset, int numberPerPage)
        {
                //计算页面数字, 页数是从1开始所以要+1
                int page = offset / numberPerPage + 1;
                int currentModulo = offset % numberPerPage;

                //如果有取余, 说明当前页数还需要+1
                if (currentModulo > 0)
                {
                        page++;
                }
                return page;

        }

        /**
         * 检测列表是否为空
         * 如果不是空的, 而且是 string类型的列表, 继续检测里面的第一个元素是否为空字符串
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
                        else if (element instanceof Integer)
                        {
                                //如果是0 返回 true
                                output = (((int)element) == 0 ? true : false);
                        }
                        //不是字符串则返回 false
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
         * 创建隐式intent 启动第三方应用
         * @param context
         * @param url 主要地址
         * @param SecondaryUrl 备用地址
         */
        public static void startWebViewIntent(Context context, String url, String SecondaryUrl)
        {

                //创建intent
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                //设置主要地址
                intent.setData(Uri.parse(url));

                //如果主要地址 不能被正常解析 (没有安装第三方应用)
                if (intent.resolveActivity(context.getPackageManager()) == null)
                {
                        //使用备用地址
                        intent.setData(Uri.parse(SecondaryUrl));
                }
                //启动
                context.startActivity(intent);


        }


        /**
         * 修正乱码字符串, 忽视错误
         * @param text
         * @return
         */
        public static String fixStringEncoding(String text){
                String newText = "";
                try{
                        newText = new String(text.getBytes("ISO-8859-1"), "UTF-8");
                }
                catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                }

                return newText;


        }


}
