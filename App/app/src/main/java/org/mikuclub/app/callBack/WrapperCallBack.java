package org.mikuclub.app.callBack;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.contexts.MyApplication;

/*questo è un classe wrapper  per gestire operazione callback di richiesta HTTP*/
public class WrapperCallBack
{
        /**
         * esegue questa funzione se risposta è positiva
         * da Override
         *
         * @param response
         */
        public void onSuccess(Object response)
        {

        }

        /**
         * esegue questa funzione se è successo qualche errore
         * da Override
         */
        public void onErrorHappened()
        {

        }

        //default handle su errore generici non causati dalla connessione HTTP
        public void onOtherError(Exception error)
        {
                onErrorHappened();

                Log.i("TAG", "errore generici: " + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore generici: " + error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
        }

        //default handle globale in caso di errore deriva dalla connessione HTTP
        public void onError(VolleyError error)
        {

                onErrorHappened();

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                //se errore è dovuto da timeout o connessione
                if (networkResponse == null)
                {
                        if (error.getClass().equals(TimeoutError.class))
                        {
                                errorMessage = "Request timeout";
                                handleOnTimeOutError(error);
                        }
                        else if (error.getClass().equals(NoConnectionError.class))
                        {
                                errorMessage = "Failed to connect server";
                                handleOnConnectionError(error);
                        }

                        Log.i("Error", errorMessage);
                }
                //altrimenti
                else
                {
                        //convertire i messaggi di errore in stringa
                        String result = new String(networkResponse.data);
                        try
                        {
                                //convertire in oggetto da JSON string
                                JSONObject response = new JSONObject(result);
                                //get status code
                                int status = response.getJSONObject("data").getInt("status");
                                //get error title
                                String code = response.getString("code");
                                //get error description
                                String message = response.getString("message");

                                Log.e("Error Status", status + "");
                                Log.e("Error Message", code + " : " + message);

                                if (networkResponse.statusCode == 404)
                                {
                                        errorMessage = "Resource not found";
                                        handleOnNotFoundError(error);
                                }
                                else if (networkResponse.statusCode == 401)
                                {
                                        errorMessage = message + " Please login again";
                                        handleOnUnauthorizedError(error);
                                }
                                else if (networkResponse.statusCode == 400)
                                {
                                        errorMessage = message + " Check your inputs";
                                        handleOnBadRequestError(error);

                                }
                                else if (networkResponse.statusCode == 500)
                                {
                                        errorMessage = message + " Something is getting wrong";
                                        handleOnInternalServerError(error);
                                }
                        }
                        catch (JSONException e)
                        {
                                e.printStackTrace();
                        }
                }
                error.printStackTrace();
        }

        public void handleOnTimeOutError(VolleyError error)
        {
                Log.i("TAG", "errore timeOut:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore timeOut: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnConnectionError(VolleyError error)
        {
                Log.i("TAG", "errore connection:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore connection: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }


        public void handleOnNotFoundError(VolleyError error)
        {
                Log.i("TAG", "errore notFound:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore notFound: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnUnauthorizedError(VolleyError error)
        {
                Log.i("TAG", "errore Unauthorized:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore Unauthorized: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnBadRequestError(VolleyError error)
        {
                Log.i("TAG", "errore BadRequest:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore BadRequest: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void handleOnInternalServerError(VolleyError error)
        {
                Log.i("TAG", "errore InternalServerError:" + error.getMessage(), error);
                Toast.makeText(MyApplication.getContext(), "errore InternalServerError: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }


}
