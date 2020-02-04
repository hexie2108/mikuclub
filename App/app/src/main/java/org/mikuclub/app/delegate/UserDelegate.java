package org.mikuclub.app.delegate;

import org.mikuclub.app.delegate.base.BaseDelegate;
import org.mikuclub.app.javaBeans.parameters.UpdateUserParameters;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.models.ResourceModel;
import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.utils.http.Request;

/**
 *  用户资源请求代理人
 *  user resource request delegate
 */
public class UserDelegate extends BaseDelegate
{

        public UserDelegate(int tag)
        {
                super(tag, new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.USERS));
        }


        /**
         * 获取指定作者的信息
         * @param httpCallBack
         * @param authorId
         */
        public void getAuthor(HttpCallBack httpCallBack, int authorId){

                BaseParameters baseParameters = new BaseParameters();
                Request.get(GlobalConfig.Server.GET_AUTHOR+authorId, baseParameters.toMap(), null, getTag(), httpCallBack);
        }

        /**
         * 获取用户的信息
         * @param httpCallBack
         * @param userId
         */
        public void getUser(HttpCallBack httpCallBack, int userId){

                getModel().selectById(userId, new BaseParameters().toMap(), null, getTag(), httpCallBack);
        }

        /**
         * 更新用户自己的信息
         * @param httpCallBack
         */
        public void updateUser(HttpCallBack httpCallBack, UpdateUserParameters bodyParameters){

                Request.post(GlobalConfig.Server.UPDATE_USER, new BaseParameters().toMap(),  bodyParameters.toMap(), UserPreferencesUtils.createLoggedUserHeader(),getTag(), httpCallBack);
        }



}
