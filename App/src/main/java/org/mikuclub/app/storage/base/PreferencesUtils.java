package org.mikuclub.app.storage.base;

import android.content.Context;
import android.content.SharedPreferences;

import org.mikuclub.app.context.MyApplication;

/**
 * 应用共享偏好管理器
 * shared preference manager
 */
public class PreferencesUtils
{

        private static final String NAME_APPLICATION = "application_preference";
        private static final String NAME_CATEGORY = "category_preference";
        private static final String NAME_USER = "user_preference";
        private static final String NAME_POST = "post_preference";
        private static final String NAME_MESSAGE = "message_preference";

        //应用全局偏好
        private static SharedPreferences applicationPreference;
        //分类偏好
        private static SharedPreferences categoryPreference;
        //用户信息偏好
        private static SharedPreferences userPreference;
        //文章偏好
        private static SharedPreferences postPreference;
        //消息偏好
        private static SharedPreferences messagePreference;


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

        public static SharedPreferences getMessagePreference()
        {
                if (messagePreference == null)
                {
                        messagePreference = MyApplication.getContext().getSharedPreferences(NAME_MESSAGE, Context.MODE_PRIVATE);
                }
                return messagePreference;
        }


}
