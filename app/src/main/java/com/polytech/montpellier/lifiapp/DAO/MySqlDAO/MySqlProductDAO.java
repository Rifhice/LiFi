package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;


import android.os.AsyncTask;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;
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

public class MySqlProductDAO extends ProductDAO {

    @Override
    public void create(Product obj,String token, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());
        params.put("description", obj.getDescription());
        params.put("price",  String.valueOf(obj.getPrice()));
        params.put("brand", obj.getDescription());
        params.put("idDepartement", String.valueOf(obj.getDepartment())) ;


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
                            response.onSuccess(new Product(current.getInt("id"), current.getString("name"),  current.getString("description"),  Float.parseFloat(current.getString("price")),  current.getString("brand"),new Department(current.getInt("idDepartment"), current.getString("nameDepartment"))));
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

        //TODO : getbyId in product.js
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Product/" + id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {
                        try {
                            JSONObject current = array.getJSONObject(0);
                            response.onSuccess(new Product(current.getInt("idProduct"), current.getString("name"),  current.getString("description"),  Float.parseFloat(current.getString("price")),  current.getString("brand"),new Department(current.getInt("idDepartment"), current.getString("name"))));
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
    public void update(Product obj,String token, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());
        params.put("description", obj.getDescription());
        params.put("price",  String.valueOf(obj.getPrice()));
        params.put("brand", obj.getDescription());
        params.put("idDepartement", String.valueOf(obj.getDepartment())) ;


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
                            response.onSuccess(new Product(current.getInt("idProduct"), current.getString("nameProduct"),  current.getString("descriptionProduct"),  Float.parseFloat(current.getString("priceProduct")),  current.getString("brandProduct"),new Department(current.getInt("idDepartment"), current.getString("nameDepartment"))));
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
    public void delete(int id,String token, final ResponseHandler response) throws DAOException {
        Helper.getInstance().DELETE("http://81.64.139.113:1337/api/Product/" + id, "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8" , new ResponseHandler() {
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
    public void getAll(final ResponseHandler response) throws DAOException {
        final ArrayList<Product> products =  new ArrayList<Product>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Product/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    try {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject current = array.getJSONObject(i);
                            products.add(new Product(current.getInt("idProduct"), current.getString("nameProduct"),  current.getString("descriptionProduct"),  Float.parseFloat(current.getString("priceProduct")),  current.getString("brandProduct"),new Department(current.getInt("idDepartment"), current.getString("nameDepartment"))));
                        }
                        response.onSuccess(products);
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
