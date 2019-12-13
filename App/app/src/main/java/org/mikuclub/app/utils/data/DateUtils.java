package org.mikuclub.app.utils.data;

import org.mikuclub.app.configs.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期数据处理函数集
 */
public class DateUtils
{

        /**
         * 把日期实例转换成预设的字符串格式
         * @param date
         * @return
         */
        public static String dateToString(Date date)
        {
                String output = null;
                //se l'oggetto data non è nullo
                if(date != null)
                {
                        SimpleDateFormat formatter = new SimpleDateFormat(GlobalConfig.DATE_FORMAT_JSON);
                        output =  formatter.format(date);
                }
                return output;
        }
}
