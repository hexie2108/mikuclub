package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.delegates.models.ResourceModel;

/**
 *  根据需要生成对应资源的请求
 */
public class CommentDelegate extends  BaseDelegate
{

        public CommentDelegate(int tag)
        {
                super(tag, new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.COMMENTS));
        }

        /**
         * 获和某文章id 相关的评论列表
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
                        parameters.setOrderby(GlobalConfig.OrderBy.DATE);
                }

                getModel().selectForList(parameters.toMap(), null, getTag(), httpCallBack);

        }





}
