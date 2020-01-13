package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.AppUpdate;
import org.mikuclub.app.javaBeans.resources.Categories;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.javaBeans.resources.CreateComment;
import org.mikuclub.app.javaBeans.resources.UserLogin;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.javaBeans.resources.WpError;

import java.util.ArrayList;
import java.util.List;

import mikuclub.app.BuildConfig;

/**
 * 负责反序列化 和 序列化 数据
 */
public class ParserUtils
{

        private static Gson gson = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();

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
        public static CreateComment createComment(String response)
        {

                CreateComment createComment = gson.fromJson(response, CreateComment.class);
                return createComment;

        }

        /**
         * 解析软件更新信息
         *
         * @param response
         * @return
         */
        public static AppUpdate appUpdate(String response)
        {

                AppUpdate  appUpdate = gson.fromJson(response, AppUpdate.class);
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
}
