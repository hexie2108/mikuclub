package org.mikuclub.app.utils;

import android.util.Log;


/**
 * 自定义日志输出类
 * 通过log level 可以控制应用整体的DEBUG日志输出
 * custom log print function
 */
public class LogUtils
{
        private static final String VERBOSE_TAG = "verbose_tag";
        private static final String DEBUG_TAG = "debug_tag";
        private static final String INFO_TAG = "info_tag";
        private static final String WARN_TAG = "warn_tag";
        private static final String ERROR_TAG = "error_tag";


        private static final int VERBOSE = 1;
        private static final int DEBUG = 2;
        private static final int INFO = 3;
        private static final int WARN = 4;
        private static final int ERROR = 5;
        private static final int NOTHING = 6;

        //改变这个变量来控制日志输出等级
        //change this variable to manage the level of info to print
        private static int log_level = VERBOSE;

        public static void v(String msg)
        {
                if (log_level <= VERBOSE)
                {
                        Log.v(VERBOSE_TAG, msg);
                }
        }

        public static void d(String msg)
        {
                if (log_level <= DEBUG)
                {
                        Log.d(DEBUG_TAG, msg);
                }
        }

        public static void i(String msg){
                if(log_level <= INFO ){
                        Log.i(INFO_TAG, msg);
                }
        }

        public static void w(String msg){
                if(log_level <= WARN ){
                        Log.w(WARN_TAG, msg);
                }
        }

        public static void e(String msg){
                if(log_level <= ERROR ){
                        Log.e(ERROR_TAG, msg);
                }
        }




}
