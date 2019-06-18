package org.mikuclub.app.utils.httpUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.models.PostModel;

/**
 * questo classe serve per mantenere un singolo istanza di Gson per l'intero progetto.
 */
public class GsonInstance
{
        private static Gson instance;

        //singleton mode
        public static synchronized Gson getInstance()
        {
                if (instance == null)
                {
                        synchronized (Gson.class)
                        {
                                if (instance == null)
                                {
                                        instance = new GsonBuilder().setDateFormat(GlobalConfig.DATE_FORMAT_JSON).create();
                                }
                        }

                }
                return instance;
        }
}
