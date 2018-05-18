package com.polytech.montpellier.lifiapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        if(AdminActivity.isIsDisplayed() && UserConnection.getInstance().isConnected()){
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
            System.out.println("\nLiFi reçu: id = " + lamp.toString());
            if(lamp.has("id")) {
                lampDAO.getById(lamp.getInt("id"), new ResponseHandler() {
                    @Override
                    public void onSuccess(Object object) {
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
