package org.mikuclub.app.contexts;

import android.app.Application;
import android.content.Context;

import org.mikuclub.app.utils.httpUtils.HttpRequestQueue;

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
                //salvare context in variabile statico
                context = getApplicationContext();
                // inizializzare coda di richiesta HTTP
                HttpRequestQueue.getInstance(context);

        }

        public static Context getContext()
        {
                return context;
        }
}
