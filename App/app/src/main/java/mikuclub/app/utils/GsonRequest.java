package mikuclub.app.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T>
{

        private Response.Listener<T> mListener;
        private Map<String, String> mParams;
        private Gson mGson;
        private Class<T> mClass;

        /**
         * @param url
         * @param clazz
         * @param listener
         * @param errorListener
         */
        public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                           Response.ErrorListener errorListener)
        {
                this(Method.GET, url, clazz, listener, errorListener);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
                if (mParams != null)
                {
                        return mParams;
                }
                return super.getParams();
        }

        public GsonRequest(int method, Map<String, String> params, String url, Class<T> clazz, Response.Listener listener, Response.ErrorListener errorListener)
        {
                this(Method.POST, url, clazz, listener, errorListener);
                mParams = params;
        }


        /**
         * @param method
         * @param url
         * @param clazz
         * @param listener
         * @param errorListener
         */
        public GsonRequest(int method, String url, Class<T> clazz, Response.Listener listener, Response.ErrorListener errorListener)
        {
                super(method, url, errorListener);

                this.mListener = listener;
                this.mGson = new Gson();
                this.mClass = clazz;
                setTag(listener);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response)
        {
                try
                {
                        String jsonString = new String(response.data, "utf-8");

                        return Response.success(mGson.fromJson(jsonString, mClass), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e)
                {
                        e.printStackTrace();
                }
                return null;
        }

        @Override
        protected void deliverResponse(T response)
        {
                mListener.onResponse(response);
        }
}
