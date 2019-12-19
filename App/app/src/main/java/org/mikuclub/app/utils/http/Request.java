package org.mikuclub.app.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.data.MapUtils;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import mikuclub.app.R;

/**
 * questo è classe utils che occupa di mandare tutte le richieste
 */
public class Request
{
        /**
         * 自定义网络超时重试策略
         */
        private static DefaultRetryPolicy customRetryPolicy = new DefaultRetryPolicy(GlobalConfig.RETRY_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        private static DefaultRetryPolicy retryPolicyForFile = new DefaultRetryPolicy(GlobalConfig.RETRY_TIME_FOR_FILE, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);





        /**
          * GET请求
         * @param url
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public static void get(String url, Map params, Map<String, String> headers,  int tag, WrapperCallBack wrapperCallBack)
        {
                //如果有参数要传递
                if (params != null && params.size() > 0)
                {
                        //把参数拼入url中
                        url = url + "?" + MapUtils.mapToString(params, "=", "&");
                        LogUtils.w(url);
                }

                request(com.android.volley.Request.Method.GET, url, null, headers, tag, wrapperCallBack);
        }

        /**
         * POST请求
         * @param url
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public static void post(String url, Map<String, String> params, Map<String, String> headers, int tag, WrapperCallBack wrapperCallBack)
        {
                request(com.android.volley.Request.Method.POST, url, params, headers, tag, wrapperCallBack);
        }

        /**
         * metodo delete
         *
         * @param url
         * @param tag             assegnare un tag specifico alla richiesta, che può essere servito in caso di annullamento
         * @param wrapperCallBack
         */
        public static void delete(String url, int tag, WrapperCallBack wrapperCallBack)
        {
                request(com.android.volley.Request.Method.DELETE, url, null, null, tag, wrapperCallBack);
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
        private static void request(int method, String url, final Map<String, String> params, final Map<String, String> headers, int tag, final WrapperCallBack wrapperCallBack)
        {

                StringRequest stringRequest  = new StringRequest(method, url,
                        new Response.Listener<String>()
                        {
                                @Override
                                public void onResponse(String response)
                                {
                                        wrapperCallBack.onSuccessHandler(response);
                                }
                        }, new Response.ErrorListener()
                {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                                wrapperCallBack.onErrorHandler(error);
                        }
                })
                {
                        /**
                         * 如果是POST类型的请求, 则从params 中读取参数
                         * @return
                         * @throws AuthFailureError
                         */
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                                if (params != null && !params.isEmpty())
                                {
                                        return params;
                                }
                                else
                                {
                                        return null;
                                }
                        }

                        /**
                         * 读取 headers参数
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

                        //取消的时候  回调函数
                        @Override
                        public void cancel()
                        {
                                super.cancel();
                                wrapperCallBack.onCancel();
                        }
                };

                stringRequest.setTag(tag);
                stringRequest.setRetryPolicy(customRetryPolicy);
                RequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(stringRequest);
        }




        /**
         * 文件POST请求
         * @param url
         * @param params
         * @param headers
         * @param file
         * @param tag
         * @param wrapperCallBack
         */
        public static void filePost(String url, Map<String,String> params, Map<String,String> headers, File file, int tag, final WrapperCallBack wrapperCallBack){


                FileRequest fileRequest = new FileRequest(com.android.volley.Request.Method.POST, url, params, file ,headers, new Response.Listener<NetworkResponse>()
                {
                        @Override
                        public void onResponse(NetworkResponse response)
                        {
                                String resultResponse = new String(response.data);
                                //Log.d("TAG", "hallo");

                        }
                }, new Response.ErrorListener()
                {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                                wrapperCallBack.onErrorHandler(error);
                        }
                });

                fileRequest.setTag(tag);
                fileRequest.setRetryPolicy(retryPolicyForFile);
                RequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(fileRequest);


        }






        /**
         * metodo get per JSON
         * @param url
         * @param params
         * @param beanClass
         * @param listClassesType
         * @param tag
         * @param wrapperCallBack
         *//*
        public static void jsonGet(String url, Map<String,String> params, Map<String,String> headers, Class beanClass, Type listClassesType,int tag, WrapperCallBack wrapperCallBack)
        {
                //se parametri non è vuoto
                if (params != null && params.size() > 0)
                {
                        //contenare i parametri sul URL
                        url = url + "?" + MapUtils.mapToString(params, "=", "&");
                }

                jsonRequest(Request.Method.GET, url, null, headers, beanClass, listClassesType, tag, wrapperCallBack);
        }*/

        /**
         *
         * @param url
         * @param params
         * @param beanClass
         * @param listClassesType
         * @param tag
         * @param wrapperCallBack
         *//*
        public static void jsonPost(String url, Map<String,String> params, Map<String,String> headers,Class beanClass, Type listClassesType, int tag, WrapperCallBack wrapperCallBack)
        {
                jsonRequest(Request.Method.POST, url, params, headers, beanClass, listClassesType, tag, wrapperCallBack);
        }*/


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
        /*
        private static void jsonRequest(int method, String url, Map<String,String> params, Map<String,String> headers, Class beanClass, Type listClassesType, int tag, final WrapperCallBack wrapperCallBack)
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
                gsonRequest.setRetryPolicy(customRetryPolicy);
                RequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(gsonRequest);

        }
*/



        /**
         *  根据TAG批量取消请求
         *
         * @param tag
         */
        public static void cancelRequest(Object tag)
        {
                RequestQueue.getInstance(MyApplication.getContext()).cancelRequest(tag);
        }


        /**
         * 获取网络图片
         * @param networkImageView
         * @param url
         */
        public static void getRemoteImg(NetworkImageView networkImageView, String url)
        {
                //创建图片加载器 (使用自定义缓存策略, 首先检查本地缓存, 找不到才会通过网络请求)
                ImageLoader imageLoader = ImageFileLoader.getImageLoader();

                //设置加载时的图案
                networkImageView.setDefaultImageResId(R.drawable.loop_black_16x9);
                //设置加载错误时的图片
                networkImageView.setErrorImageResId(R.drawable.error_outline);
                //请求图片
                networkImageView.setImageUrl(url, imageLoader);





        }

}
