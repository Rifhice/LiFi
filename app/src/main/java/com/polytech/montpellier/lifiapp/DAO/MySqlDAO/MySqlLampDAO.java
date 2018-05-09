package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import android.os.AsyncTask;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;

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


        Helper.getInstance().GET("http://81.64.139.113:1337/api/Lamp/" + id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {
                        try {
                            JSONObject current = array.getJSONObject(0);
                            response.onSuccess(new Lamp(current.getInt("idLamp"), current.getString("nameLamp"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        //TODO
    }

    @Override
    public void delete(int id,String token, final ResponseHandler response) throws DAOException {
        //TODO
    }

    @Override
    public void getAll(final ResponseHandler response) throws DAOException {
        final ArrayList<Lamp> lamps =  new ArrayList<Lamp>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Lamp/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    try {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = array.getJSONObject(i);
                            lamps.add(new Lamp(current.getInt("idLamp"), current.getString("nameLamp"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment"))));
                        }
                        response.onSuccess(lamps);
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
}
