package org.mikuclub.app.utils.dataStructure;

import java.util.HashMap;
import java.util.Map;

public class ParametersMap extends HashMap
{

        /**
         * metodo wrapper di map.put
         * put elemente  solo se il valore non è nullo
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
