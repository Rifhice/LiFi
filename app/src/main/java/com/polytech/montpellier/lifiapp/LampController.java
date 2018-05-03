package com.polytech.montpellier.lifiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kevin on 03/05/2018.
 */

public class LampController extends AppCompatActivity{

    private static LampController instance = null;
    private LampDAO lampDAO = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO();

    private LampController(){

    }

    public static LampController getInstance(){
        if(instance == null){
            instance = new LampController();
        }
        return instance;
    }

    public void onNewLamp(JSONObject lamp) throws JSONException {
        if(UserConnection.getInstance().isConnected()){
            //TODO check if lamp exists, if it doesn't, ask the user if he wants to register it
        }
        else {
            //TODO check if lamp exists, if it does display info, if it doesn't, no behaviour
            System.out.println("\nLiFi reçu: id = " + lamp.toString());
            if(lamp.has("id")) {
                Lamp lampObject = lampDAO.getById(lamp.getInt("id"));
                if(lampObject != null){
                    //TODO update the view
                    Intent intent = new Intent(LampController.this, UserUnderLampView.class);
                    startActivity(intent);
                    System.out.println("It exists");
                }
                else{
                    System.out.println("It doesn't exists");
                }
            }
        }
    }
}
