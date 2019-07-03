package org.mikuclub.app.utils.dataStructure;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class MapUtils
{
        /**
         * transform a map to string
         *
         * @param mapSource         source map
         * @param separatorKeyValue separator between key and value
         * @param separatorEntry    separator between entry
         * @return String
         */
        public static String mapToString(Map<String, String> mapSource, String separatorKeyValue, String separatorEntry) throws Exception
        {

                String output = "";
                Iterator<Map.Entry<String, String>> iterator = mapSource.entrySet().iterator();
                while (iterator.hasNext())
                {
                        Map.Entry<String, String> entry = iterator.next();
                        //System.out.println(entry.getKey() + "　：" + entry.getValue());
                        output += entry.getKey() + separatorKeyValue + URLEncoder.encode(entry.getValue(), "UTF-8") + separatorEntry;
                }
                //remove the last separatorEntry;
                output = output.substring(0, output.length() - separatorEntry.length());
                return output;
        }

        /**
         * metodo wrapper di map.put
         * put elemente nel map solo se il valore non è nullo
         *
         * @param map
         * @param key
         * @param inputValue
         */
        public static void putIfnotNull(Map<String, String> map, String key, Object inputValue)
        {
                //controlla se non è nullo
                if (inputValue != null)
                {
                        String value;
                        //se è un tipo booleano
                        if(inputValue instanceof Boolean){
                                //converte in numero intero
                                int BooleanValue = (Boolean) inputValue ? 1 : 0;
                                value = String.valueOf(BooleanValue);
                        }
                        else{
                                value = String.valueOf(inputValue);
                        }
                        //controlla se è maggiore di 0
                        if (value.length() > 0)
                        {
                                map.put(key, value);
                        }

                }
        }

}
