package com.polytech.montpellier.lifiapp.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.oledcomm.soft.lifiapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Helper {

    private static Helper instance;

    // Instantiate the RequestQueue.
   private RequestQueue queue;
   private String url ="http://www.rifhice.com/LiFiAPI/";

   private Helper(Context context){
       queue = Volley.newRequestQueue(context);
   }

   public static Helper getInstance(Context context){
        if(instance == null){
            instance = new Helper(context);
        }
        return instance;
   }

   public static Helper getInstance(){
       return instance;
   }

    public void GET(String url,  final ResponseHandler res){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET , url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONArray getJSON = new JSONArray(response);
                    res.onSuccess(getJSON);
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.onError(null);
            }
        });
        // Add the request to the RequestQueue.
        this.queue.add(stringRequest);

    }

}