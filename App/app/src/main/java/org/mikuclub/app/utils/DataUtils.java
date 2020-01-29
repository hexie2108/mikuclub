package org.mikuclub.app.utils;

import org.mikuclub.app.config.GlobalConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 结构化数据的实用处理类
 * Structured data utility class
 */
public class DataUtils
{
        /**
         * 把 arrayList 转换成 字符串
         *
         * @param arraylistSource  source list
         * @param symbolAround     symbol around the element
         * @param separatorElement separator between element
         * @return
         */
        public static String arrayListToString(ArrayList arraylistSource, String symbolAround, String separatorElement)
        {

                String output = null;
                //如果列表不是空的
                if (arraylistSource != null && !arraylistSource.isEmpty())
                {
                        output = "";
                        for (int i = 0; i < arraylistSource.size(); i++)
                        {
                                output += symbolAround + arraylistSource.get(i) + symbolAround + separatorElement;
                        }
                        //去除末尾的分隔符号
                        output = output.substring(0, output.length() - separatorElement.length());

                }
                return output;
        }


        /**
         * 把 map 转换成 字符串
         *
         * @param mapSource         source map
         * @param separatorKeyValue separator between key and value
         * @param separatorEntry    separator between entry
         * @return String
         */
        public static String mapToString(Map<String, Object> mapSource, String separatorKeyValue, String separatorEntry)
        {

                String output = "";
                Iterator<Map.Entry<String, Object>> iterator = mapSource.entrySet().iterator();
                try
                {

                        while (iterator.hasNext())
                        {
                                Map.Entry<String, Object> entry = iterator.next();
                                //System.out.println(entry.getKey() + "　：" + entry.getValue());
                                output += entry.getKey() + separatorKeyValue + URLEncoder.encode(entry.getValue().toString(), "UTF-8") + separatorEntry;
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
        public static void putIfNotNull(Map map, String key, Object inputValue)
        {
                //首先必须不是null
                if (inputValue != null)
                {
                        String value;

                        //如果是布尔类型
                        if (inputValue instanceof Boolean)
                        {
                                //转换成boolean
                                int BooleanValue = (Boolean) inputValue ? 1 : 0; //需要把boolean转换成 1和 0, 不然api后台会把 true和false认为是字符串
                                //转化成字符串
                                value = String.valueOf(BooleanValue);
                                map.put(key, value);
                        }
                        //如果是整数
                        else if (inputValue instanceof Integer)
                        {
                                value = String.valueOf(inputValue);
                                map.put(key, value);
                        }
                        //如果是字符串
                        else if (inputValue instanceof String)
                        {
                                value = (String) inputValue;
                                //字符串长度必须大于0
                                if (value.length() > 0)
                                {
                                        map.put(key, value);
                                }
                        }
                        //如果是其他类型
                        else{
                                map.put(key, inputValue);
                        }

                }
        }


        /**
         * 把日期变量转换成预设的字符串格式
         *
         * @param date
         * @return
         */
        public static String dateToString(Date date)
        {
                String output = null;
                //se l'oggetto data non è nullo
                if (date != null)
                {
                        SimpleDateFormat formatter = new SimpleDateFormat(GlobalConfig.DATE_FORMAT_JSON);
                        output = formatter.format(date);
                }
                return output;
        }
}
