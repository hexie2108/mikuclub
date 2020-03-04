package org.mikuclub.app.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.storage.UserPreferencesUtils;

import mikuclub.app.R;

/**
 * 网络请求专用回调类
 * Network request callback class
 */
public class HttpCallBack
{

        /**
         * 请求成功后 调用的函数
         *
         * @param response
         */
        public void onSuccess(String response)
        {

        }

        /**
         * 出现内容错误后 调用的函数 (请求成功,但是回复内容有问题)
         *
         * @param wpError
         */
        public void onError(WpError wpError)
        {
                //默认 弹窗显示错误信息
                ToastUtils.shortToast(wpError.getBody().getMessage());

        }



        /**
         * 出现登陆令牌过期或错误后 调用的函数
         */
        public void onTokenError()
        {

        }

        /**
         * 出现网络错误后 调用的函数
         */
        public void onHttpError()
        {

        }


        /**
         * 请求结束后的处理函数
         * (不管成功还是失败, 只有在请求被取消的情况不会被调用)
         */
        public void onFinally()
        {

        }


        /**
         * 请求被取消后 调用的函数
         */
        public void onCancel()
        {

        }

        /**
         * 请求成功的时候 调用的管理方法
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
                                        LogUtils.v("内容空数组错误");
                                        //空数组内容错误
                                        onError(null);
                                }
                                else
                                {

                                        onSuccess(response);
                                }
                        }
                        //状态码异常, 报错
                        else
                        {

                                WpError wpError = ParserUtils.fromJson(response, WpError.class);
                                //如果错误信息有code, 并且code包含 invalid token的关键词, 就是登陆令牌错误
                                if (wpError.getBody().getCode().contains("invalid_token"))
                                {
                                        //清空登陆状态
                                        UserPreferencesUtils.logout();
                                        LogUtils.v("登陆信息已过期");
                                        //提示用户
                                        ToastUtils.shortToast("登陆信息已过期, 请重新登陆");
                                        onTokenError();
                                }
                                //否则则是 内容错误
                                else
                                {
                                        LogUtils.v("内容错误: "+wpError.getBody().getCode()+" / " + wpError.getBody().getMessage());
                                        onError(wpError);
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
         * 请求失败时的错误管理方法
         *
         * @param error
         */
        public void onErrorHandler(VolleyError error)
        {

                //NetworkResponse networkResponse = error.networkResponse;

                String errorMessage = ResourcesUtils.getString(R.string.internet_unknown_error);
                if (error instanceof AuthFailureError)
                {
                        errorMessage = ResourcesUtils.getString(R.string.internet_auth_failure_error);
                }
                else if (error instanceof NetworkError)
                {
                        errorMessage = ResourcesUtils.getString(R.string.internet_network_error);
                }
                else if (error instanceof NoConnectionError)
                {
                        errorMessage = ResourcesUtils.getString(R.string.internet_no_connection_error);
                }
                else if (error instanceof TimeoutError)
                {
                        errorMessage =  ResourcesUtils.getString(R.string.internet_timeout_error);
                }
                else if (error instanceof ServerError)
                {

                        if (error.networkResponse.statusCode == 404)
                        {
                                errorMessage =  ResourcesUtils.getString(R.string.internet_empty_error);
                        }
                        else
                        {

                                errorMessage =  ResourcesUtils.getString(R.string.internet_server_error);
                        }
                }

                LogUtils.w(error.getCause() + " : " + error.getMessage());
                ToastUtils.longToast(error.getCause()+" : "+error.getMessage());

                onHttpError();

                onFinally();
        }

}
