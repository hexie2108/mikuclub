package org.mikuclub.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.mikuclub.app.config.GlobalConfig;

import java.util.List;

/**
 * 数据解析器
 * 负责反序列化和序列化数据
 * Data parser
 * Responsible for deserializing and serializing data
 */
public class ParserUtils
{

        //2个gson 解析器支持的日期格式不一样
        //第一个支持 wordpress官方接口的日期格式
        private static Gson gsonWithWordpressApiDataFormat = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();
        // 第二个支持 wordpress自定义接口的日期格式
        private static Gson gsonWithDataBaseDataFormat = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON_CUSTOM_ENDPOINTS).create();


        /**
         * 解析数据为 整数列表
         *
         * @param response
         * @return
         */
        public static List<Integer> integerArrayList(String response)
        {
                return gsonWithWordpressApiDataFormat.fromJson(response, new TypeToken<List<Integer>>()
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
                return gsonWithWordpressApiDataFormat.toJson(integerList, new TypeToken<List<Integer>>()
                {
                }.getType());
        }


        /**
         * 反序列化字符串为对象
         *
         * @param jsonText
         * @param type
         * @param <T>
         * @return
         */
        public static <T> T fromJson(String jsonText, Class<T> type)
        {

                T output;
                try
                {
                        //如果用默认的wordpress api日期格式解析错误
                        output = gsonWithWordpressApiDataFormat.fromJson(jsonText, type);
                }
                catch (JsonSyntaxException e)
                {
                        try
                        {
                                //改用 数据库日期格式来尝试解析
                                output = gsonWithDataBaseDataFormat.fromJson(jsonText, type);
                        }
                        catch (JsonSyntaxException exception)
                        {
                                LogUtils.e("GSON解析错误: " + jsonText);
                                throw exception;
                        }
                }
                return output;
        }


        /**
         * 序列化对象为json字符串
         *
         * @param object
         * @return
         */
        public static String toJson(Object object)
        {
                return gsonWithWordpressApiDataFormat.toJson(object);
        }


}
