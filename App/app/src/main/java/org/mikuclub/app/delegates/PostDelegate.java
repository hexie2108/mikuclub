package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.ParametersListPosts;
import org.mikuclub.app.delegates.models.ResourceModel;

import java.util.ArrayList;

/**
 * 根据需要生成对应资源的请求
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
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE_OF_SLIDERSHOW);
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
         * 获取分类文章列表
         *
         * @param categoryId   分类id
         * @param page         请求页数
         * @param httpCallBack 回调方法
         */

        public void getCategoryPostList(int categoryId, int page, HttpCallBack httpCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                //创建分类id列表
                ArrayList<Integer> categoryList = new ArrayList();
                categoryList.add(categoryId);
                //添加进参数中
                parametersListPosts.setCategories(new ArrayList<Integer>(categoryList));

                getPostListBase(page, httpCallBack, parametersListPosts);

        }

        /**
         * 获取普通文章列表
         * 排除魔法区
         *
         * @param page         请求页数
         * @param httpCallBack 回调方法
         */

        public void getPostList(int page, HttpCallBack httpCallBack)
        {
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                //创建分类id列表
                ArrayList<Integer> excludeCategoryList = new ArrayList();
                excludeCategoryList.add(GlobalConfig.CATEGORY_ID_MOFA);
                //添加进参数中
                parametersListPosts.setCategories_exclude(excludeCategoryList);
                getPostListBase(page, httpCallBack, parametersListPosts);

        }


        /**
         * 获取文章列表 (基础函数)
         *
         * @param page                请求页数
         * @param httpCallBack        回调方法
         * @param parametersListPosts 请求参数类
         */

        private void getPostListBase(int page, HttpCallBack httpCallBack, ParametersListPosts parametersListPosts)
        {
                parametersListPosts.setPage(page);

                //如果每页文章数量未初始化
                if (parametersListPosts.getPer_page() == null)
                {
                        parametersListPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE);
                }
                parametersListPosts.setOrderby(GlobalConfig.OrderBy.DATE);
                parametersListPosts.setStatus(GlobalConfig.Status.PUBLISH);

                postModel.selectForList(parametersListPosts.toMap(), tag, httpCallBack);

        }


}
