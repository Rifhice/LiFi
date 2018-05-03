package com.polytech.montpellier.lifiapp.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oledcomm.soft.lifiapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Helper {

    private static Helper instance;

    // Instantiate the RequestQueue.
   private RequestQueue queue;
   private String url ="http://www.rifhice.com/LiFiAPI/";

   public Helper(Context context){
       queue = Volley.newRequestQueue(context);
   }

    public JSONObject GET(String url, String token){

        //final TextView mTextView = (TextView) mTextView.findViewById();
        final JSONObject getJson = null;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET , url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                       try {
                           JSONObject getJsonw = new JSONObject(response);
                           System.out.println(getJsonw);

                       } catch (Throwable t) {
                           Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                       }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    getJson.put("error", "That didn't work!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add the request to the RequestQueue.
        this.queue.add(stringRequest);
        return getJson ;
    }




}