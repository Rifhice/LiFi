package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kevin on 03/05/2018.
 */

public class LampController extends AppCompatActivity {

    private static LampController instance = null;
    private LampDAO lampDAO = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO();

    private LampController(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp_controller);
    }

    public static LampController getInstance(){
        if(instance == null){
            instance = new LampController();
        }
        return instance;
    }

    public void onNewLamp(final JSONObject lamp, final Context context) throws JSONException {
        System.out.println("IS INSTANCE : " + (context.getApplicationContext() instanceof AdminActivity));
        if(context.getApplicationContext() instanceof AdminActivity){
            //TODO check if lamp exists, if it doesn't, ask the user if he wants to register it
            lampDAO.getById(lamp.getInt("id"), new ResponseHandler() {
                @Override
                public void onSuccess(Object object) {
                    if(object != null){
                        System.out.println("It exists");
                    }
                    else{
                        try {
                            AdminActivity.getInstance().openNewLampPopUp(lamp.getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Object object) {

                }
            });
        }
        else {
            //TODO check if lamp exists, if it does display info, if it doesn't, no behaviour
            System.out.println("\nLiFi reçu: id = " + lamp.toString());
            if(lamp.has("id")) {
                lampDAO.getById(lamp.getInt("id"), new ResponseHandler() {
                    @Override
                    public void onSuccess(Object object) {
                        if(object != null){
                            //TODO update the view
                            Intent intent = new Intent(context, UserUnderLampView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Lamp lamp = (Lamp)object;
                            intent.putExtra("lamp",lamp.getId());
                            intent.putExtra("lampName",lamp.getName());
                            intent.putExtra("lampDep",lamp.getDepartment().getId());


                            //TODO Add lamp as parameter
                            context.startActivity(intent);
                            System.out.println("It exists");
                        }
                        else{
                            System.out.println("It doesn't exists");
                        }
                    }

                    @Override
                    public void onError(Object object) {

                    }
                });

            }
        }
    }
}
