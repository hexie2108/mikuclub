package org.mikuclub.app.utils.http.deprecated;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * 废弃
 * la classe request personalizzato per trasformare response in oggetto bean
 *  source   https://www.jianshu.com/p/4e153e925878
 */
public class GsonRequest extends Request
{

        private Response.Listener mListener;
        private Map<String, String> mParams;
        private Map<String, String> mHeaders;
        private Gson mGson;
        private Class beanClass;
        private Type listClassesType;
        private Object responseObject;


        /**
         * costruttore
         * @param method
         * @param url
         * @param beanClass
         * @param listener
         * @param errorListener
         */
        public GsonRequest(int method, String url, Map<String, String> params, Map<String, String> headers, Class beanClass, Type listClassesType, Response.Listener listener, Response.ErrorListener errorListener)
        {
                super(method, url, errorListener);
                this.mParams = params;
                this.mHeaders = headers;
                this.mListener = listener;

                this.beanClass = beanClass;
                this.listClassesType = listClassesType;

        }

        @Override
        protected Response parseNetworkResponse(NetworkResponse response)
        {
                try
                {
                        //get la risposta raw
                        String jsonString = new String(response.data, "utf-8");
                        Log.d("TAG", jsonString);
                        //se è una risposta di multipli elementi
                        if(listClassesType != null){
                                responseObject = mGson.fromJson(jsonString, listClassesType);
                        }
                        //se è una risposta di singolo elemento
                        else{
                                //trasforma in oggetto.
                                responseObject = mGson.fromJson(jsonString, beanClass);
                        }

                        return Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));

                }
                catch (UnsupportedEncodingException e)
                {
                        e.printStackTrace();
                }
                catch (JsonSyntaxException e){
                        return Response.error(new VolleyError(e.getMessage(), e.getCause()));
                }
                return null;
        }

        @Override
        protected void deliverResponse(Object response)
        {
                mListener.onResponse(response);
        }


        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
                if (mParams != null && mParams.size()>0)
                {
                        return mParams;
                }
                return super.getParams();

        }

        /**
         * personalizzare HEADER di richiesta
         * @return
         * @throws AuthFailureError
         */
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError
        {
                if(mHeaders !=null && mHeaders.size() > 0){
                        return mHeaders;
                }
                else{
                        return Collections.emptyMap();

                }
        }


}
