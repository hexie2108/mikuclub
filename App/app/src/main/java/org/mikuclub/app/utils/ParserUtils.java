package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.text.StringEscapeUtils;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.response.AppUpdate;
import org.mikuclub.app.javaBeans.response.SingleResponse;
import org.mikuclub.app.javaBeans.response.Categories;
import org.mikuclub.app.javaBeans.response.Comments;
import org.mikuclub.app.javaBeans.response.SingleComment;
import org.mikuclub.app.javaBeans.response.PrivateMessages;
import org.mikuclub.app.javaBeans.response.SingleUser;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.javaBeans.response.Posts;
import org.mikuclub.app.javaBeans.response.WpError;

import java.util.List;

/**
 * 负责反序列化 和 序列化 数据
 */
public class ParserUtils
{

        //2个gson 解析器支持的日期格式不一样
        //第一次支持 wordpress默认接口的日期格式
        private static Gson gsonWithWordpressApiDataFormat = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();
        // 第二个用来支持 wordpress自定义接口的日期格式
        private static Gson gsonWithDataBaseDataFormat = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON_CUSTOM_ENDPOINTS).create();


        /**
         * 解析文章列表
         *
         * @param response
         * @return
         */
        public static Posts posts(String response)
        {

                Posts posts = gsonWithWordpressApiDataFormat.fromJson(response, Posts.class);

                //遍历文章
                for (int i = 0; i < posts.getBody().size(); i++)
                {
                        //提取标题
                        String unescapeText = StringEscapeUtils.unescapeHtml4(posts.getBody().get(i).getTitle().getRendered());
                        //恢复被html转义的字符
                        posts.getBody().get(i).getTitle().setRendered(unescapeText);
                }


                return posts;

        }

        /**
         * 解析评论列表
         *
         * @param response
         * @return
         */
        public static Comments comments(String response)
        {

                Comments comments = gsonWithWordpressApiDataFormat.fromJson(response, Comments.class);
                return comments;

        }


        /**
         * 解析新建评论
         *
         * @param response
         * @return
         */
        public static SingleComment createComment(String response)
        {

                SingleComment singleComment = gsonWithWordpressApiDataFormat.fromJson(response, SingleComment.class);
                return singleComment;

        }

        /**
         * 解析软件更新信息
         *
         * @param response
         * @return
         */
        public static AppUpdate appUpdate(String response)
        {

                AppUpdate appUpdate = gsonWithWordpressApiDataFormat.fromJson(response, AppUpdate.class);
                return appUpdate;
        }


        /**
         * 解析分类菜单
         *
         * @param response
         * @return
         */
        public static Categories categories(String response)
        {
                Categories categories = gsonWithWordpressApiDataFormat.fromJson(response, Categories.class);
                return categories;
        }


        /**
         * 解析用户登陆信息
         *
         * @param response
         * @return
         */
        public static UserLogin userLogin(String response)
        {

                UserLogin userLogin = gsonWithWordpressApiDataFormat.fromJson(response, UserLogin.class);
                return userLogin;
        }


        /**
         * 解析wordpress错误
         *
         * @param response
         * @return
         */
        public static WpError wpError(String response)
        {
                WpError wpError = gsonWithWordpressApiDataFormat.fromJson(response, WpError.class);
                return wpError;
        }

        /**
         * 解析作者信息
         *
         * @param response
         * @return
         */
        public static SingleUser userAuthor(String response)
        {
                SingleUser singleUser = gsonWithWordpressApiDataFormat.fromJson(response, SingleUser.class);
                return singleUser;
        }

        /**
         * 解析基础回复
         *
         * @param response
         * @return
         */
        public static SingleResponse baseRespond(String response)
        {
                SingleResponse singleResponse = gsonWithWordpressApiDataFormat.fromJson(response, SingleResponse.class);
                return singleResponse;
        }


        /**
         * 解析私信类
         *
         * @param response
         * @return
         */
        public static PrivateMessages privateMessages(String response)
        {
                PrivateMessages privateMessages = gsonWithDataBaseDataFormat.fromJson(response, PrivateMessages.class);
                return privateMessages;
        }


        /**
         * 解析数据为 整数列表
         *
         * @param response
         * @return
         */
        public static List<Integer> integerArrayList(String response)
        {
                return gsonWithWordpressApiDataFormat.fromJson(response, new TypeToken<List<Integer>>()
                {
                }.getType());
        }

        /**
         * 序列化整数列表 为 JSON字符串
         *
         * @param integerList
         * @return
         */
        public static String integerArrayListToJson(List<Integer> integerList)
        {
                return gsonWithWordpressApiDataFormat.toJson(integerList, new TypeToken<List<Integer>>()
                {
                }.getType());
        }


        /**
         *  反序列化字符串为对象
         * @param jsonText
         * @param type
         * @param <T>
         * @return
         */
        public  static <T> T fromJson(String jsonText, Class<T> type){

                T output;
                try
                {
                        //如果用默认的wordpress api日期格式解析错误
                        output = gsonWithWordpressApiDataFormat.fromJson(jsonText, type);
                }
                catch (JsonSyntaxException exception){
                        //改用 数据库日期格式来尝试解析
                        output = gsonWithDataBaseDataFormat.fromJson(jsonText, type);
                }
                return output;
        }





        /**
         * 序列化对象为json字符串
         * @param object
         * @return
         */
        public static String toJson(Object object){

                return gsonWithWordpressApiDataFormat.toJson(object);
        }




}
