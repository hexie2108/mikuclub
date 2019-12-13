package org.mikuclub.app.utils.data;

import org.mikuclub.app.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * map数据 处理函数集
 */
public class MapUtils
{
        /**
         * 把 map 转换成 字符串
         *
         * @param mapSource         source map
         * @param separatorKeyValue separator between key and value
         * @param separatorEntry    separator between entry
         * @return String
         */
        public static String mapToString(Map<String, String> mapSource, String separatorKeyValue, String separatorEntry)
        {

                String output = "";
                Iterator<Map.Entry<String, String>> iterator = mapSource.entrySet().iterator();
                try
                {


                        while (iterator.hasNext())
                        {
                                Map.Entry<String, String> entry = iterator.next();
                                //System.out.println(entry.getKey() + "　：" + entry.getValue());
                                output += entry.getKey() + separatorKeyValue + URLEncoder.encode(entry.getValue(), "UTF-8") + separatorEntry;
                        }
                }
                catch (UnsupportedEncodingException e)
                {

                        LogUtils.w("MAP编码转换字符串错误");
                        e.printStackTrace();
                }
                //remove the last separatorEntry;
                output = output.substring(0, output.length() - separatorEntry.length());
                return output;
        }

        /**
         * map自定义插入方法
         * 只有在内容不为空的情况下 插入数据
         *
         * @param map
         * @param key
         * @param inputValue
         */
        public static void putIfnotNull(Map<String, String> map, String key, Object inputValue)
        {
                //必须不是null
                if (inputValue != null)
                {
                        String value;

                        //如果是布尔类型
                        if (inputValue instanceof Boolean)
                        {
                                //转换成int
                                int BooleanValue = (Boolean) inputValue ? 1 : 0;
                                //转化成字符串
                                value = String.valueOf(BooleanValue);
                        }
                        else
                        {
                                value = String.valueOf(inputValue);
                        }
                        //字符串长度必须大于0
                        if (value.length() > 0)
                        {
                                map.put(key, value);
                        }

                }
        }

}
