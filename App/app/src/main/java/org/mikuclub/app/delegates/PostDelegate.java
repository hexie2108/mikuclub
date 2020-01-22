package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.delegates.models.ResourceModel;
import org.mikuclub.app.utils.UserUtils;

import java.util.Map;

/**
 * 根据需要生成对应资源的请求
 */
public class PostDelegate extends BaseDelegate
{

        public PostDelegate(int tag)
        {
                super(tag, new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.POSTS));
        }


        /**
         * 获取置顶文章列表
         *
         * @param httpCallBack
         * @param page
         */
        public void getStickyPostList(HttpCallBack httpCallBack, int page)
        {
                PostParameters parametersPosts = new PostParameters();
                parametersPosts.setSticky(true);
                parametersPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE_OF_SLIDERSHOW);
                getPostList(httpCallBack, page, parametersPosts);
        }



        /**
         * 获取文章列表 (基础函数)
         *  @param httpCallBack        回调方法
         * @param page                请求页数
         * @param parameters 请求参数类
         */

        public void getPostList(HttpCallBack httpCallBack, int page, PostParameters parameters)
        {
                parameters.setPage(page);

                //如果每页文章数量未设置
                if (parameters.getPer_page() == null)
                {
                        parameters.setPer_page(GlobalConfig.NUMBER_PER_PAGE);
                }
                //如果排列顺序未设置
                if (parameters.getOrderby() == null)
                {
                        parameters.setOrderby(GlobalConfig.OrderBy.DATE);
                }
                //如果文章状态未设置
                if (parameters.getStatus() == null)
                {
                        parameters.setStatus(GlobalConfig.Status.PUBLISH);
                }

                //如果是搜索文章
                Map<String, String> headers = null;
                if(parameters.getSearch()!=null)
                {
                        headers = UserUtils.createLoggedUserHeader();
                }


                getModel().selectForList(parameters.toMap(), headers, getTag(), httpCallBack);

        }


}
