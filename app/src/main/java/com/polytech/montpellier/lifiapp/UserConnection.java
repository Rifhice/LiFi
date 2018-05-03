package com.polytech.montpellier.lifiapp;

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
    
    public boolean login(String password){
        //TODO faire l'appel en post a rifhice.com/LiFiAPI/Auth puis regarder le retour si le retour est bon, set le token a la valeur renvoyer par l'api
        System.out.println(password);
        return true;
    }

    public boolean isConnected(){
        return token != null;
    }

    public String getToken(){
        return token;
    }
}
