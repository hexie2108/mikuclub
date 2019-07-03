package org.mikuclub.app.utils.dataStructure;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ArrayUtils
{
        /**
         * transform a arrayList to string
         *
         * @param arraylistSource  source list
         * @param symbolAround     symbol around the element
         * @param separatorElement separator between element
         * @return String se lista non è vuoto, altrimenti ritorna null
         */
        public static String arrayListToString(ArrayList arraylistSource, String symbolAround, String separatorElement)
        {

                String output = null;
                //se list è definito e contiene almeno un elemento
                if (arraylistSource != null && arraylistSource.size() > 0)
                {
                        output = "";
                        for (int i = 0; i < arraylistSource.size(); i++)
                        {
                                output += symbolAround + String.valueOf(arraylistSource.get(i)) + symbolAround + separatorElement;
                        }
                        //remove the last separatorElement;
                        output = output.substring(0, output.length() - separatorElement.length());

                }
                return output;
        }


}
