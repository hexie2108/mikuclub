package org.mikuclub.app.storage;

import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.storage.base.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 管理用户登陆相关的共享偏好
 *  * Manage user-login-related sharing preferences
 */
public class UserPreferencesUtils
{
        //用户登陆信息
        private static final String USER_LOGIN = "user_login";
        //用户令牌
        private static final String USER_TOKEN = "user_token";

        private static UserLogin user;

        /**
         * 检查用户是否登陆了
         *
         * @return
         */
        public static boolean isLogin()
        {
                boolean isLogin = true;
                if (PreferencesUtils.getUserPreference().getString(USER_TOKEN, null) == null)
                {
                        isLogin = false;
                }
                return isLogin;
        }

        /*获取当前用户信息*/
        public static UserLogin getUser()
        {
                if (user == null)
                {
                        String userInfoString = PreferencesUtils.getUserPreference().getString(USER_LOGIN, null);
                        user = ParserUtils.fromJson(userInfoString, UserLogin.class);
                }

                return user;
        }

        /**
         *
         * 检测当前用户的ID是否等于指定ID
         * @param userId
         * @return
         */
        public static boolean isCurrentUser(int userId){
                boolean result= false;
                UserLogin user = getUser();
                //如果有登陆 并且 当前登陆的用户id 等于 指定的用户id
                if(user != null && user.getId() == userId){
                        result = true;
                }
                return result;
        }



        /**
         * 设置用户登陆信息
         */
        public static void login(String userInfoString)
        {

                UserLogin userLogin = ParserUtils.fromJson(userInfoString, UserLogin.class);
                //储存登陆信息
                PreferencesUtils.getUserPreference()
                        .edit()
                        .putString(USER_TOKEN, userLogin.getToken())
                        .putString(USER_LOGIN, userInfoString)
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
                        .remove(USER_LOGIN)
                        .remove(USER_TOKEN)
                        .apply();
                //清空用户登陆信息
                user = null;
        }

        /**
         * 创建一个带有用户登陆令牌的map, 用来添加到网络请求的header里 来通过服务端验证
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
                        headers.put("Authorization", "Bearer " + PreferencesUtils.getUserPreference().getString(USER_TOKEN, null));
                }
                return headers;
        }
}
