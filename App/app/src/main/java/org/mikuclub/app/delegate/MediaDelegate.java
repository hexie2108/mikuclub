package org.mikuclub.app.delegate;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.base.BaseDelegate;
import org.mikuclub.app.delegate.models.ResourceModel;
import org.mikuclub.app.javaBeans.parameters.UpdatePostMetaParameters;
import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.io.File;
import java.util.Map;

/**
 * 多媒体资源请求代理人
 * media resource request delegate
 */
public class MediaDelegate extends BaseDelegate
{
        public MediaDelegate(int tag)
        {
                super(tag, new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.MEDIA));
        }


        /**
         * 上传头像
         *
         * @param httpCallBack
         * @param file
         * @param userId
         */
        public void updateAvatar(HttpCallBack httpCallBack, File file, int userId)
        {

                BaseParameters baseParameters = new BaseParameters();

                getModel().insertFile(baseParameters.toMap(), null, UserPreferencesUtils.createLoggedUserHeader(), file, getTag(), httpCallBack);

        }

        /**
         * 更新头像图片的元数据
         * 添加用户id到元数据
         *
         * @param httpCallBack
         * @param postId
         * @param userId
         */
        public void updateAvatarMeta(HttpCallBack httpCallBack, int postId, int userId)
        {

                //设置基本url参数
                BaseParameters baseParameters = new BaseParameters();

                //设置body参数
                UpdatePostMetaParameters bodyParameter = new UpdatePostMetaParameters();
                bodyParameter.setPost_id(postId);
                bodyParameter.setMeta_key(GlobalConfig.Metadata.Attachment._WP_ATTACHMENT_WP_USER_AVATAR);
                bodyParameter.setMeta_value(userId);

                //发送请求
                Request.post(GlobalConfig.Server.POST_META, baseParameters.toMap(), bodyParameter.toMap(), UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }

        /**
         * 删除图片
         *
         * @param httpCallBack
         * @param mediaId
         */
        public void deleteMedia(HttpCallBack httpCallBack, int mediaId)
        {

                //设置基本url参数
                Map<String, Object> baseParametersMap =new BaseParameters().toMap();
                //增加确认永远删除参数
                baseParametersMap.put("force", 1);

                //发送请求
                getModel().deleteById(mediaId, baseParametersMap, null, UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }



}
