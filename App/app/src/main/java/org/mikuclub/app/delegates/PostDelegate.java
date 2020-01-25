package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.delegates.models.ResourceModel;
import org.mikuclub.app.utils.storage.UserUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

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
         * 获取单独一个文章
         *
         * @param httpCallBack
         * @param postId
         */
        public void getPost(HttpCallBack httpCallBack, int postId)
        {
                BaseParameters baseParameters = new BaseParameters();
                getModel().selectById(postId, baseParameters.toMap(), null, getTag(), httpCallBack);
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




        /**
         * 文章点赞和取消点赞功能
         * @param httpCallBack
         * @param postId
         * @param isAddLike true = 点赞, false = 取消点赞
         * */
        public void postLikeCount(HttpCallBack httpCallBack, int postId, boolean isAddLike)
        {
                BaseParameters baseParameters = new BaseParameters();
                Map<String, Object> bodyParameters = new HashMap();
                putIfNotNull(bodyParameters, "post_id", postId);
                //如果是要取消点赞
                if(!isAddLike)
                {
                        putIfNotNull(bodyParameters, "cancel", 1);
                }
                Request.post(GlobalConfig.Server.POST_LIKE_COUNT, baseParameters.toMap(), bodyParameters, null, getTag(), httpCallBack);

        }


        /**
         * 文章分享次数计算
         * @param postId
         * */
        public void postShareCount(int postId)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_SHARING_COUNT, queryParameters, null, getTag(), null);
        }

        /**
         * 文章查看次数计算
         * @param postId
         * */
        public void postViewCount(int postId)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_VIEW_COUNT, queryParameters, null, getTag(), null);
        }

        /**
         * 文章失效次数计算
         * @param httpCallBack
         * @param postId
         * */
        public void postFailDownCount(HttpCallBack httpCallBack,  int postId)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_FAIL_DOWN_COUNT, queryParameters, null, getTag(), httpCallBack);
        }


}
