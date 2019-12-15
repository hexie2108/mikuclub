package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;

public class Parser
{

        private static Gson gson = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();

        public static Posts posts(String response)
        {

                Posts posts = gson.fromJson(response, Posts.class);
                return posts;

        }


}
