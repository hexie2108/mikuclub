package org.mikuclub.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 管理 应用参数配置储存
 */
public class PreferencesUtlis
{

        private static final String NAME_APPLICATION = "application_preference";
        private static final String NAME_CATEGORY = "category_preference";
        private static final String NAME_USER = "user_preference";

        //应用参数
        private static SharedPreferences applicationPreference;
        //分类参数
        private static SharedPreferences categoryPreference;
        //用户信息参数
        private static SharedPreferences userPreference;

        public static SharedPreferences getApplicationPreference(Context context)
        {
                if (applicationPreference == null)
                {
                        applicationPreference = context.getSharedPreferences(NAME_APPLICATION, Context.MODE_PRIVATE);
                }
                return applicationPreference;
        }

        public static SharedPreferences getCategoryPreference(Context context)
        {
                if (categoryPreference == null)
                {
                        categoryPreference = context.getSharedPreferences(NAME_CATEGORY, Context.MODE_PRIVATE);
                }

                return categoryPreference;
        }

        public static SharedPreferences getUserPreference(Context context)
        {
                if (userPreference == null)
                {
                        userPreference = context.getSharedPreferences(NAME_USER, Context.MODE_PRIVATE);
                }
                return userPreference;
        }

}
