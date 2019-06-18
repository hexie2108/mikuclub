package org.mikuclub.app.callBack;

import android.util.Log;

import com.android.volley.VolleyError;

/*questo è un classe wrapper  per gestire operazione callback di richiesta HTTP*/
public class WrapperCallBack
{
        //funzione da Override
        public void onSuccess(Object response){

        }

       public void onError(Exception error){
               Log.e("TAG", error.getMessage(), error);
       }
}
