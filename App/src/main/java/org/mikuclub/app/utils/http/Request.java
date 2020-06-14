package org.mikuclub.app.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.context.MyApplication;
import org.mikuclub.app.utils.DataUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;

import java.io.File;
import java.util.Collections;
import java.util.Map;

/**
 * 网络请求发送类
 * Network Request Sending Class
 */
public class Request
{

        private static final String CONTENT_TYPE_JSON = "application/json";

        /**
         * 自定义网络超时重试策略
         */
        private static DefaultRetryPolicy customRetryPolicy = new DefaultRetryPolicy(GlobalConfig.RETRY_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        private static DefaultRetryPolicy retryPolicyForFile = new DefaultRetryPolicy(GlobalConfig.RETRY_TIME_FOR_FILE, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


        /**
         * GET请求
         *
         * @param url
         * @param params
         * @param headers
         * @param tag
         * @param httpCallBack
         */
        public static void get(String url, Map<String, Object> params, Map<String, String> headers, int tag, HttpCallBack httpCallBack)
        {
                //如果有URL参数要传递
                if (params != null && params.size() > 0)
                {
                        //把参数拼入url中
                        url = url + "?" + DataUtils.mapToString(params, "=", "&");
                }

                request(com.android.volley.Request.Method.GET, url, null, headers, tag, httpCallBack);
        }

        /**
         * POST请求
         *
         * @param url
         * @param params
         * @param bodyParams
         * @param headers
         * @param tag
         * @param httpCallBack
         */
        public static void post(String url, Map<String, Object> params, Map<String, Object> bodyParams, Map<String, String> headers, int tag, HttpCallBack httpCallBack)
        {
                //如果有URL参数要传递
                if (params != null && params.size() > 0)
                {
                        //把参数拼入url中
                        url = url + "?" + DataUtils.mapToString(params, "=", "&");
                }
                request(com.android.volley.Request.Method.POST, url, bodyParams, headers, tag, httpCallBack);
        }

        /**
         * DELETE请求
         *
         * @param url
         * @param params
         * @param bodyParams
         * @param headers
         * @param tag
         * @param httpCallBack
         */
        public static void delete(String url, Map<String, Object> params, Map<String, Object> bodyParams, Map<String, String> headers, int tag, HttpCallBack httpCallBack)
        {
                //如果有URL参数要传递
                if (params != null && params.size() > 0)
                {
                        //把参数拼入url中
                        url = url + "?" + DataUtils.mapToString(params, "=", "&");
                }

                request(com.android.volley.Request.Method.DELETE, url, bodyParams, headers, tag, httpCallBack);
        }

        /**
         * 基础请求方法
         *
         * @param method
         * @param url
         * @param params
         * @param headers
         * @param tag          assegnare un tag specifico alla richiesta, che può essere servito in caso di annullamento
         * @param httpCallBack
         */
        private static void request(int method, String url, final Map<String, Object> params, Map<String, String> headers, int tag, final HttpCallBack httpCallBack)
        {
                printRequestUrl(method, url);

                StringRequest stringRequest = new StringRequest(method, url,
                        response -> {
                                //只有在不是null的情况
                                if (httpCallBack != null)
                                {
                                        //调用回调成功方法
                                        httpCallBack.onSuccessHandler(response);
                                }
                        }, error -> {
                        //只有在不是null的情况
                        if (httpCallBack != null)
                        {
                                //调用回调错误方法
                                httpCallBack.onErrorHandler(error);
                        }
                })
                {

                        /**
                         * 如果有body参数, 则输出成JSON字符串格式
                         * @return
                         * @throws AuthFailureError
                         */
                        @Override
                        public byte[] getBody() throws AuthFailureError
                        {
                                if (params != null && !params.isEmpty())
                                {
                                        return ParserUtils.toJson(params).getBytes();
                                }
                                else
                                {
                                        return null;
                                }
                        }

                        /**
                         * 声明body的数据格式
                         * @return
                         */
                        @Override
                        public String getBodyContentType()
                        {
                                return CONTENT_TYPE_JSON;
                        }

                        /**
                         * 读取 headers参数
                         * @return
                         * @throws AuthFailureError
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError
                        {
                                //如果为空 则返回空map
                                if (headers == null || headers.size() == 0)
                                {
                                        return Collections.emptyMap();
                                }
                                else
                                {
                                        return headers;
                                }
                        }

                        /**
                         * 请求被取消的时候  回调onCancel方法
                         */
                        @Override
                        public void cancel()
                        {
                                super.cancel();
                                //只有在不是null的情况
                                if (httpCallBack != null)
                                {
                                        httpCallBack.onCancel();
                                }
                        }
                };
                //设置标签, 方便后续根据标签取消
                stringRequest.setTag(tag);
                //设置重试策略
                stringRequest.setRetryPolicy(customRetryPolicy);
                RequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(stringRequest);
        }


        /**
         * deprecated
         * 文件POST请求
         *
         * @param url
         * @param params
         * @param headers
         * @param file
         * @param tag
         * @param httpCallBack
         */
        public static void filePost(String url, final Map<String, Object> params, Map<String, Object> bodyParams, Map<String, String> headers, File file, int tag, final HttpCallBack httpCallBack)
        {

                printRequestUrl(com.android.volley.Request.Method.POST, url);

                //如果有URL参数要传递
                if (params != null && params.size() > 0)
                {
                        //把参数拼入url中
                        url = url + "?" + DataUtils.mapToString(params, "=", "&");
                }
                //改成string格式
                Map<String, String> bodyParamsString = (Map) bodyParams;

                FileRequest fileRequest = new FileRequest(com.android.volley.Request.Method.POST, url, bodyParamsString, file, headers, response -> {
                        String resultResponse = new String(response.data);
                        //只有在不是null的情况
                        if (httpCallBack != null)
                        {
                                //调用回调成功方法
                                httpCallBack.onSuccessHandler(resultResponse);
                        }

                }, error -> {
                        //只有在不是null的情况
                        if (httpCallBack != null)
                        {
                                //调用回调错误方法
                                httpCallBack.onErrorHandler(error);
                        }

                })
                {

                        /**
                         * 请求被取消的时候  回调onCancel方法
                         */
                        @Override
                        public void cancel()
                        {
                                super.cancel();
                                //只有在不是null的情况
                                if (httpCallBack != null)
                                {
                                        httpCallBack.onCancel();
                                }
                        }

                };

                fileRequest.setTag(tag);
                fileRequest.setRetryPolicy(retryPolicyForFile);
                RequestQueue.getInstance(MyApplication.getContext()).addRequestQueue(fileRequest);


        }

        /**
         * 记录 请求方式 和请求地址
         *
         * @param methodCode
         * @param url
         */
        private static void printRequestUrl(int methodCode, String url)
        {
                //记录请求方式和地址
                String methodString = "";
                switch (methodCode)
                {
                        case com.android.volley.Request.Method.GET:
                                methodString = "GET";
                                break;
                        case com.android.volley.Request.Method.POST:
                                methodString = "POST";
                                break;
                        case com.android.volley.Request.Method.DELETE:
                                methodString = "DELETE";
                                break;
                }
                LogUtils.v("请求地址 " + methodString + " " + url);

        }

        /**
         * 根据TAG批量取消请求
         *
         * @param tag
         */
        public static void cancelRequest(Object tag)
        {
                RequestQueue.getInstance(MyApplication.getContext()).cancelRequest(tag);
        }


}
