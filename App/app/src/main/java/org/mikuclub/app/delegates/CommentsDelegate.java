package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.configs.Constants;
import org.mikuclub.app.javaBeans.parameters.ParametersListComments;
import org.mikuclub.app.models.ResourceModel;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.LogUtils;

/**
 *  根据需要生成对应资源的请求
 */
public class CommentsDelegate
{

        private ResourceModel commentModel;
        private int tag;

        public CommentsDelegate(int tag)
        {
                this.tag = tag;
                this.commentModel = new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.COMMENTS);

        }


        /**
         * 获和某文章id 相关的评论列表
         * @param  postId 文章id
         * @param offset 开始位置
         * @param wrapperCallBack
         */
        public void getCommentsListByPostId(int postId, int offset, WrapperCallBack wrapperCallBack)
        {

                int page = GeneralUtils.getNextPageNumber(offset, GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS);

                ParametersListComments parameters = new ParametersListComments();
                parameters.setPage(page);
                parameters.setPer_page(GlobalConfig.NUMBER_PER_PAGE_OF_COMMENTS);
                parameters.setOrderby(Constants.OrderBy.DATE);
                parameters.setPost(postId);

                commentModel.selectForList(parameters.toMap(), tag, wrapperCallBack);

        }




}
