package org.mikuclub.app.utils;

import android.os.Build;

import org.mikuclub.app.context.MyApplication;

/**
 * 通过全局context变量获取对应res资源管理器
 * Get res resource manager through the global context
 */
public class ResourcesUtils
{
        /*
        public static Resources get()
        {
                return MyApplication.getContext().getResources();
        }
        */

        /**
         * @param id
         * @return
         */
        public static String getString(int id)
        {
                return MyApplication.getContext().getResources().getString(id);
        }

        /**
         * @param id
         * @return
         */
        public static int getColor(int id)
        {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                        return MyApplication.getContext().getResources().getColor(id, null);
                }
                else{
                        return MyApplication.getContext().getResources().getColor(id);
                }
        }

}
