package org.mikuclub.app.utils;

import android.util.Log;

//控制应用整体的DEBUG日志输出
//manage the print of debug info of application

public class LogUtils
{
        public static final String VERBOSE_TAG = "verbose_tag";
        public static final String DEBUG_TAG = "debug_tag";
        public static final String INFO_TAG = "info_tag";
        public static final String WARN_TAG = "warn_tag";
        public static final String ERROR_TAG = "error_tag";


        public static final int VERBOSE = 1;
        public static final int DEBUG = 2;
        public static final int INFO = 3;
        public static final int WARN = 4;
        public static final int ERROR = 5;
        public static final int NOTHING = 6;

        //改变这个变量来控制日志输出等级
        //change this variable to manage the level of info to print
        public static int log_level = VERBOSE;

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
