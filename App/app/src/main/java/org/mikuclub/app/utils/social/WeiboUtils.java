package org.mikuclub.app.utils.social;


import android.content.Context;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;

/**
 * 新浪微博 SDK接口管理器
 * Weibo SDK interface management
 */
public class WeiboUtils
{
        public static final String OPEN_TYPE = "sina";
        private static final String APP_KEY = "368573275";
        private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
        private static final String SCOPE = "follow_app_official_microblog";


        public static IWBAPI getInstance(Context context)
        {
                //初始化
                IWBAPI weiboAPI;
                AuthInfo authInfo = new AuthInfo(context, APP_KEY, REDIRECT_URL, SCOPE);
                weiboAPI = WBAPIFactory.createWBAPI(context);
                weiboAPI.registerApp(context, authInfo);
                return weiboAPI;
        }

}
