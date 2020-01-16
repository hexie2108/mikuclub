package org.mikuclub.app.utils.social;


import android.content.Context;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;
import org.mikuclub.app.contexts.MyApplication;

/**
 * WEIBO接口类
 */
public class TencentUtils
{
        public static final String OPEN_TYPE = "qq";
        private static final String APP_KEY = "101842471";
        private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
        private static final String SCOPE = "follow_app_official_microblog";
        private static Tencent TencentAPI;



        public static Tencent getInstance()
        {
                //如果为null
                if(TencentAPI == null){
                        //初始化
                        TencentAPI = Tencent.createInstance(APP_KEY, MyApplication.getContext());
                }
                return TencentAPI;
        }


}
