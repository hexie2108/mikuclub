package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.configs.Constants;
import org.mikuclub.app.javaBeans.parameters.ParametersListPosts;
import org.mikuclub.app.models.ResourceModel;
import org.mikuclub.app.utils.GeneralUtils;

/**
 *  根据需要生成对应资源的请求
 */
public class PostsDelegate
{

        private ResourceModel postModel;
        private int tag;

        public PostsDelegate(int tag)
        {
                this.tag = tag;
                this.postModel = new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.POSTS);

        }


        /*
        获取置顶文章列表
         */
        public void getStickyPostsList(WrapperCallBack wrapperCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(1);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_SLIDERSHOW);

                parametersListPosts.setSticky(true);
                parametersListPosts.setOrderby(Constants.OrderBy.DATE);
                parametersListPosts.setStatus(Constants.Status.PUBLISH);
                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }

        /**
         * 获取最新发布的都文章列表
         * @param offset 开始位置
         * @param wrapperCallBack
         */
        public void getRecentlyPostsList(int offset, WrapperCallBack wrapperCallBack)
        {

                int page = GeneralUtils.getNextPageNumber(offset, GlobalConfig.NUMBER_PER_PAGE);
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE);

                parametersListPosts.setOrderby(Constants.OrderBy.DATE);
                parametersListPosts.setStatus(Constants.Status.PUBLISH);

                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }


        /*
        获取搜索结果的文章列表
         */
        public void getPostsListBySearch(String query, int offset, WrapperCallBack wrapperCallBack)
        {
                int page = GeneralUtils.getNextPageNumber(offset, GlobalConfig.NUMBER_PER_PAGE);

                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE);

                parametersListPosts.setSearch(query);
                parametersListPosts.setOrderby(Constants.OrderBy.DATE);
                parametersListPosts.setStatus(Constants.Status.PUBLISH);
                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }



}
