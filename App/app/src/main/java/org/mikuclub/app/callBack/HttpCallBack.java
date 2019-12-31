package org.mikuclub.app.callBack;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;

import java.io.UnsupportedEncodingException;

import mikuclub.app.R;

/*questo è un classe wrapper  per gestire operazione callback di richiesta HTTP
 * 空的回调类, 用来处理网络请求的回调
 * 必须重写来实现各种需求
 * */
public class HttpCallBack
{

        private Object argument1;
        private Object argument2;

        public HttpCallBack()
        {
        }

        public HttpCallBack(Object argument1)
        {
                this.argument1 = argument1;
        }

        public HttpCallBack(Object argument1, Object argument2)
        {
                this.argument1 = argument1;
                this.argument2 = argument2;
        }

        public Object getArgument1()
        {
                return argument1;
        }

        public void setArgument1(Object argument1)
        {
                this.argument1 = argument1;
        }

        public Object getArgument2()
        {
                return argument2;
        }

        public void setArgument2(Object argument2)
        {
                this.argument2 = argument2;
        }

        /**
         * 自定义成功处理函数
         * 默认为空
         *
         * @param response
         */
        public void onSuccess(String response)
        {

        }

        /**
         * 自定义内容错误处理函数 (请求成功,但是内容有问题)
         * 默认为空
         */
        public void onError()
        {

        }


        /**
         * 自定义请求取消处理函数 (请求被半路取消)
         * 默认为空
         */
        public void onCancel()
        {

        }

        /**
         * 自定义网络错误处理函数
         * 默认为空
         */
        public void onHttpError()
        {

        }


        /**
         * 请求结束后的处理函数 (不管成功还是失败)
         * 默认为空, 在有需要的时候
         */
        public void onFinally()
        {

        }

        /**
         * 请求成功的情况
         *
         * @param response
         */
        public void onSuccessHandler(String response)
        {
                try
                {
                        //先解析一遍返回数据
                        JSONObject jsonObject = new JSONObject(response);
                        //获取内容状态码
                        int statusCode = jsonObject.getInt("status");
                        //如果请求内的状态码在200~300之间,   而且 内容主体不是空的, 说明请求结果正常
                        if (statusCode >= 200 && statusCode <= 300 && jsonObject.getJSONArray("body").length() > 0)
                        {

                                onSuccess(response);
                        }
                        //状态码异常,  或者 主体为空, 说明有异常错误
                        else
                        {


                                onError();
                        }

                        onFinally();
                }
                catch (JSONException exception)
                {
                        LogUtils.w("JSONObject无法解析返回数据");
                        exception.printStackTrace();
                }

        }


        /**
         * 请求失败的情况
         *
         * @param error
         */
        public void onErrorHandler(VolleyError error)
        {

                onHttpError();

                //NetworkResponse networkResponse = error.networkResponse;

                String errorMessage = "未知错误";
                if (error instanceof AuthFailureError)
                {
                        errorMessage = "请求信息验证失败";
                }
                else if (error instanceof NetworkError)
                {
                        errorMessage = "未知的网络错误";
                }
                else if (error instanceof NoConnectionError)
                {
                        errorMessage = "无法建立网络连接";
                }
                else if (error instanceof TimeoutError)
                {
                        errorMessage = "请求超时错误";
                }
                else if (error instanceof ServerError)
                {

                        if (error.networkResponse.statusCode == 404)
                        {
                                errorMessage = "404请求的页面不存在";
                        }
                        else
                        {

                                errorMessage = "服务器内部错误";
                        }
                }

                LogUtils.w(errorMessage + " : " + error.getMessage());
                Toast.makeText(MyApplication.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                error.printStackTrace();

                onFinally();
        }


}
