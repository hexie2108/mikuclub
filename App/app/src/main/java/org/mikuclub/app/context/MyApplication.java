package org.mikuclub.app.context;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.mikuclub.app.utils.http.RequestQueue;

/**
 * 自定义 application类
 * 支持储存和获取全局上下文context
 * 和 创建volley网络请求队列
 * custom application class
 * support the storage and getting of global context
 * and init the volley http request queue
 */
public class MyApplication extends Application
{
        @SuppressLint("StaticFieldLeak")
        //因为这是个application的context所以不会造成内存泄露
        private static Context context;

        @Override
        public void onCreate()
        {
                super.onCreate();
                context = getApplicationContext();
                //创建网络请求队列
                RequestQueue.getInstance(context);
        }

        public static Context getContext()
        {
                return context;
        }



}
