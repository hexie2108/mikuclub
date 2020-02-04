package org.mikuclub.app.delegate;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.base.BaseDelegate;
import org.mikuclub.app.delegate.models.ResourceModel;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.storage.ApplicationPreferencesUtils;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.DataUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 *  文章资源请求代理人
 *  post resource request delegate
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
                parametersPosts.setPer_page(GlobalConfig.NUMBER_PER_PAGE_OF_SLIDERS);
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
                        headers = UserPreferencesUtils.createLoggedUserHeader();
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
                Map<String, Object> bodyParameters = new HashMap<>();
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
                Map<String, Object> queryParameters = new HashMap<>();
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
                Map<String, Object> queryParameters = new HashMap<>();
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
                Map<String, Object> queryParameters = new HashMap<>();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_FAIL_DOWN_COUNT, queryParameters, null, getTag(), httpCallBack);
        }

        /**
         * 获取从最后访问时间 到目前为止 新发布的文章数量
         * @param httpCallBack
         * */
        public void getNewPostCount(HttpCallBack httpCallBack)
        {
                long lastAccessTime = ApplicationPreferencesUtils.getLatestAccessTime();
                //如果是0
                if(lastAccessTime ==0){
                        //就获取系统时间, 避免报错
                        lastAccessTime = System.currentTimeMillis();
                }
                //转换成时间类, 然后再转发成对应时间格式的字符串
                String dateString = DataUtils.dateToString(new Date(lastAccessTime));

                Map<String, Object> queryParameters = new HashMap<>();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "date", dateString);
                Request.get(GlobalConfig.Server.NEW_POST_COUNT, queryParameters, null, getTag(), httpCallBack);
        }



}
