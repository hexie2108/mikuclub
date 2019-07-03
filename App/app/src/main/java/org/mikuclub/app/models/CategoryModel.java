package org.mikuclub.app.models;

import com.google.gson.reflect.TypeToken;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Categories;
import org.mikuclub.app.javaBeans.resources.Category;
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
public class CategoryModel extends ModelFactory
{
        private static CategoryModel instance;
        private Type postsListType = new TypeToken<ArrayList<Category>>()
        {
        }.getType();


        //singleton mode
        public static synchronized CategoryModel getInstance()
        {
                if (instance == null)
                {
                        synchronized (CategoryModel.class)
                        {
                                if (instance == null)
                                {
                                        instance = new CategoryModel();
                                }
                        }

                }
                return instance;
        }


        /**
         * get un singolo catogoria
         *
         * @param id
         * @param tag
         * @param wrapperCallBack
         */
        public void selectById(int id, Map<String, String> params, String tag, WrapperCallBack wrapperCallBack)
        {
                String url = GlobalConfig.SERVER_URL + GlobalConfig.CATEGORIES_URL + id;
                Connection.jsonGet(url, params, null, Category.class, null, tag, wrapperCallBack);
        }


        /**
         * get una lista di categoria
         *
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public void selectForList(Map<String, String> params, String tag, WrapperCallBack wrapperCallBack)
        {

                String url = GlobalConfig.SERVER_URL + GlobalConfig.CATEGORIES_URL;
                Connection.jsonGet(url, params, null,Categories.class, null, tag, wrapperCallBack);
        }

        /**
         * inserisce un nuovo categoria
         *
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public void insert(Map<String, String> params, String tag, WrapperCallBack wrapperCallBack)
        {

                String url = GlobalConfig.SERVER_URL + GlobalConfig.CATEGORIES_URL;
                Connection.jsonPost(url, params, null, Category.class, null, tag, wrapperCallBack);

        }

        /**
         * update un commento esistente
         *
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public void updateById(int id, Map<String, String> params, String tag, WrapperCallBack wrapperCallBack)
        {

                String url = GlobalConfig.SERVER_URL + GlobalConfig.CATEGORIES_URL + id;
                Connection.jsonPost(url, params, null, Category.class, null, tag, wrapperCallBack);

        }


        /**
         * delete un singolo categoria
         *
         * @param id
         * @param tag
         * @param wrapperCallBack
         */
        public void deleteById(int id, String tag, WrapperCallBack wrapperCallBack)
        {
                String url = GlobalConfig.SERVER_URL + GlobalConfig.CATEGORIES_URL + id;
                Connection.delete(url, tag, wrapperCallBack);
        }


}
