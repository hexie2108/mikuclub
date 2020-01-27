package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.models.ResourceModel;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.parameters.CreateCommentParameters;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.Map;

/**
 *  根据需要生成对应资源的请求
 */
public class UserDelegate extends  BaseDelegate
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



}
