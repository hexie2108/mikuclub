package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.utils.http.Request;

/**
 *  根据需要生成对应资源的请求
 */
public class UtilsDelegate extends BaseDelegate
{

        public UtilsDelegate(int tag)
        {
               super(tag);
        }

        /**
         * 检查更新
         * @param httpCallBack
         */
        public void checkUpdate(HttpCallBack httpCallBack)
        {
                Request.get(GlobalConfig.Server.APP_UPDATE, null, null, getTag(), httpCallBack);

        }

        /**
         * 获取分类信息
         * @param httpCallBack
         */
        public void getCategory(HttpCallBack httpCallBack)
        {
                BaseParameters baseParameters = new BaseParameters();
                Request.get(GlobalConfig.Server.CATEGORIES, baseParameters.toMap(), null, getTag(), httpCallBack);

        }

        /**
         * 登陆
         * @param httpCallBack
         */
        public void login(HttpCallBack httpCallBack,  String userName, String userPassword)
        {
                BaseParameters baseParameters = new BaseParameters();
                LoginParameters loginParameters = new LoginParameters();
                loginParameters.setUsername(userName);
                loginParameters.setPassword(userPassword);
                Request.post(GlobalConfig.Server.LOGIN, baseParameters.toMap(), loginParameters.toMap(), null, getTag(), httpCallBack);

        }




}
