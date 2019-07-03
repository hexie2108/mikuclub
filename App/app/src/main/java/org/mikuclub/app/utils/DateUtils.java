package org.mikuclub.app.utils;

import org.mikuclub.app.configs.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class DateUtils
{

        /**
         * trasformare print data in formatta prestabilita dalla config
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
