package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.ParametersListComments;
import org.mikuclub.app.delegates.models.ResourceModel;

/**
 *  根据需要生成对应资源的请求
 */
public class CommentDelegate
{

        private ResourceModel commentModel;
        private int tag;

        public CommentDelegate(int tag)
        {
                this.tag = tag;
                this.commentModel = new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.COMMENTS);

        }


        /**
         * 获和某文章id 相关的评论列表
         * @param  postId 文章id
         * @param commentParentId 评论ID, 默认为0, 获取所有评论, 其他情况 为获取特定评论收到的回复
         * @param page 页数
         * @param httpCallBack
         */
        public void getCommentsListByPostId(int postId, int commentParentId,  int page, HttpCallBack httpCallBack)
        {

                ParametersListComments parameters = new ParametersListComments();
                parameters.setPage(page);
                parameters.setPer_page(GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS);
                parameters.setOrderby(GlobalConfig.OrderBy.DATE);
                parameters.setPost(postId);
                //去除评论收到的回复
                parameters.setParent(commentParentId);

                commentModel.selectForList(parameters.toMap(), tag, httpCallBack);

        }





}
