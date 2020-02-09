package org.mikuclub.app.delegate;

import org.mikuclub.app.delegate.base.BaseDelegate;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.models.ResourceModel;
import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.parameters.CreateCommentParameters;
import org.mikuclub.app.storage.UserPreferencesUtils;

import java.util.Map;

/**
 *  评论资源请求代理人
 *  Comment resource request delegate
 */
public class CommentDelegate extends BaseDelegate
{

        public CommentDelegate(int tag)
        {
                super(tag, new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.COMMENTS));
        }

        /**
         * 获取某文章相关的评论
         * @param httpCallBack
         * @param page                请求页数
         * @param parameters 请求参数类
         */
        public void getCommentList(HttpCallBack httpCallBack, int page, CommentParameters parameters)
        {

                parameters.setPage(page);
                //如果每页评论数量未设置
                if (parameters.getPer_page() == null)
                {
                        parameters.setPer_page(GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS);
                }
                //如果排列顺序未设置
                if (parameters.getOrderby() == null)
                {
                        parameters.setOrderby(GlobalConfig.Post.OrderBy.DATE);
                }

                getModel().selectForList(parameters.toMap(), null, getTag(), httpCallBack);

        }

        /**
         * 创建评论
         * @param httpCallBack
         * @param createCommentParameters
         */
        public void createComment(HttpCallBack httpCallBack, CreateCommentParameters createCommentParameters){


                Map<String, String> headers = UserPreferencesUtils.createLoggedUserHeader();
                getModel().insert(new BaseParameters().toMap(), createCommentParameters.toMap(), headers,getTag(), httpCallBack);
        }





}
