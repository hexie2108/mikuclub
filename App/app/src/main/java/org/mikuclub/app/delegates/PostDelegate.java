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
                parametersListPosts.setPage(1);
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

                int page = getNextPageNumber(start);
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_PAGE);

                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);

                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }


        /*
        获取搜索结果的文章列表
         */
        public void getPostListBySearch(String query, int start, WrapperCallBack wrapperCallBack)
        {
                int page = getNextPageNumber(start);

                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_PAGE);

                parametersListPosts.setSearch(query);
                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);
                postModel.selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }

        /**
         * 计算 下个请求应该设置的页数
         * @param number 当前已拥有的数据数量
         * @return
         */
        private int getNextPageNumber(int number){
                //计算页面数字, 页数是从1开始所以要+1
                int page = number / GlobalConfig.NUMBER_FOR_PAGE+1;
                int currentModulo = number % GlobalConfig.NUMBER_FOR_PAGE;

                //如果有取余, 说明当前页数还需要+1
                if(currentModulo > 0){
                        page++;
                }
                return page;

        }


}
