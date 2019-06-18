package org.mikuclub.app.utils.httpUtils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * questa classe gestisce l'unico coda di richiesta HTTP Volley per intero applicazione
 */

public class HttpRequestQueue
{
        private static HttpRequestQueue instance;
        private RequestQueue mRequestQueue;

        /**
         * singleton mode
         * @param context application context
         * @return
         */
        public static synchronized HttpRequestQueue getInstance(Context context)
        {
                if (instance == null)
                {
                        synchronized (HttpRequestQueue .class)
                        {
                                if (instance == null)
                                {
                                        instance = new HttpRequestQueue(context);
                                }
                        }

                }
                return instance;
        }

        /**
         * costruttore
         * @param context
         */
        private HttpRequestQueue(Context context)
        {
                mRequestQueue = Volley.newRequestQueue(context);
        }

        /**
         * get la coda di richiesta
         * @return
         */
        public RequestQueue getRequestQueue()
        {
                return this.mRequestQueue;
        }

        /**
         * inserire la richiesta in coda
         * @param request
         * @param <T>
         */
        public <T> void  addRequestQueue(Request<T> request)
        {
                getRequestQueue().add(request);
        }

        /**
         * cancellare le richieste con determinato TAG
         * @param tag
         */
        public void cancelRequest(Object tag)
        {
                getRequestQueue().cancelAll(tag);
        }
}
