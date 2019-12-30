package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.AppUpdate;
import org.mikuclub.app.javaBeans.WordpressError;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;

public class Parser
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




        public static WordpressError wordpressError(String response) throws JsonParseException
        {
                WordpressError wordpressError = gson.fromJson(response, WordpressError.class);
                return wordpressError;

        }


}
