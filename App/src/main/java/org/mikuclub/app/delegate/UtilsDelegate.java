package org.mikuclub.app.delegate;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.base.BaseDelegate;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.util.HashMap;
import java.util.Map;

/**
 *  辅助资源请求代理人
 *  auxiliary resource request delegate
 */
public class UtilsDelegate extends BaseDelegate
{

        public UtilsDelegate(int tag)
        {
                super(tag);
        }

        /**
         * 检查更新
         *
         * @param httpCallBack
         */
        public void checkUpdate(HttpCallBack httpCallBack)
        {
                BaseParameters baseParameters = new BaseParameters();
                Request.get(GlobalConfig.Server.APP_UPDATE, baseParameters.toMap(), null, getTag(), httpCallBack);

        }
        /**
         * 获取站点通知信息
         *
         * @param httpCallBack
         */
        public void getSiteCommunication(HttpCallBack httpCallBack)
        {
                BaseParameters baseParameters = new BaseParameters();
                Request.get(GlobalConfig.Server.SITE_COMMUNICATION, baseParameters.toMap(), null, getTag(), httpCallBack);

        }


        /**
         * 获取分类信息缓存
         *
         * @param httpCallBack
         */
        public void getCategory(HttpCallBack httpCallBack)
        {
                BaseParameters baseParameters = new BaseParameters();
                Request.get(GlobalConfig.Server.CATEGORIES, baseParameters.toMap(), null, getTag(), httpCallBack);

        }

        /**
         * 登陆
         *
         * @param httpCallBack
         */
        public void login(HttpCallBack httpCallBack, LoginParameters loginParameters)
        {
                BaseParameters baseParameters = new BaseParameters();
                Request.post(GlobalConfig.Server.LOGIN, baseParameters.toMap(), loginParameters.toMap(), null, getTag(), httpCallBack);

        }

        /**
         * token有效性检查
         *
         * @param httpCallBack
         */
        public void tokenValidate(HttpCallBack httpCallBack)
        {
                BaseParameters baseParameters = new BaseParameters();
                Map<String,String> header = UserPreferencesUtils.createLoggedUserHeader();

                Request.post(GlobalConfig.Server.TOKEN_VALIDATE, baseParameters.toMap(), null, header, getTag(), httpCallBack);
        }

        /**
         * 上报错误用的接口
         * @param errorMessage
         */
        public void test(String errorMessage){

                Map<String, Object> parametersMap = new HashMap();
                parametersMap.put("error_message", errorMessage);
                Request.post(GlobalConfig.Server.TEST, null, parametersMap, null, getTag(), null);

        }





}
