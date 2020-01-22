package org.mikuclub.app.callBack;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.javaBeans.resources.WpError;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.UserUtils;

/**
 * utilsDelegate 专用
 * 去除了 body内容长度的检查
 */
public class HttpCallBack
{

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
         *
         * @param response
         */
        public void onError(String response)
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
         * 登陆令牌过期或错误
         */
        public void onTokenError(){

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
                        //LogUtils.e(response);

                        //先解析一遍返回数据
                        JSONObject jsonObject = new JSONObject(response);
                        //获取内容状态码
                        int statusCode = jsonObject.getInt("status");
                        //如果请求内的状态码在200~300之间
                        if (statusCode >= 200 && statusCode <= 300)
                        {
                                //获取body内容
                                Object body = jsonObject.get("body");
                                //如果是一个json数组 , 但是长度为0
                                if (body instanceof JSONArray && ((JSONArray) body).length() == 0)
                                {
                                        //报空内容错误
                                        onError(response);
                                }
                                else
                                {
                                        onSuccess(response);
                                }
                        }
                        //状态码异常, 报错
                        else
                        {

                                WpError wpError = ParserUtils.wpError(response);
                                //如果错误信息有code, 并且code包含 invalid token的关键词
                                if (wpError.getBody().getCode().indexOf("invalid_token") != -1)
                                {
                                        //清空登陆状态
                                        UserUtils.logout();
                                        LogUtils.v("登陆信息已过期");
                                        //提示用户
                                        ToastUtils.shortToast("登陆信息已过期, 请重新登陆");
                                        onTokenError();
                                }
                                else
                                {
                                        onError(response);
                                }

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
                ToastUtils.shortToast(errorMessage);
                error.printStackTrace();

                onHttpError();

                onFinally();
        }

}
