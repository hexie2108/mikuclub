package org.mikuclub.app.delegate;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.base.BaseDelegate;
import org.mikuclub.app.delegate.models.ResourceModel;
import org.mikuclub.app.javaBeans.parameters.CreatePostParameters;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.storage.ApplicationPreferencesUtils;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.DataUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 文章资源请求代理人
 * post resource request delegate
 */
public class PostDelegate extends BaseDelegate
{

        public PostDelegate(int tag)
        {
                super(tag, new ResourceModel(GlobalConfig.Server.ROOT + GlobalConfig.Server.POSTS));
        }


        /**
         * 获取置顶文章列表
         * 排除魔法区文章
         *
         * @param httpCallBack
         * @param page
         */
        public void getStickyPostList(HttpCallBack httpCallBack, int page)
        {
                PostParameters parametersPosts = new PostParameters();
                parametersPosts.setSticky(true);
                //排除魔法区
                parametersPosts.setCategories_exclude(new ArrayList<>(Collections.singletonList(GlobalConfig.CATEGORY_ID_MOFA)));
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
                Map<String, Object> queryParameters = new BaseParameters().toMap();
                putIfNotNull(queryParameters, "edit", 1);
                getModel().selectById(postId, queryParameters, UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);
        }


        /**
         * 获取文章列表 (基础函数)
         *
         * @param httpCallBack 回调方法
         * @param page         请求页数
         * @param parameters   请求参数类
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
                        parameters.setOrderby(GlobalConfig.Post.OrderBy.DATE);
                }
                //如果是搜索文章
                Map<String, String> headers = null;
                //如果是要搜索的情况, 或者是要获取 除了publish 以外其他状态的文章
                if (parameters.getSearch() != null || parameters.getStatus() != null)
                {
                        headers = UserPreferencesUtils.createLoggedUserHeader();
                }
                getModel().selectForList(parameters.toMap(), headers, getTag(), httpCallBack);

        }


        /**
         * 删除文章
         *
         * @param httpCallBack
         * @param postId
         */
        public void deletePost(HttpCallBack httpCallBack, int postId)
        {
                //设置基本url参数
                Map<String, Object> baseParametersMap = new BaseParameters().toMap();
                //增加确认永远删除参数
                baseParametersMap.put("force", 1);
                getModel().deleteById(postId, baseParametersMap, null, UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);
        }


        /**
         * 设置文章点赞和取消点赞功能
         *
         * @param httpCallBack
         * @param postId
         * @param isAddLike    true = 点赞, false = 取消点赞
         */
        public void setPostLikeCount(HttpCallBack httpCallBack, int postId, boolean isAddLike)
        {
                BaseParameters baseParameters = new BaseParameters();
                Map<String, Object> bodyParameters = new HashMap<>();
                putIfNotNull(bodyParameters, "post_id", postId);
                //如果是要取消点赞
                if (!isAddLike)
                {
                        putIfNotNull(bodyParameters, "cancel", 1);
                }
                Request.post(GlobalConfig.Server.POST_LIKE_COUNT, baseParameters.toMap(), bodyParameters, null, getTag(), httpCallBack);

        }


        /**
         * 增加文章分享次数
         *
         * @param postId
         */
        public void addPostShareCount(int postId)
        {
                Map<String, Object> queryParameters = new HashMap<>();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_SHARING_COUNT, queryParameters, null, getTag(), null);
        }

        /**
         * 增加文章查看次数计算
         *
         * @param postId
         */
        public void addPostViewCount(int postId)
        {
                Map<String, Object> queryParameters = new HashMap<>();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_VIEW_COUNT, queryParameters, null, getTag(), null);
        }

        /**
         * 增加文章失效次数
         *
         * @param httpCallBack
         * @param postId
         */
        public void addPostFailDownCount(HttpCallBack httpCallBack, int postId)
        {
                Map<String, Object> queryParameters = new HashMap<>();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "post_id", postId);
                Request.get(GlobalConfig.Server.POST_FAIL_DOWN_COUNT, queryParameters, null, getTag(), httpCallBack);
        }


        /**
         * 获取从最后访问时间 到目前为止 新发布的文章数量
         *
         * @param httpCallBack
         */
        public void getNewPostCount(HttpCallBack httpCallBack)
        {
                long lastAccessTime = ApplicationPreferencesUtils.getLatestAccessTime();
                //如果是0
                if (lastAccessTime == 0)
                {
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

        /**
         * 创建文章
         *
         * @param httpCallBack
         * @param parameters
         */
        public void createPost(HttpCallBack httpCallBack, CreatePostParameters parameters)
        {

                BaseParameters baseParameters = new BaseParameters();
                getModel().insert(baseParameters.toMap(), parameters.toMap(), UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }

        /**
         * 更新文章
         *
         * @param httpCallBack
         * @param parameters
         * @param postId
         */
        public void updatePost(HttpCallBack httpCallBack, CreatePostParameters parameters, int postId)
        {

                BaseParameters baseParameters = new BaseParameters();
                getModel().updateById(postId, baseParameters.toMap(), parameters.toMap(), UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }


        /**
         * 获取用户收藏夹
         *
         * @param httpCallBack
         */
        public void getPostFavorite(HttpCallBack httpCallBack)
        {
                Request.get(GlobalConfig.Server.POST_FAVORITE, new BaseParameters().toMap(), UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);
        }

        /**
         * 添加id到用户收藏夹
         *
         * @param httpCallBack
         * @param postId
         */
        public void setPostFavorite(HttpCallBack httpCallBack, int postId)
        {
                Map<String, Object> bodyParameters = new HashMap<>();
                putIfNotNull(bodyParameters, "post_id", postId);
                Request.post(GlobalConfig.Server.POST_FAVORITE, new BaseParameters().toMap(), bodyParameters, UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);
        }

        /**
         * 从用户收藏夹删除id
         *
         * @param httpCallBack
         * @param postId
         */
        public void deletePostFavorite(HttpCallBack httpCallBack, int postId)
        {
                Map<String, Object> queryParameters = new BaseParameters().toMap();
                putIfNotNull(queryParameters, "post_id", postId);
                /*================================================
                目前delete请求中的body参数无法被解读, 所以把post_id 加到 query参数里
                =================================================*/
                Request.delete(GlobalConfig.Server.POST_FAVORITE, queryParameters, null, UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);
        }


        /**
         * 获取收藏文章列表
         *
         * @param httpCallBack 回调方法
         * @param page         请求页数
         */
        public void getFavoritePostList(HttpCallBack httpCallBack, int page, PostParameters parameters)
        {

                parameters.setPage(page);
                parameters.setPer_page(GlobalConfig.NUMBER_PER_PAGE);

                Request.get(GlobalConfig.Server.APP_FAVORITE_POST_LIST, parameters.toMap(), UserPreferencesUtils.createLoggedUserHeader(), getTag(), httpCallBack);
        }

}
