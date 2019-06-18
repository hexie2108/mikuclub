package org.mikuclub.app.models.factory;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.utils.httpUtils.Connection;

/**
 * questo classe occupa tutte le comunicazione con APIs tramite HTTP
 */
public class ModelFactory
{

        /**
         * cancella la richiesta HTTP in corso
         * @param tag
         */
        public void cancelRequest(String tag){
                Connection.cancelRequest(tag);
        }


}
