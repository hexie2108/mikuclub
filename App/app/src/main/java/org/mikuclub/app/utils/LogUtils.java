package org.mikuclub.app.utils;

import android.util.Log;

//控制应用整体的DEBUG日志输出
//manage the print of debug info of application

public class LogUtils
{


        public static final int VERBOSE = 1;
        public static final int DEBUG = 2;
        public static final int INFO = 3;
        public static final int WARN = 4;
        public static final int ERROR = 5;
        public static final int NOTHING = 6;

        //改变这个变量来控制日志输出等级
        //change this variable to manage the level of info to print
        public static int log_level = VERBOSE;

        public static void v(String tag, String msg)
        {
                if (log_level <= VERBOSE)
                {
                        Log.v(tag, msg);
                }
        }

        public static void d(String tag, String msg)
        {
                if (log_level <= DEBUG)
                {
                        Log.d(tag, msg);
                }
        }

        public static void i(String tag, String msg){
                if(log_level <= INFO ){
                        Log.i(tag, msg);
                }
        }

        public static void w(String tag, String msg){
                if(log_level <= WARN ){
                        Log.w(tag, msg);
                }
        }

        public static void e(String tag, String msg){
                if(log_level <= ERROR ){
                        Log.e(tag, msg);
                }
        }




}
