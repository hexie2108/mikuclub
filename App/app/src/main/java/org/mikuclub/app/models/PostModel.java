package org.mikuclub.app.models;

import com.google.gson.reflect.TypeToken;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.models.factory.ModelFactory;
import org.mikuclub.app.utils.httpUtils.Connection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * questo classe occupa tutte le comunicazione con APIs tramite HTTP
 */
public class PostModel extends ModelFactory {
    private static PostModel instance;
    private Type postsListType = new TypeToken<ArrayList<Post>>() {
    }.getType();


    //singleton mode
    public static synchronized PostModel getInstance() {
        if (instance == null) {
            synchronized (PostModel.class) {
                if (instance == null) {
                    instance = new PostModel();
                }
            }

        }
        return instance;
    }


    /**
     * get un singolo post
     *
     * @param id
     * @param tag
     * @param wrapperCallBack
     */
    public void selectById(int id, Map<String,String> params, String tag, WrapperCallBack wrapperCallBack) {
        String url = GlobalConfig.SERVER_URL + GlobalConfig.POSTS_URL + id;
        Connection.jsonGet(url, params, null, Post.class, null, tag, wrapperCallBack);
    }


    /**
     * get una lista di post
     *
     * @param params
     * @param tag
     * @param wrapperCallBack
     */
    public void selectForList(Map<String, String> params, String tag, WrapperCallBack wrapperCallBack) {

        String url = GlobalConfig.SERVER_URL + GlobalConfig.POSTS_URL;
        Connection.jsonGet(url, params, null, Posts.class, null, tag, wrapperCallBack);
    }

    /**
     * inserisce un nuovo post
     *
     * @param params
     * @param tag
     * @param wrapperCallBack
     */
    public void insert(Map<String, String> params, String tag, WrapperCallBack wrapperCallBack) {

        String url = GlobalConfig.SERVER_URL + GlobalConfig.POSTS_URL;
        Connection.jsonPost(url, params, null, Post.class, null, tag, wrapperCallBack);

    }

    /**
     * update un post esistente
     *
     * @param params
     * @param tag
     * @param wrapperCallBack
     */
    public void updateById(int id, Map<String, String> params, String tag, WrapperCallBack wrapperCallBack) {

        String url = GlobalConfig.SERVER_URL + GlobalConfig.POSTS_URL + id;
        Connection.jsonPost(url, params, null, Post.class, null, tag, wrapperCallBack);
    }

    /**
     * delete un singolo post
     *
     * @param id
     * @param tag
     * @param wrapperCallBack
     */
    public void deleteById(int id, String tag, WrapperCallBack wrapperCallBack) {
        String url = GlobalConfig.SERVER_URL + GlobalConfig.POSTS_URL + id;
        Connection.delete(url, tag, wrapperCallBack);
    }






}
