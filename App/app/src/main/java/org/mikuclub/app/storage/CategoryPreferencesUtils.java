package org.mikuclub.app.storage;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.javaBeans.response.Categories;
import org.mikuclub.app.javaBeans.response.baseResource.Category;
import org.mikuclub.app.storage.base.PreferencesUtils;
import org.mikuclub.app.utils.ParserUtils;

import java.util.List;

/**
 * 管理分类相关的共享偏好
 * * Manage category-related sharing preferences
 */
public class CategoryPreferencesUtils
{

        /**
         * 获取分类信息检测的到期时间
         *
         * @return
         */
        public static long getCategoryCheckExpire()
        {
                return PreferencesUtils.getCategoryPreference().getLong(GlobalConfig.Preferences.CATEGORIES_CACHE_EXPIRE, 0);
        }

        /**
         * 获取分类信息缓存字符串
         *
         * @return
         */
        public static String getCategoryCache()
        {
                return PreferencesUtils.getCategoryPreference().getString(GlobalConfig.Preferences.CATEGORIES_CACHE, null);
        }

        /**
         * 获取分类信息缓存
         *
         * @return
         */
        public static List<Category> getCategory()
        {
                List<Category> output=null;
                String categoryCache = getCategoryCache();
                if (categoryCache != null)
                {
                        output = ParserUtils.fromJson(CategoryPreferencesUtils.getCategoryCache(), Categories.class).getBody();
                }
                return output;
        }


        /**
         * 设置分类缓存信息 和 缓存的到期时间
         *
         * @param categoryCache
         */
        public static void setCategoryCacheAndExpire(String categoryCache)
        {
                //计算缓存过期时间
                long expire = System.currentTimeMillis() + GlobalConfig.Preferences.CATEGORIES_CACHE_EXPIRE_TIME;
                //保存分类缓存 和 缓存过期时间 到 共享偏好
                PreferencesUtils.getCategoryPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.CATEGORIES_CACHE, categoryCache)
                        .putLong(GlobalConfig.Preferences.CATEGORIES_CACHE_EXPIRE, expire)
                        .apply();
        }


        /**
         * 设置最新一次打开应用的时间
         */
        public static void setLatestAccessTime()
        {
                //保存变更到共享偏好里
                PreferencesUtils.getApplicationPreference()
                        .edit()
                        .putLong(GlobalConfig.Preferences.LATEST_ACCESS_TIME, System.currentTimeMillis())
                        .apply();
        }

}
