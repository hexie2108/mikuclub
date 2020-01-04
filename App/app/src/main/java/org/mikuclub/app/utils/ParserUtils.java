package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.AppUpdate;
import org.mikuclub.app.javaBeans.WordpressError;
import org.mikuclub.app.javaBeans.resources.Categories;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;

/**
 * 负责反序列化 和 序列化 数据
 */
public class ParserUtils
{

        private static Gson gson = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();

        /**
         * 解析文章列表
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
                        String unescapeText =  StringEscapeUtils.unescapeHtml4(posts.getBody().get(i).getTitle().getRendered());
                        //恢复被html转义的字符
                        posts.getBody().get(i).getTitle().setRendered(unescapeText);
                }


                return posts;

        }

        /**
         * 解析评论列表
         * @param response
         * @return
         */
        public static Comments comments(String response)
        {

                Comments comments = gson.fromJson(response, Comments.class);
                return comments;

        }

        /**
         * 解析软件更新信息
         * @param response
         * @return
         */
        public static AppUpdate appUpdate(String response)
        {

                AppUpdate appUpdate = null;
               try
               {
                       appUpdate =    gson.fromJson(response, AppUpdate.class);
               }
               catch (Exception e){
                       //屏蔽可能的解析错误, 避免程序崩溃
                       e.printStackTrace();
               }
                return appUpdate;
        }


        /**
         * 解析分类菜单
         * @param response
         * @return
         */
        public static Categories categories(String response)
        {

                Categories categories = gson.fromJson(response, Categories.class);
                return categories;

        }

        /**
         * 把 分类菜单类 序列化为字符串
         * @param categories
         * @return
         */
        public static String categoriesToJson(Categories categories)
        {

                String categoriesString = gson.toJson(categories, Categories.class);
                return categoriesString;

        }




}
