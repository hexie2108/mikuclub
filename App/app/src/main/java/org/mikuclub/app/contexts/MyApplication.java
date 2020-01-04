package org.mikuclub.app.contexts;

import android.app.Application;
import android.content.Context;

import org.mikuclub.app.utils.http.RequestQueue;

/**
 * 储存和获取全局context
 * storage the global context of application
 */
public class MyApplication extends Application
{
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
