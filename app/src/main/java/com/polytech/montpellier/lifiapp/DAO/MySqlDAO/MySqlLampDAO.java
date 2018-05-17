package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import android.os.AsyncTask;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.Model.Product;
import com.polytech.montpellier.lifiapp.UserConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlLampDAO extends LampDAO {

    @Override
    public void create(Lamp obj,String token, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("idLamp", obj.getId() + "");
        params.put("name", obj.getName());
        params.put("idDepartment",  String.valueOf(obj.getDepartment().getId()));

        Helper.getInstance().POST("http://81.64.139.113:1337/api/Lamp",token , params, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                response.onSuccess(object);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    public void getById(final int id, final ResponseHandler response) throws DAOException {
        final ArrayList<Lamp> lamp =  new ArrayList<Lamp>();

        Helper.getInstance().GET("http://81.64.139.113:1337/api/Lamp/" + id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {

                        JSONObject current = null;
                        Object iddep = null;
                        try {
                            current = array.getJSONObject(0);
                            iddep = current.optInt("idDepartment");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(iddep == null){
                                lamp.add(new Lamp(current.getInt("idLamp"), current.getString("nameLamp"), null));
                            }
                            else{
                                lamp.add(new Lamp(current.getInt("idLamp"), current.getString("nameLamp"), new Department(current.optInt("idDepartment"), current.getString("nameDepartment"))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                            response.onSuccess(lamp);

                    }
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @Override
    public void update(Lamp obj,String token, final ResponseHandler response) throws DAOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());
        params.put("idDepartment",  String.valueOf(obj.getDepartment().getId()));
        Helper.getInstance().PUT("http://81.64.139.113:1337/api/Lamp/" + obj.getId(), token, params, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                response.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                response.onError(object);
            }
        });
    }

    @Override
    public void delete(int id,String token, final ResponseHandler response) throws DAOException {
        Helper.getInstance().DELETE("http://81.64.139.113:1337/api/Lamp/" + id, token, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                response.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                response.onError(object);
            }
        });
    }

    @Override
    public void getAll(final ResponseHandler response) throws DAOException {
        final ArrayList<Lamp> lamps =  new ArrayList<Lamp>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Lamp/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = null;
                            Object iddep = null;
                            try {
                                current = array.getJSONObject(i);
                                iddep = current.optInt("idDepartment");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if(iddep == null){
                                    lamps.add(new Lamp(current.getInt("idLamp"), current.getString("nameLamp"), null));
                                }
                                else{
                                    lamps.add(new Lamp(current.getInt("idLamp"), current.getString("nameLamp"), new Department(current.optInt("idDepartment"), current.getString("nameDepartment"))));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        response.onSuccess(lamps);

                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
