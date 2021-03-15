package org.mikuclub.app.utils.social;

import android.content.Context;

import com.tencent.connect.UnionInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ToastUtils;

/**
 * 自定义QQ登陆动作监听器
 * QQ login action listener
 */
public abstract class TencentListener extends DefaultUiListener
{
        private Context context;
        private String openID;
        private String accessToken;
        private String expires;
        private String unionId;

        public TencentListener(Context context)
        {
                this.context = context;
        }

        @Override
        public void onComplete(Object object)
        {
                LogUtils.v("QQ登陆成功");
                //获取openid和token
                getOpenIDAndToken(object);
                //获取union id
                requestUnionId();

        }


        @Override
        public void onError(UiError uiError)
        {
                //登录失败
                LogUtils.v("QQ登陆失败");
        }

        @Override
        public void onCancel()
        {
                //取消登录
                LogUtils.v("QQ登陆取消");
        }

        /**
         * 获取令牌信息
         *
         * @param object
         */
        private void getOpenIDAndToken(Object object)
        {
                JSONObject jb = (JSONObject) object;
                try
                {

                        //openid用户唯一标识
                        openID = jb.getString(Constants.PARAM_OPEN_ID);
                        accessToken = jb.getString(Constants.PARAM_ACCESS_TOKEN);
                        expires = jb.getString(Constants.PARAM_EXPIRES_IN);

                        //保存信息到 腾讯API变量里
                        TencentUtils.getInstance().setOpenId(openID);
                        TencentUtils.getInstance().setAccessToken(accessToken, expires);
                }
                catch (JSONException e)
                {
                        e.printStackTrace();
                }
        }

        /**
         * 获取用户统一识别id , 之后再发送请求给初音服务器 获取初音社用户信息
         */
        private void requestUnionId(){
                IUiListener listener = new DefaultUiListener()
                {

                        @Override
                        public void onComplete(final Object response)
                        {
                                if (response != null)
                                {
                                        JSONObject jsonObject = (JSONObject) response;
                                        try
                                        {
                                                //获取unionid
                                                unionId = jsonObject.getString("unionid");
                                                //获取QQ相关信息后的动作
                                                onSuccess();
                                        }
                                        catch (Exception e)
                                        {
                                                ToastUtils.shortToast("QQ Union id 不存在于请求结果中");
                                        }
                                }
                                else
                                {
                                        ToastUtils.shortToast("QQ Union id 不存在于请求结果中");
                                }
                        }
                        @Override
                        public void onError(UiError e)
                        {
                                ToastUtils.shortToast("QQ Union id 信息请求失败");
                        }
                        @Override
                        public void onCancel()
                        {
                                ToastUtils.shortToast("QQ Union id 信息请求已取消");
                        }
                };
                UnionInfo unionInfo = new UnionInfo(context, TencentUtils.getInstance().getQQToken());
                unionInfo.getUnionId(listener);
        }

        /**
         * 获取QQ相关信息后的动作
         */
        public  abstract  void onSuccess();



        public String getOpenID()
        {
                return openID;
        }

        public String getAccessToken()
        {
                return accessToken;
        }

        public String getUnionId()
        {
                return unionId;
        }
}
