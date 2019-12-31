package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.utils.http.Request;

/**
 *  根据需要生成对应资源的请求
 */
public class AppUpdateDelegate
{

        private int tag;

        public AppUpdateDelegate(int tag)
        {
                this.tag = tag;
        }

        /**
         * 检查更新
         * @param httpCallBack
         */
        public void checkUpdate(HttpCallBack httpCallBack)
        {

                Request.get(GlobalConfig.Server.APP_UPDATE, null, null, tag, httpCallBack);

        }




}
