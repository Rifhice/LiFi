package com.polytech.montpellier.lifiapp;

import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Kevin on 03/05/2018.
 */

public class UserConnection {

    private static UserConnection instance = null;
    private String token = null;

    private UserConnection(){

    }

    public static UserConnection getInstance(){
        if(instance == null){
            instance = new UserConnection();
        }
        return instance;
    }
    
    public void login(String password, final ResponseHandler responseHandler){

        String url = "http://www.rifhice.com/LiFiAPI/Auth" ;
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);


        Helper.getInstance().POST(url, token, params,  new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("login  " + object);
                if (object instanceof JSONObject){

                   try {
                       JSONObject json = (JSONObject) object;
                       System.out.println("login result " + object);

                       if (json.getString("result").equals("success")){
                           setToken(json.getString("token"));

                           responseHandler.onSuccess(null);

                       }
                       else {
                           responseHandler.onError(null);
                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                       responseHandler.onError(null);
                       System.out.println("login error " );

                   }

               }

            }

            @Override
            public void onError(Object object) {
                System.out.println("error login " + object.toString());
                responseHandler.onError(null);

            }
        });


        System.out.println(password);
    }

    public boolean isConnected(){
        return token != null;
    }

    public String getToken(){
        return token;
    }
    private void setToken(String token){
        this.token = token;
    }


}
