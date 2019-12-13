package org.mikuclub.app.utils.data;

import java.util.HashMap;

/**
 * 自定义map类
 */
public class ParametersMap extends HashMap
{

        /**
         * 自定义插入方法
         * 只有数据不是null的情况下 才插入数据
         * @param key
         * @param inputValue
         */
        public void  putIfnotNull(String key, Object inputValue){
                if( inputValue != null){
                        String value = String.valueOf(inputValue);
                        super.put(key, value);
                }
        }
}
