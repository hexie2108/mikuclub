package mikuclub.app.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton
{
        private static VolleySingleton ourInstance;
        private RequestQueue mRequestQuene;

        public static synchronized VolleySingleton getInstance(Context context)
        {
                if (ourInstance == null)
                {
                        ourInstance = new VolleySingleton(context);
                }
                return ourInstance;
        }

        private VolleySingleton(Context context)
        {
                mRequestQuene = Volley.newRequestQueue(context);
        }

        public RequestQueue getRequestQuene()
        {
                return this.mRequestQuene;
        }

        public <T> void  addRequestQueue(Request<T> request)
        {
                getRequestQuene().add(request);
        }

        public void cancelRequest(Object object)
        {
                getRequestQuene().cancelAll(object);
        }
}
