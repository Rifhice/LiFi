package com.polytech.montpellier.lifiapp.Helper;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
//import com.oledcomm.soft.lifiapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

   public static Helper getInstance(MySqlDiscountDAO mySqlDiscountDAO){
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
                    res.onSuccess(null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.onError(error);
        }
        });
        // Add the request to the RequestQueue.
        this.queue.add(stringRequest);

    }


    public void POST(String url , final String token, Map<String, String> params,  final ResponseHandler res ){

        params.put("token" , token);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST , url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            res.onSuccess(response);

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.onError(error);
            }
        });

        this.queue.add(jsonObjectRequest);
    }

    public void PUT(String url , final String token, Map<String, String> params,  final ResponseHandler res ){

        params.put("token" , token);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT , url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            res.onSuccess(response);

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.onError(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                // add headers <key,value>
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.queue.add(jsonObjectRequest);
    }


    public void DELETE(String url, final String token ,  final ResponseHandler res){
       Map<String,String> params = new HashMap<String,String>();
        params.put("token" , token);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE , url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        res.onSuccess(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res.onError(error);
            }
        });

        // Add the request to the RequestQueue.
        this.queue.add(jsonObjectRequest);

    }
}




