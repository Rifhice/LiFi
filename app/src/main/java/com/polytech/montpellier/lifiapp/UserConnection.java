package com.polytech.montpellier.lifiapp;

import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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

        String url = MainActivity.url + "Auth" ;
       //TODO getResources().getString(R.string.url)

        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);


        Helper.getInstance().POST(url, token, params,  new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONObject){

                   try {
                       JSONObject json = (JSONObject) object;

                       if (json.getInt("code") == 200){
                           setToken(json.getString("token"));
                           responseHandler.onSuccess(object);
                       }
                       else {
                           responseHandler.onError(null);
                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                       responseHandler.onError(null);
                   }

               }

            }

            @Override
            public void onError(Object object) {
                responseHandler.onError(null);

            }
        });
    }

    public void changePassword(String newPass, final ResponseHandler res){
        String url = MainActivity.url + "Auth" ;
        //TODO getResources().getString(R.string.url)

        Map<String, String> params = new HashMap<String, String>();
        params.put("password", newPass);


        Helper.getInstance().PUT(url, token, params,  new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONObject){
                    try {
                        JSONObject json = (JSONObject) object;

                        if (json.getInt("code") == 200){
                            setToken(json.getString("token"));
                            res.onSuccess(object);
                        }
                        else {
                            res.onError(null);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        res.onError(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                res.onError(null);
            }
        });
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
