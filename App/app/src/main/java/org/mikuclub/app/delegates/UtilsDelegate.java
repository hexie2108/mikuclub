package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

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
         * 获取分类信息缓存
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
        public void login(HttpCallBack httpCallBack, LoginParameters loginParameters)
        {
                BaseParameters baseParameters = new BaseParameters();

                Request.post(GlobalConfig.Server.LOGIN, baseParameters.toMap(), loginParameters.toMap(), null, getTag(), httpCallBack);

        }

        /**
         * token有效性检查
         * @param httpCallBack
         */
        public void tokenValidate(HttpCallBack httpCallBack)
        {
                BaseParameters baseParameters = new BaseParameters();
                Map header = GeneralUtils.createHeaderWithTokenForLoggedUser();

                Request.post(GlobalConfig.Server.TOKEN_VALIDATE, baseParameters.toMap(), null, header, getTag(), httpCallBack);

        }




        /**
         * 文章点赞和取消点赞功能
         * @param httpCallBack
         * @param postId
         * @param isAddLike true = 点赞, false = 取消点赞
         * */
        public void likePost(HttpCallBack httpCallBack, int postId, boolean isAddLike)
        {
                BaseParameters baseParameters = new BaseParameters();
                Map<String, String> bodyParameters = new HashMap();
                putIfNotNull(bodyParameters, "post_id", postId);
                //如果是要取消点赞
                if(!isAddLike)
                {
                        putIfNotNull(bodyParameters, "cancel", 1);
                }
                Request.post(GlobalConfig.Server.LIKE_POST, baseParameters.toMap(), bodyParameters, null, getTag(), httpCallBack);

        }

}
