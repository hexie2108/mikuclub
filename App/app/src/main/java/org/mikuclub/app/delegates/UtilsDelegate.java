package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.CreatePrivateMessageParameters;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.utils.storage.UserUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 根据需要生成对应资源的请求
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
                Map header = UserUtils.createLoggedUserHeader();

                Request.post(GlobalConfig.Server.TOKEN_VALIDATE, baseParameters.toMap(), null, header, getTag(), httpCallBack);

        }



}
