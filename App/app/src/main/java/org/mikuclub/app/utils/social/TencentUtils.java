package org.mikuclub.app.utils.social;


import com.tencent.tauth.Tencent;

import org.mikuclub.app.context.MyApplication;

/**
 * 腾讯QQ SDK接口管理
 * Tencent QQ SDK interface management
 */
public class TencentUtils
{
        public static final String OPEN_TYPE = "qq";
        private static final String APP_KEY = "101842471";

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
