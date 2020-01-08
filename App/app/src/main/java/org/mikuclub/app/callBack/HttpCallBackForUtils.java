package org.mikuclub.app.callBack;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.utils.LogUtils;

/**
 *  utilsDelegate 专用
 *  去除了 body内容长度的检查
 * */
public class HttpCallBackForUtils extends HttpCallBack
{


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
                        //如果请求内的状态码在200~300之间,   而且 内容主体不是空的, 说明请求结果正常
                        if (statusCode >= 200 && statusCode <= 300)
                        {
                                onSuccess(response);
                        }
                        //状态码异常,  或者 主体为空, 说明有异常错误
                        else
                        {
                                onError(response);
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
         * 错误的情况 也能收到数据
         * @param response
         */
        public void onError(String response)
        {
                super.onError();
        }
}
