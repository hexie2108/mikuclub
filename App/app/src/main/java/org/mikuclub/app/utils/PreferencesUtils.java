package org.mikuclub.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.mikuclub.app.contexts.MyApplication;

/**
 * 管理 应用参数配置储存
 */
public class PreferencesUtils
{

        private static final String NAME_APPLICATION = "application_preference";
        private static final String NAME_CATEGORY = "category_preference";
        private static final String NAME_USER = "user_preference";
        private static final String NAME_POST = "post_preference";

        //应用参数
        private static SharedPreferences applicationPreference;
        //分类参数
        private static SharedPreferences categoryPreference;
        //用户信息参数
        private static SharedPreferences userPreference;

        //文章信息参数
        private static SharedPreferences postPreference;

        public static SharedPreferences getApplicationPreference()
        {
                if (applicationPreference == null)
                {
                        applicationPreference = MyApplication.getContext().getSharedPreferences(NAME_APPLICATION, Context.MODE_PRIVATE);
                }
                return applicationPreference;
        }

        public static SharedPreferences getCategoryPreference()
        {
                if (categoryPreference == null)
                {
                        categoryPreference = MyApplication.getContext().getSharedPreferences(NAME_CATEGORY, Context.MODE_PRIVATE);
                }

                return categoryPreference;
        }

        public static SharedPreferences getUserPreference()
        {
                if (userPreference == null)
                {
                        userPreference = MyApplication.getContext().getSharedPreferences(NAME_USER, Context.MODE_PRIVATE);
                }
                return userPreference;
        }
        public static SharedPreferences getPostPreference()
        {
                if (postPreference == null)
                {
                        postPreference = MyApplication.getContext().getSharedPreferences(NAME_POST, Context.MODE_PRIVATE);
                }
                return postPreference;
        }


}
