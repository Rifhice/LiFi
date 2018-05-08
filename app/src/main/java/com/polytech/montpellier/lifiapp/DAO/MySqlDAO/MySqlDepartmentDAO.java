package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlDepartmentDAO extends DepartmentDAO{

    @Override
    public void create(Department obj, final ResponseHandler response) throws DAOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());

        Helper.getInstance().POST("http://81.64.139.113:1337/api/Department", "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", params, new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {
                        try {
                            JSONObject current = array.getJSONObject(0);
                            response.onSuccess(new Department(current.getInt("id"), current.getString("name")));
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
    public void getById(int id, final ResponseHandler response) throws DAOException {
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Department/" + id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {
                        try {
                            JSONObject current = array.getJSONObject(0);
                            response.onSuccess(new Department(current.getInt("id"), current.getString("name")));
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
    public void update(Department obj, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());


        Helper.getInstance().POST("http://81.64.139.113:1337/api/Product", "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8", params, new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {
                        try {
                            JSONObject current = array.getJSONObject(0);
                            response.onSuccess(new Department(current.getInt("id"), current.getString("name")));
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
    public void delete(int id, ResponseHandler response) throws DAOException {
        Helper.getInstance().DELETE("http://81.64.139.113:1337/api/Department/" + id, "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8" , new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                //TODO
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @Override
    public void getAll(ResponseHandler response) throws DAOException {
        final ArrayList<Department> departments =  new ArrayList<Department>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Departement/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    try {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = array.getJSONObject(i);
                            departments.add(new Department(current.getInt("id"), current.getString("name")));
                        }
                        response.onSuccess(departments);
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
