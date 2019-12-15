package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.configs.PostConstants;
import org.mikuclub.app.javaBeans.parameters.ParametersListPosts;
import org.mikuclub.app.models.ResourceModel;

/**
 *  根据需要生成对应资源的请求
 */
public class PostDelegate
{

        private ResourceModel postModel;
        private int tag;

        public PostDelegate(int tag)
        {
                this.tag = tag;
                this.postModel = new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.POSTS);

        }


        /*
        获取置顶文章列表
         */
        public void getStickyPostList(WrapperCallBack wrapperCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                int page = 1;
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_SLIDERSHOW);

                parametersListPosts.setSticky(true);
                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);
                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }

        /**
         * 获取最新发布的都文章列表
         * @param start 开始位置
         * @param wrapperCallBack
         */
        public void getRecentlyPostList(int start, WrapperCallBack wrapperCallBack)
        {

                int page = start / GlobalConfig.NUMBER_FOR_RECENTLY_POSTS_LIST + 1;
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_RECENTLY_POSTS_LIST);

                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);

                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }


        /*
        获取搜索结果的文章列表
         */
        public void getPostListBySearch(String query, int start, WrapperCallBack wrapperCallBack)
        {

                int page = start / GlobalConfig.NUMBER_FOR_PAGE + 1;

                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_PAGE);

                parametersListPosts.setSearch(query);
                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);
                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }


}
