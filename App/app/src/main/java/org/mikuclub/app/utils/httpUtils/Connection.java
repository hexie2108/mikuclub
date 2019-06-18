package org.mikuclub.app.utils.httpUtils;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.lang.reflect.Type;

import mikuclub.app.R;

/**
 * questo è classe utils che occupa di mandare tutte le richieste
 */
public class Connection
{
        /**
         * personalizza la politica di retry per tutte le richieste
         */
        private static DefaultRetryPolicy defaultRetryPolicy = new DefaultRetryPolicy(GlobalConfig.RETRY_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        private Gson gson = new Gson();

        /**
         * metodo get
         *
         * @param url
         * @param params
         * @param tag             assegnare un tag specifico alla richiesta, che può essere servito in caso di annullamento
         * @param wrapperCallBack
         */
        public static void get(String url, Map params, String tag, WrapperCallBack wrapperCallBack)
        {
                //se parametri non è vuoto
                if (params != null && params.size() > 0)
                {
                        //concatenare i Parametri sul URL
                        try
                        {
                                url = url + "?" + StringUtils.mapToString(params, "=", "&");
                        }
                        catch (Exception error)
                        {
                                wrapperCallBack.onError(error);
                        }

                }
                request(Request.Method.GET, url, null, null, tag, wrapperCallBack);
        }

        /**
         * metodo post
         *
         * @param url
         * @param params
         * @param tag             assegnare un tag specifico alla richiesta, che può essere servito in caso di annullamento
         * @param wrapperCallBack
         */
        public static void post(String url, final Map params, String tag, WrapperCallBack wrapperCallBack)
        {
                request(Request.Method.POST, url, params, null, tag, wrapperCallBack);
        }

        /**
         * metodo delete
         *
         * @param url
         * @param tag             assegnare un tag specifico alla richiesta, che può essere servito in caso di annullamento
         * @param wrapperCallBack
         */
        public static void delete(String url, String tag, WrapperCallBack wrapperCallBack)
        {
                request(Request.Method.DELETE, url, null, null, tag, wrapperCallBack);
        }

        /**
         * method di richiesta base
         *
         * @param method
         * @param url
         * @param params
         * @param headers
         * @param tag             assegnare un tag specifico alla richiesta, che può essere servito in caso di annullamento
         * @param wrapperCallBack
         */
        private static void request(int method, String url, final Map params, final Map headers, String tag, final WrapperCallBack wrapperCallBack)
        {

                StringRequest stringRequest = new StringRequest(method, url,
                        new Response.Listener<String>()
                        {
                                @Override
                                public void onResponse(String response)
                                {
                                        wrapperCallBack.onSuccess(response);

                                }
                        }, new Response.ErrorListener()
                {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                                wrapperCallBack.onError(error);
                        }
                })
                {
                        /**
                         * estrarre parametri per  POST
                         * @return
                         * @throws AuthFailureError
                         */
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                                if (params == null || params.size() == 0)
                                {
                                        return null;
                                }
                                else
                                {
                                        return params;
                                }
                        }

                        /**
                         * personalizzare HEADER di richiesta
                         * @return
                         * @throws AuthFailureError
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError
                        {
                                if (headers == null || headers.size() == 0)
                                {
                                        return Collections.emptyMap();
                                }
                                else
                                {
                                        return headers;
                                }
                        }
                };

                stringRequest.setTag(tag);
                stringRequest.setRetryPolicy(defaultRetryPolicy);
                HttpRequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(stringRequest);
        }


        /**
         * metodo get per JSON
         * @param url
         * @param params
         * @param beanClass
         * @param listClassesType
         * @param tag
         * @param wrapperCallBack
         */
        public static void jsonGet(String url, Map params, Class beanClass, Type listClassesType,String tag, WrapperCallBack wrapperCallBack)
        {
                //se parametri non è vuoto
                if (params != null && params.size() > 0)
                {
                        //contenare i parametri sul URL
                        try
                        {
                                url = url + "?" + StringUtils.mapToString(params, "=", "&");
                        }
                        catch (Exception error)
                        {
                                wrapperCallBack.onError(error);
                        }
                }
                jsonRequest(Request.Method.GET, url, null, null, beanClass, listClassesType, tag, wrapperCallBack);
        }

        /**
         *
         * @param url
         * @param params
         * @param beanClass
         * @param isArray
         * @param tag
         * @param wrapperCallBack
         */
        public static void jsonPost(String url, Map params, Class beanClass, Type listClassesType, String tag, WrapperCallBack wrapperCallBack)
        {
                jsonRequest(Request.Method.POST, url, params, null, beanClass, listClassesType, tag, wrapperCallBack);
        }


        /**
         * method di richiesta base per JSON
         * @param method
         * @param url
         * @param params
         * @param headers
         * @param beanClass
         * @param listClassesType
         * @param tag
         * @param wrapperCallBack
         */
        private static void jsonRequest(int method, String url, Map params, Map headers, Class beanClass, Type listClassesType, String tag, final WrapperCallBack wrapperCallBack)
        {
                GsonRequest gsonRequest = new GsonRequest(method, url, params, headers, beanClass, listClassesType,
                        new Response.Listener<Object>()
                        {
                                @Override
                                public void onResponse(Object response)
                                {
                                        wrapperCallBack.onSuccess(response);

                                }
                        }, new Response.ErrorListener()
                {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                                wrapperCallBack.onError(error);
                        }
                });

                gsonRequest.setTag(tag);
                gsonRequest.setRetryPolicy(defaultRetryPolicy);
                HttpRequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(gsonRequest);

        }


        /**
         * funzione per annullare la richiesta in corso secondo tag
         *
         * @param tag
         */
        public static void cancelRequest(Object tag)
        {
                HttpRequestQueue.getInstance(MyApplication.getContext()).cancelRequest(tag);
        }


        /**
         * funzione per get e set img remote
         * prima fa un check in cache di storage locale
         * poi fa un check in ram
         * se non trova nessun cache, farà la richiesta HTTP
         *
         * @param url
         * @param networkImageView
         */
        public static void getImg(String url, NetworkImageView networkImageView)
        {
                //crea una istanza di imageLoader con la politica di cache personalizzato
                ImageLoader imageLoader = new ImageLoader(HttpRequestQueue.getInstance(MyApplication.getContext()).getRequestQueue(), new ImageFileCache());
                //set default img per view
                networkImageView.setDefaultImageResId(R.drawable.loading);
                //set error image per view
                networkImageView.setErrorImageResId(R.drawable.error);
                //get e set image remote
                networkImageView.setImageUrl(url, imageLoader);
        }

}
