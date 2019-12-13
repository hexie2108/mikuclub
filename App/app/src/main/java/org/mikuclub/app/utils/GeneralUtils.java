package org.mikuclub.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
}
