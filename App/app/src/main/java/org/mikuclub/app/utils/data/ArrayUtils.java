package org.mikuclub.app.utils.data;

import java.util.ArrayList;

/**
 * 列表数据处理函数集
 */
public class ArrayUtils
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


}
