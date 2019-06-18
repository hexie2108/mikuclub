package org.mikuclub.app.contexts;

import android.app.Application;
import android.content.Context;

import org.mikuclub.app.utils.httpUtils.HttpRequestQueue;

/**
 *  classe applicazione customizzato, che salva Application context in modo statico durante fase di OnCreate
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
