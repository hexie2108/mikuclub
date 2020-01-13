package org.mikuclub.app.delegates.models;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.util.Map;


/**
 * 资源模型
 */
public class ResourceModel
{
        //资源地址
        private String resource_url;

        public ResourceModel(String resource_url)
        {
                this.resource_url = resource_url;
        }


        /**
         * 根据id获取单个资源
         *
         * @param id
         * @param tag
         * @param httpCallBack
         */
        public void selectById(int id, Map<String, String> params,Map<String, String> headers,  int tag, HttpCallBack httpCallBack)
        {
                String url = resource_url + id;
                Request.get(url, params, headers, tag, httpCallBack);
        }


        /**
         * 获取多个资源
         *  @param params
         * @param headers
         * @param tag
         * @param httpCallBack
         */
        public void selectForList(Map<String, String> params, Map<String, String> headers, int tag, HttpCallBack httpCallBack)
        {

                String url = resource_url;
                Request.get(url, params, headers, tag, httpCallBack);
        }

        /**
         * 插入一个新资源
         *
         * @param params
         * @param tag
         * @param httpCallBack
         */
        public void insert(Map<String, String> params, Map<String, String> bodyParams, Map<String, String> headers, int tag, HttpCallBack httpCallBack)
        {

                String url = resource_url;
                Request.post(url, params, bodyParams, headers, tag, httpCallBack);

        }

        /**
         * 根据id更新一个资源
         *
         * @param params
         * @param tag
         * @param httpCallBack
         */
        public void updateById(int id, Map<String, String> params, Map<String, String> bodyParams, Map<String, String> headers, int tag, HttpCallBack httpCallBack)
        {

                String url = resource_url + id;
                Request.post(url, params, bodyParams, headers, tag, httpCallBack);

        }


        /**
         * 根据id删除一个资源
         *
         * @param id
         * @param tag
         * @param httpCallBack
         */
        public void deleteById(int id, int tag, HttpCallBack httpCallBack)
        {
                String url = resource_url + id;
                Request.delete(url, tag, httpCallBack);
        }


}
