package org.mikuclub.app.models;

import com.google.gson.reflect.TypeToken;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.javaBeans.resources.User;
import org.mikuclub.app.javaBeans.resources.Users;
import org.mikuclub.app.models.factory.ModelFactory;
import org.mikuclub.app.utils.httpUtils.Connection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * questo classe occupa tutte le comunicazione con APIs tramite HTTP
 */
public class UserModel extends ModelFactory {
    private static UserModel instance;
    private Type postsListType = new TypeToken<ArrayList<User>>() {
    }.getType();


    //singleton mode
    public static synchronized UserModel getInstance() {
        if (instance == null) {
            synchronized (UserModel.class) {
                if (instance == null) {
                    instance = new UserModel();
                }
            }

        }
        return instance;
    }


    /**
     * get un singolo comment
     *
     * @param id
     * @param tag
     * @param wrapperCallBack
     */
    public void selectById(int id, Map<String,String> params, String tag, WrapperCallBack wrapperCallBack) {
        String url = GlobalConfig.SERVER_URL + GlobalConfig.USERS_URL + id;
        Connection.jsonGet(url, params, null, User.class, null, tag, wrapperCallBack);
    }


    /**
     * get una lista di commento
     *
     * @param params
     * @param tag
     * @param wrapperCallBack
     */
    public void selectForList(Map<String, String> params, String tag, WrapperCallBack wrapperCallBack) {

        String url = GlobalConfig.SERVER_URL + GlobalConfig.USERS_URL;
        Connection.jsonGet(url, params, null, Users.class, null, tag, wrapperCallBack);
    }

    /**
     * inserisce un nuovo commento
     *
     * @param params
     * @param tag
     * @param wrapperCallBack
     */
    public void insert(Map<String, String> params, String tag, WrapperCallBack wrapperCallBack) {

        String url = GlobalConfig.SERVER_URL + GlobalConfig.USERS_URL;
        Connection.jsonPost(url, params, null, User.class, null, tag, wrapperCallBack);

    }

    /**
     * update un commento esistente
     *
     * @param params
     * @param tag
     * @param wrapperCallBack
     */
    public void updateById(int id, Map<String, String> params, String tag, WrapperCallBack wrapperCallBack) {

        String url = GlobalConfig.SERVER_URL + GlobalConfig.USERS_URL + id;
        Connection.jsonPost(url, params, null, User.class, null, tag, wrapperCallBack);

    }


    /**
     * delete un singolo commento
     *
     * @param id
     * @param tag
     * @param wrapperCallBack
     */
    public void deleteById(int id, String tag, WrapperCallBack wrapperCallBack) {
        String url = GlobalConfig.SERVER_URL + GlobalConfig.USERS_URL + id;
        Connection.delete(url, tag, wrapperCallBack);
    }


}
