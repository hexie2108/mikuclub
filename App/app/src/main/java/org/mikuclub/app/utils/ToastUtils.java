package org.mikuclub.app.utils;

import android.widget.Toast;

import org.mikuclub.app.context.MyApplication;

/**
 * 自定义Toast信息 实用类
 * custom toast print utility class
 */

public class ToastUtils
{
        /**
         * 输出信息窗口 显示时间短
         * @param message
         */
        public static void shortToast(String message)
        {
                Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT).show();
        }

        /**
         * 输出信息窗口 显示时间长
         * @param message
         */
        public static void longToast(String message)
        {
                Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG).show();
        }

}
