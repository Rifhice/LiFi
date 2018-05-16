package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Product;
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

public class MySqlDepartmentDAO extends DepartmentDAO{

    @Override
    public void create(Department obj,String token, final ResponseHandler response) throws DAOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());

        Helper.getInstance().POST("http://81.64.139.113:1337/api/Department", token, params, new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                response.onSuccess(object);
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
    public void update(Department obj,String token, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());


        Helper.getInstance().PUT("http://81.64.139.113:1337/api/Department/" + obj.getId(), token, params, new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                response.onSuccess(object);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @Override
    public void delete(int id, String token, final ResponseHandler response) throws DAOException {
        System.out.println("IDRECEIVED" + id);
        Helper.getInstance().DELETE("http://81.64.139.113:1337/api/Department/" + id, token , new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                response.onSuccess(object);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @Override
    public void getAll(final ResponseHandler response) throws DAOException {
        final ArrayList<Department> departments =  new ArrayList<Department>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Department/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    try {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = array.getJSONObject(i);
                            departments.add(new Department(current.getInt("idDepartment"), current.getString("name")));
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

    @Override
    public void getAllProducts(final Department dep, final ResponseHandler res) {
        final ArrayList<Product> products =  new ArrayList<Product>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Department/" + dep.getId() + "/Products", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    try {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = array.getJSONObject(i);
                            products.add(new Product(current.getInt("idProduct"), current.getString("name"), current.getString("description"), (float)current.getDouble("price"), current.getString("brand"), dep));
                        }
                        res.onSuccess(products);
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
