package org.mikuclub.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.configs.GlobalConfig;

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
         * @param context
         * @param view
         * @param percentage 屏幕高度百分比
         */
        public static void setMaxHeightOfLayout(Context context, View view, float percentage){
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                int height = (int) (context.getResources().getDisplayMetrics().heightPixels * percentage);//屏幕高的60%
                layoutParams.height = height;
                view.setLayoutParams(layoutParams);
        }


        /**
         * 计算 下个请求应该设置的页数
         * @param offset 当前已拥有的数据数量
         * @return numberPerPage 每页的数据数量
         */
        public static int getNextPageNumber(int offset, int numberPerPage){
                //计算页面数字, 页数是从1开始所以要+1
                int page = offset / numberPerPage+1;
                int currentModulo = offset % numberPerPage;

                //如果有取余, 说明当前页数还需要+1
                if(currentModulo > 0){
                        page++;
                }
                return page;

        }




}
