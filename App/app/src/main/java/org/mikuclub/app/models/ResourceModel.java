package org.mikuclub.app.models;

import org.mikuclub.app.callBack.WrapperCallBack;
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
         * @param wrapperCallBack
         */
        public void selectById(int id, Map<String, String> params, int tag, WrapperCallBack wrapperCallBack)
        {
                String url = resource_url + id;
                Request.get(url, params, null, tag, wrapperCallBack);
        }


        /**
         * 获取多个资源
         *
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public void selectForList(Map<String, String> params, int tag, WrapperCallBack wrapperCallBack)
        {

                String url = resource_url;
                Request.get(url, params, null, tag, wrapperCallBack);
        }

        /**
         * 插入一个新资源
         *
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public void insert(Map<String, String> params, int tag, WrapperCallBack wrapperCallBack)
        {

                String url = resource_url;
                Request.post(url, params, null, tag, wrapperCallBack);

        }

        /**
         * 根据id更新一个资源
         *
         * @param params
         * @param tag
         * @param wrapperCallBack
         */
        public void updateById(int id, Map<String, String> params, int tag, WrapperCallBack wrapperCallBack)
        {

                String url = resource_url + id;
                Request.post(url, params, null, tag, wrapperCallBack);

        }


        /**
         * 根据id删除一个资源
         *
         * @param id
         * @param tag
         * @param wrapperCallBack
         */
        public void deleteById(int id, int tag, WrapperCallBack wrapperCallBack)
        {
                String url = resource_url + id;
                Request.delete(url, tag, wrapperCallBack);
        }


}
