package mikuclub.app.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import mikuclub.app.activities.MainActivity;

public class Connection
{
        private static RequestQueue requestQueue;

        public static String get(Context context, String url)
        {
                requestQueue = Volley.newRequestQueue(context);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                                @Override
                                public void onResponse(String response)
                                {
                                        Log.d("TAG", response);

                                }
                        }, new Response.ErrorListener()
                {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                                Log.e("TAG", error.getMessage(), error);
                        }
                });

                return "ee";
        }


}
