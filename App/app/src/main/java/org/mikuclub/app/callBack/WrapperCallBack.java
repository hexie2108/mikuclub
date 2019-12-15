package org.mikuclub.app.callBack;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.utils.LogUtils;

/*questo è un classe wrapper  per gestire operazione callback di richiesta HTTP
* 空的回调类, 用来处理网络请求的回调
* 必须重写来实现各种需求
* */
public class WrapperCallBack
{

        private Object argument1;
        private Object argument2;

        public WrapperCallBack()
        {
        }

        public WrapperCallBack(Object argument1)
        {
                this.argument1 = argument1;
        }
        public WrapperCallBack(Object argument1, Object argument2)
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
         *  请求成功的情况
         * 默认什么都不做
         * @param response
         */
        public void onSuccess(String response)
        {

        }

        /**
         * 自定义错误处理函数
         * 默认为空, 在有需要的时候
         */
        public void onError()
        {

        }


        /**
         * 请求失败的情况
         * @param error
         */
        public void onErrorHandler(VolleyError error)
        {

                onError();

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                //如果是网络连接错误或超时
                if (networkResponse == null)
                {
                        if (error.getClass().equals(TimeoutError.class))
                        {
                                errorMessage = "Request timeout";
                                handleOnTimeOutError(error);
                        }
                        else if (error.getClass().equals(NoConnectionError.class))
                        {
                                errorMessage = "Failed to connect server";
                                handleOnConnectionError(error);
                        }

                        //Log.v("Error", errorMessage);
                }
                //其他错误情况 (网络正常, 服务器返回包含错误信息的json)
                else
                {
                        //从返回值里获取错误信息 (生成字符串格式)
                        String result = new String(networkResponse.data);
                        try
                        {
                                //转换成 JSON类
                                JSONObject response = new JSONObject(result);
                                //get status code
                                int status = response.getJSONObject("data").getInt("status");
                                //get error title
                                String code = response.getString("code");
                                //get error description
                                String message = response.getString("message");

                                Log.e("Error Status", status + "");
                                Log.e("Error Message", code + " : " + message);

                                if (networkResponse.statusCode == 404)
                                {
                                        errorMessage = "Resource not found";
                                        handleOnNotFoundError(error);
                                }
                                else if (networkResponse.statusCode == 401)
                                {
                                        errorMessage = message + " Please login again";
                                        handleOnUnauthorizedError(error);
                                }
                                else if (networkResponse.statusCode == 400)
                                {
                                        errorMessage = message + " Check your inputs";
                                        handleOnBadRequestError(error);

                                }
                                else if (networkResponse.statusCode == 500)
                                {
                                        errorMessage = message + " Something is getting wrong";
                                        handleOnInternalServerError(error);
                                }
                        }
                        catch (JSONException e)
                        {
                                LogUtils.w("解析网络请求错误原因的JSON失败");
                                e.printStackTrace();
                        }
                }
                error.printStackTrace();
        }

        public void handleOnTimeOutError(VolleyError error)
        {
                Log.v(LogUtils.INFO_TAG, "errore timeOut:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "error timeOut: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnConnectionError(VolleyError error)
        {
                Log.v(LogUtils.INFO_TAG, "errore connection:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "error connection: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }


        public void handleOnNotFoundError(VolleyError error)
        {
                Log.v(LogUtils.INFO_TAG, "errore notFound:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "error notFound: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnUnauthorizedError(VolleyError error)
        {
                Log.v(LogUtils.INFO_TAG, "errore Unauthorized:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "error Unauthorized: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnBadRequestError(VolleyError error)
        {
                Log.v(LogUtils.INFO_TAG, "error BadRequest:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "error BadRequest: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnInternalServerError(VolleyError error)
        {
                Log.v(LogUtils.INFO_TAG, "errore InternalServerError:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "error InternalServerError: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }


}
