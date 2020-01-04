package org.mikuclub.app.utils.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

/**
 * 网络请求队列 统一管理器
 */

public class RequestQueue
{

        private static RequestQueue instance;
        private com.android.volley.RequestQueue mRequestQueue;

        /**
         * singleton mode
         * @param context application context
         * @return
         */
        public static synchronized RequestQueue getInstance(Context context)
        {
                if (instance == null)
                {
                        synchronized (RequestQueue.class)
                        {
                                if (instance == null)
                                {
                                        instance = new RequestQueue(context);
                                }
                        }

                }
                return instance;
        }

        /**
         * 构建函数
         * @param context
         */
        private RequestQueue(Context context)
        {
                //生成请求队列
                mRequestQueue = Volley.newRequestQueue(context);
        }

        /**
         * 获取请求队列
         */
        public com.android.volley.RequestQueue getRequestQueue()
        {
                return this.mRequestQueue;
        }

        /**
         * 插入新请求到队列
         */
        public <T> void  addRequestQueue(Request<T> request)
        {
                getRequestQueue().add(request);
        }

        /**
         * 批量删除TAG对应的请求
         * @param tag
         */
        public void cancelRequest(Object tag)
        {
                getRequestQueue().cancelAll(tag);
        }
}
