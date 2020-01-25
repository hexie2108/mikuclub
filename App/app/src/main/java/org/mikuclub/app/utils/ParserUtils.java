package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.text.StringEscapeUtils;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.AppUpdate;
import org.mikuclub.app.javaBeans.resources.BaseRespond;
import org.mikuclub.app.javaBeans.resources.Categories;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.javaBeans.resources.SingleComment;
import org.mikuclub.app.javaBeans.resources.PrivateMessages;
import org.mikuclub.app.javaBeans.resources.UserAuthor;
import org.mikuclub.app.javaBeans.resources.UserLogin;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.javaBeans.resources.WpError;

import java.util.List;

/**
 * 负责反序列化 和 序列化 数据
 */
public class ParserUtils
{

        //2个gson 解析器配置的日期格式不一样
        private static Gson gson = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();
        private static Gson gson2 = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON_CUSTOM_ENDPOINTS).create();
        /**
         * 解析文章列表
         *
         * @param response
         * @return
         */
        public static Posts posts(String response)
        {

                Posts posts = gson.fromJson(response, Posts.class);

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

                Comments comments = gson.fromJson(response, Comments.class);
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

                SingleComment singleComment = gson.fromJson(response, SingleComment.class);
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

                AppUpdate appUpdate = gson.fromJson(response, AppUpdate.class);
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
                Categories categories = gson.fromJson(response, Categories.class);
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

                UserLogin userLogin = gson.fromJson(response, UserLogin.class);
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
                WpError wpError = gson.fromJson(response, WpError.class);
                return wpError;
        }

        /**
         * 解析作者信息
         *
         * @param response
         * @return
         */
        public static UserAuthor userAuthor(String response)
        {
                UserAuthor userAuthor = gson.fromJson(response, UserAuthor.class);
                return userAuthor;
        }

        /**
         * 解析基础回复
         *
         * @param response
         * @return
         */
        public static BaseRespond baseRespond(String response)
        {
                BaseRespond baseRespond = gson.fromJson(response, BaseRespond.class);
                return baseRespond;
        }


        /**
         * 解析私信类
         *
         * @param response
         * @return
         */
        public static PrivateMessages privateMessages(String response)
        {
                PrivateMessages privateMessages = gson2.fromJson(response, PrivateMessages.class);
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
                return gson.fromJson(response, new TypeToken<List<Integer>>()
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
                return gson.toJson(integerList, new TypeToken<List<Integer>>()
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

                return gson.fromJson(jsonText, type);
        }


        /**
         * 序列化对象为json字符串
         * @param object
         * @return
         */
        public static String toJson(Object object){

                return gson.toJson(object);
        }




}
