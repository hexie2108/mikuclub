package org.mikuclub.app.storage;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.storage.base.PreferencesUtils;
import org.mikuclub.app.utils.ParserUtils;

import java.util.ArrayList;

/**
 * 管理文章相关的共享偏好
 * * Manage post-related sharing preferences
 */
public class PostPreferencesUtils
{
        private static ArrayList<Integer> likedPostIds;
        private static ArrayList<Integer> favoritePostIds;
        private static ArrayList<Integer> historyPostIds;

        /**
         * 获取 点赞文章id储存数组
         * 如果在偏好里不存在的话 就创建一个新数组
         *
         * @return
         */
        public static ArrayList<Integer> getLikedPostIds()
        {

                //如果还未初始化
                if (likedPostIds == null)
                {
                        String likedPostIdsString = PreferencesUtils.getPostPreference().getString(GlobalConfig.Preferences.POST_LIKED_ARRAY, null);
                        //如果已经存在相关数据
                        if (likedPostIdsString != null)
                        {
                                //解析为数组
                                likedPostIds = new ArrayList<>(ParserUtils.integerArrayList(likedPostIdsString));
                        }
                        //如果数据不存在
                        else
                        {
                                //创建全新数组
                                likedPostIds = new ArrayList<>();
                        }
                }
                return likedPostIds;
        }

        /**
         * 检测 点赞文章id数组 是否包含指定的文章id
         *
         * @param postId 文章id
         * @return
         */
        public static boolean isContainedInLikedPostIds(int postId)
        {

                ArrayList<Integer> likedPostIds = getLikedPostIds();
                boolean isContained = false;
                if (likedPostIds != null && likedPostIds.contains(postId))
                {
                        isContained = true;
                }
                return isContained;
        }

        /**
         * 添加到数组 或从数组里移除 指定文章id
         * Manage likes array
         *
         * @param postId
         */
        public static void setLikedPostId(int postId)
        {
                ArrayList<Integer> likedPostIds = getLikedPostIds();

                //检查id是否已存在
                int position = likedPostIds.indexOf(postId);
                //如果id不存在, 就是添加操作
                if (position == -1)
                {
                        //添加id到数组
                        likedPostIds.add(postId);
                        //如果数组长度已超过上限
                        if (likedPostIds.size() > GlobalConfig.Preferences.POST_LIKED_ARRAY_SIZE)
                        {
                                //去除一半
                                likedPostIds = new ArrayList<>(likedPostIds.subList(likedPostIds.size() / 2, likedPostIds.size()));
                        }
                }
                //如果id已存在于数组中 那就是删除操作
                else
                {
                        //移除
                        likedPostIds.remove(position);
                }

                //保存变更到共享偏好里
                PreferencesUtils.getPostPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.POST_LIKED_ARRAY, ParserUtils.integerArrayListToJson(likedPostIds))
                        .apply();

        }





        /**
         * 获取 收藏夹文章id储存数组
         * 如果在偏好里不存在的话 就创建一个新数组
         *
         * @return
         */
        public static ArrayList<Integer> getFavoritePostIds()
        {

                //如果还未初始化
                if (favoritePostIds == null)
                {
                        String favoritePostIdsString = PreferencesUtils.getPostPreference().getString(GlobalConfig.Preferences.POST_FAVORITE, null);
                        //如果已经存在相关数据
                        if (favoritePostIdsString != null)
                        {
                                //解析为数组
                                favoritePostIds = new ArrayList<>(ParserUtils.integerArrayList(favoritePostIdsString));
                        }
                        //如果数据不存在
                        else
                        {
                                //创建全新数组
                                favoritePostIds = new ArrayList<>();
                        }
                }
                return favoritePostIds;
        }

        /**
         * 检测 收藏夹文章id数组 是否包含指定的文章id
         * @param postId 文章id
         * @return  true 包含, false 不包含
         */
        public static boolean isContainedInFavoritePostIds(int postId)
        {

                ArrayList<Integer> favoritePostIds = getFavoritePostIds();
                boolean isContained = false;
                if (favoritePostIds != null && favoritePostIds.contains(postId))
                {
                        isContained = true;
                }
                return isContained;
        }



        /**
         * 重新设置整个收藏夹
         * @param postIds
         */
        public static void setFavoritePostIds(ArrayList postIds)
        {
                favoritePostIds = postIds;

                //保存变更到共享偏好里
                PreferencesUtils.getPostPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.POST_FAVORITE, ParserUtils.integerArrayListToJson(favoritePostIds))
                        .apply();

        }



        /**
         * 获取 浏览记录文章id储存数组
         * 如果在偏好里不存在的话 就创建一个新数组
         *
         * @return ArrayList
         */
        public static ArrayList<Integer> getHistoryPostIds()
        {

                //如果还未初始化
                if (historyPostIds == null)
                {
                        String postIdsString = PreferencesUtils.getPostPreference().getString(GlobalConfig.Preferences.POST_HISTORY, null);
                        //如果已经存在相关数据
                        if (postIdsString != null)
                        {
                                //解析为数组
                                historyPostIds = new ArrayList<>(ParserUtils.integerArrayList(postIdsString));
                        }
                        //如果数据不存在
                        else
                        {
                                //创建全新数组
                                historyPostIds = new ArrayList<>();
                        }
                }

                return historyPostIds;
        }

        /**
         * 添加文章id到浏览记录数组
         * 如果已存在则移动到前排第一位置
         *
         * @param postId 文章id
         */
        public static void addHistoryPostId(int postId)
        {
                ArrayList<Integer> postIds = getHistoryPostIds();

                //检查id是否已存在
                int position = postIds.indexOf(postId);
                //如果id已存在, 就从数组中移除
                if (position != -1)
                {
                        postIds.remove(position);
                }
                //如果数组长度已达到或超过上限
                if (postIds.size() >= GlobalConfig.Preferences.POST_HISTORY_ARRAY_SIZE)
                {
                        //去除最后一个元素
                        postIds.remove(postIds.size()-1);
                }
                //添加id到数组头部
                postIds.add(0, postId);


                //保存变更到共享偏好里
                PreferencesUtils.getPostPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.POST_HISTORY, ParserUtils.integerArrayListToJson(postIds))
                        .apply();

        }





}
