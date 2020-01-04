package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.ParametersBase;
import org.mikuclub.app.utils.http.Request;

import java.security.Policy;

import androidx.appcompat.view.menu.BaseMenuPresenter;

/**
 *  根据需要生成对应资源的请求
 */
public class BaseDelegate
{

        private int tag;

        public BaseDelegate(int tag)
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

        /**
         * 获取分类信息
         * @param httpCallBack
         */
        public void getCategory(HttpCallBack httpCallBack)
        {
                ParametersBase parametersBase = new ParametersBase();
                Request.get(GlobalConfig.Server.CATEGORIES, parametersBase.toMap(), null, tag, httpCallBack);

        }




}
