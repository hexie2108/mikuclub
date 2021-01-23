package org.mikuclub.app.storage;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.javaBeans.response.SiteCommunication;
import org.mikuclub.app.storage.base.PreferencesUtils;
import org.mikuclub.app.utils.ParserUtils;

/**
 * 管理应用全局相关的共享偏好
 * * Manage application-related sharing preferences
 */
public class ApplicationPreferencesUtils
{

        /**
         * 获取检查更新的到期时间
         *
         * @return
         */
        public static long getUpdateCheckExpire()
        {
                return PreferencesUtils.getApplicationPreference().getLong(GlobalConfig.Preferences.APP_UPDATE_EXPIRE, 0);
        }

        /**
         * 设置检查更新的有效期
         */
        public static void setUpdateCheckExpire()
        {
                //当前时间 + 一次检查更新带来的有效周期
                long expire = System.currentTimeMillis() + GlobalConfig.Preferences.APP_UPDATE_EXPIRE_TIME;
                //保存到偏好文件内
                PreferencesUtils.getApplicationPreference().edit().putLong(GlobalConfig.Preferences.APP_UPDATE_EXPIRE, expire).apply();
        }


        /**
         * 获取站点通知消息的到期时间
         *
         * @return
         */
        public static long getSiteCommunicationExpire()
        {
                return PreferencesUtils.getApplicationPreference().getLong(GlobalConfig.Preferences.SITE_COMMUNICATION_EXPIRE, 0);
        }

        /**
         * 获取站点通知消息+广告对象
         *
         * @return 消息和广告对象
         */
        public static SiteCommunication.SiteCommunicationBody getSiteCommunication()
        {
                String communicationString = PreferencesUtils.getApplicationPreference().getString(GlobalConfig.Preferences.SITE_COMMUNICATION, null);
                SiteCommunication.SiteCommunicationBody output = null;
                if (communicationString != null)
                {
                        output = ParserUtils.fromJson(communicationString, SiteCommunication.class).getBody();
                }
                return output;
        }

        /**
         * 检查app通知消息+广告缓存 是否存在
         *
         * @return true 缓存存在 | false 缓存不存在
         */
        public static boolean isExistSiteCommunication()
        {
                boolean exist = true;
                String communicationString = PreferencesUtils.getApplicationPreference().getString(GlobalConfig.Preferences.SITE_COMMUNICATION, null);
                if (communicationString == null)
                {
                        exist = false;
                }
                return exist;
        }


        /**
         * 设置站点通知消息的有效期
         *
         * @param siteCommunicationJson 序列化后的 消息对象
         */
        public static void setSiteCommunicationAndExpire(String siteCommunicationJson)
        {
                //当前时间 + 一次检查更新带来的有效周期
                long expire = System.currentTimeMillis() + GlobalConfig.Preferences.SITE_COMMUNICATION_EXPIRE_TIME;
                //保存到偏好文件内
                PreferencesUtils.getApplicationPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.SITE_COMMUNICATION, siteCommunicationJson)
                        .putLong(GlobalConfig.Preferences.SITE_COMMUNICATION_EXPIRE, expire).apply();
        }


        /**
         * 获取最新一次打开应用的时间
         *
         * @return
         */
        public static long getLatestAccessTime()
        {
                return PreferencesUtils.getApplicationPreference().getLong(GlobalConfig.Preferences.LATEST_ACCESS_TIME, 0);
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

        /**
         * 获取当前 日/夜模式 ID编号
         * @return boolean
         */
        public static int getAppUiMode()
        {
                return PreferencesUtils.getApplicationPreference().getInt(GlobalConfig.Preferences.APP_UI_MODE, 0);
        }

        /**
         * 设置 日/夜模式 ID编号
         * @param int 开关
         */
        public static void setAppUiMode(int uiMode)
        {
                //保存变更到共享偏好里
                PreferencesUtils.getApplicationPreference()
                        .edit()
                        .putInt(GlobalConfig.Preferences.APP_UI_MODE, uiMode)
                        .apply();
        }


}
