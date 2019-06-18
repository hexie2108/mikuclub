package org.mikuclub.app.utils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class StringUtils
{
        /**
         * transform a map to string
         * @param mapSource source map
         * @param separatorKeyValue  separator between key and value
         * @param separatorEntry separator between entry
         * @return String
         */
        public static String mapToString(Map<String, String> mapSource, String separatorKeyValue, String separatorEntry) throws Exception{

                String output = "";
                Iterator<Map.Entry<String, String>> iterator = mapSource.entrySet().iterator();
                while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = iterator.next();
                        System.out.println(entry.getKey() + "　：" + entry.getValue());
                        output += entry.getKey() + separatorKeyValue + URLEncoder.encode(entry.getValue(), "UTF-8")+separatorEntry;
                }
                //remove the last separatorEntry;
                output = output.substring(0, output.length()-1);
                return output;
        }
}
