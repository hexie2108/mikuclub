package org.mikuclub.app.models;

import android.support.constraint.solver.GoalRow;

import com.google.gson.reflect.TypeToken;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.Post;
import org.mikuclub.app.models.factory.ModelFactory;
import org.mikuclub.app.utils.httpUtils.Connection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * questo classe occupa tutte le comunicazione con APIs tramite HTTP
 */
public class PostModel extends ModelFactory
{
        private static PostModel instance;
        private Type postsListType = new TypeToken<ArrayList<Post>>(){}.getType();


        //singleton mode
        public static synchronized PostModel getInstance()
        {
                if (instance == null)
                {
                        synchronized (PostModel.class)
                        {
                                if (instance == null)
                                {
                                        instance = new PostModel();
                                }
                        }

                }
                return instance;
        }

        /**
         * get la lista di post
         * @param wrapperCallBack
         */
        public void getListPost(String tag,  final WrapperCallBack wrapperCallBack){
                String url = GlobalConfig.SERVER_URL + GlobalConfig.POSTS_URL;
                //Connection.get(url, null, tag,wrapperCallBack);
                Map params = new HashMap();
                params.put("per_page", "2");
                Connection.jsonGet(url,params, Post.class, postsListType, tag,wrapperCallBack);
        }


}
