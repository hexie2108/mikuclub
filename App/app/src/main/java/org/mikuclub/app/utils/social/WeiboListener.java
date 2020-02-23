package org.mikuclub.app.utils.social;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;

import org.mikuclub.app.utils.LogUtils;


/**
 * 自定义微博登陆动作监听器
 * Weibo login action listener
 */
public abstract class WeiboListener implements WbAuthListener
{
        private String openID;
        private String accessToken;
        private String expires;




        @Override
        public void onComplete(Oauth2AccessToken token)
        {
                LogUtils.v("微博登陆成功");
                openID = token.getUid();
                accessToken = token.getAccessToken();
                onSuccess();
        }

        @Override
        public void onError(UiError error)
        {
                //取消失败
                LogUtils.v("微博登陆失败");

        }

        @Override
        public void onCancel()
        {
                //取消登录
                LogUtils.v("微博登陆取消");
        }

        /**
         * 获取微博相关信息后的动作
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

}
