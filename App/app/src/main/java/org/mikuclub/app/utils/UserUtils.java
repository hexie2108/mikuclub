package org.mikuclub.app.utils;

import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.UserLogin;

import java.util.HashMap;
import java.util.Map;

public class UserUtils
{

        private static UserLogin user;

        /**
         * 检查用户是否登陆了
         *
         * @return
         */
        public static boolean isLogin()
        {
                boolean isLogin = true;
                if (PreferencesUtils.getUserPreference().getString(GlobalConfig.Preferences.USER_TOKEN, null) == null)
                {
                        isLogin = false;
                }
                return isLogin;
        }

        /*获取当前用户信息*/
        public static UserLogin getUser()
        {
                if(user == null){
                        String userInfoString = PreferencesUtils.getUserPreference().getString(GlobalConfig.Preferences.USER_LOGIN, null);
                        user = ParserUtils.userLogin(userInfoString);
                }

                return user;
        }


        /**
         * 记录用户登陆信息
         */
        public static void login(String userInfoString)
        {

                UserLogin userLogin = ParserUtils.userLogin(userInfoString);
                //储存登陆信息
                PreferencesUtils.getUserPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.USER_TOKEN, userLogin.getToken())
                        .putString(GlobalConfig.Preferences.USER_LOGIN, userInfoString)
                        .apply();
                //更新用户信息
                user = userLogin;
        }


        /**
         * 注销登陆用户相关信息
         *
         * @return
         */
        public static void logout()
        {
                //删除用户信息
                PreferencesUtils.getUserPreference()
                        .edit()
                        .remove(GlobalConfig.Preferences.USER_LOGIN)
                        .remove(GlobalConfig.Preferences.USER_TOKEN)
                        .apply();
                //清空用户登陆信息
                user = null;
        }

        /**
         * 创建一个带token数据的 map用作网络请求的头部信息
         * 用户必须登陆
         *
         * @return Map 如果用户有登陆, null 如果没登陆
         */
        public static Map<String, String> createLoggedUserHeader()
        {
                Map<String, String> headers = null;
                //如果用户有登陆
                if (isLogin())
                {
                        headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + PreferencesUtils.getUserPreference().getString(GlobalConfig.Preferences.USER_TOKEN, null));
                }
                return headers;
        }
}
