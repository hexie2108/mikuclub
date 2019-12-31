package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.configs.Constants;
import org.mikuclub.app.javaBeans.parameters.ParametersListPosts;
import org.mikuclub.app.models.ResourceModel;

/**
 * 根据需要生成对应资源的请求
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


        /**
         * 获取置顶文章列表
         *
         * @param page
         * @param httpCallBack
         */
        public void getStickyPostList(int page, HttpCallBack httpCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setSticky(true);
                getPostListBase(page, httpCallBack, parametersListPosts);

        }

        /**
         * 根据搜索文本获取文章列表
         *
         * @param query
         * @param page
         * @param httpCallBack
         */
        public void getSearchPostList(String query, int page, HttpCallBack httpCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setSearch(query);
                getPostListBase(page, httpCallBack, parametersListPosts);

        }


        /**
         * 获取普通文章列表
         *
         * @param page            请求页数
         * @param httpCallBack 回调方法
         */

        public void getPostList(int page, HttpCallBack httpCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                getPostListBase(page, httpCallBack, parametersListPosts);

        }

        /**
         * 获取文章列表 (基础函数)
         * @param page                请求页数
         * @param httpCallBack     回调方法
         * @param parametersListPosts 请求参数类
         */

        private void getPostListBase(int page, HttpCallBack httpCallBack, ParametersListPosts parametersListPosts)
        {
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE);
                parametersListPosts.setOrderby(Constants.OrderBy.DATE);
                parametersListPosts.setStatus(Constants.Status.PUBLISH);

                postModel.selectForList(parametersListPosts.toMap(), tag, httpCallBack);

        }


}
