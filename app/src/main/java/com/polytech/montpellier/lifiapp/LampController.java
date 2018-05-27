package com.polytech.montpellier.lifiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Admin.AdminActivity;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LampController extends AppCompatActivity {

    final Context context = this;
    private static LampController instance = null;

    //DAO declaration
    private LampDAO lampDAO = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getLampDAO();

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
        //If the admin page is open and the user is connected as admin, open a pop up to add a new lamp.
        if(AdminActivity.isIsDisplayed() && UserConnection.getInstance().isConnected()){
            //Ask the DAO the lamp with the id given in parameter.
            lampDAO.getById(lamp.getInt("id"), new ResponseHandler() {
                //Function triggered on success from the request.
                @Override
                public void onSuccess(Object object) {
                    //If the lamp already exist in the database, there is no need to
                    //ask the user to add it
                    if(object != null){

                    }
                    //The lamp doesn't exist so we open a pop to the user
                    else{
                        try {
                            AdminActivity.getInstance().openNewLampPopUp(lamp.getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //Function triggered when an error occurred with the request.
                @Override
                public void onError(Object object) {
                    new AlertDialog.Builder(context)
                            .setTitle(getResources().getString(R.string.error))
                            .setMessage(getResources().getString(R.string.erroroccured))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                }}).show();
                }
            });
        }
        //If the user is not connected or not on the admin page we display the received lamp
        else {
            if(lamp.has("id")) {
                //Ask the DAO the lamp with the id given in parameter.
                lampDAO.getById(lamp.getInt("id"), new ResponseHandler() {
                    //Function trigered on success from the request.
                    @Override
                    public void onSuccess(Object object) {
                        //If the lamp exist we display it's details
                        if(object != null){
                            Intent intent = new Intent(context, UserUnderLampView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ArrayList<Lamp> array = (ArrayList<Lamp>)object;
                            Lamp lamp = array.get(0);

                            intent.putExtra("lamp", lamp.getId());
                            intent.putExtra("lampName",lamp.getName());
                            if(lamp.getDepartment() != null){
                                intent.putExtra("lampDep",lamp.getDepartment().getId());
                                intent.putExtra("lampDepName",lamp.getDepartment().getName());
                            }
                            context.startActivity(intent);
                        }
                        //If it doesn't exist, we aren't doing anything
                        else{

                        }
                    }
                    //Function triggered when an error occurred with the request.
                    @Override
                    public void onError(Object object) {
                        new AlertDialog.Builder(context)
                                .setTitle(getResources().getString(R.string.error))
                                .setMessage(getResources().getString(R.string.erroroccured))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }}).show();
                    }
                });

            }
        }
    }


}
