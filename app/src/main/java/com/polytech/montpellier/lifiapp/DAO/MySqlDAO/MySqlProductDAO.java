package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;


import android.os.AsyncTask;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
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
        params.put("brand", obj.getBrand());
        params.put("idDepartment", String.valueOf(obj.getDepartment().getId())) ;

        //System.out.println("Name: " + params.get("name") +" Description: " + params.get("description") + " price: " + params.get("price") + " brand: " + params.get("brand") +
         //       " idDep : " +  params.get("idDepartment"));

        Helper.getInstance().POST("http://81.64.139.113:1337/api/Product",token , params, new ResponseHandler() {
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
        final ArrayList<Product> product =  new ArrayList<Product>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Product/" + id, new ResponseHandler() {
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
                                    product.add(new Product(current.getInt("idProduct"), current.getString("nameProduct"), current.getString("descriptionProduct"), Float.parseFloat(current.getString("priceProduct")), current.getString("brandProduct"), null));
                                }
                                else{
                                    product.add(new Product(current.getInt("idProduct"), current.getString("nameProduct"), current.getString("descriptionProduct"), Float.parseFloat(current.getString("priceProduct")), current.getString("brandProduct"), new Department(current.optInt("idDepartment"), current.getString("nameDepartment"))));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            response.onSuccess(product);

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
        params.put("brand", obj.getBrand());
        params.put("idDepartment", String.valueOf(obj.getDepartment().getId())) ;

        Helper.getInstance().PUT("http://81.64.139.113:1337/api/Product/" + obj.getId(), token, params, new ResponseHandler() {

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
        Helper.getInstance().DELETE("http://81.64.139.113:1337/api/Product/" + id, token , new ResponseHandler() {
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
        final ArrayList<Product> products =  new ArrayList<Product>();
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Product/", new ResponseHandler() {
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
                                products.add(new Product(current.getInt("idProduct"), current.getString("name"), current.getString("description"), Float.parseFloat(current.getString("price")), current.getString("brand"), null));
                            }
                            else{
                                products.add(new Product(current.getInt("idProduct"), current.getString("name"), current.getString("description"), Float.parseFloat(current.getString("price")), current.getString("brand"), new Department(current.optInt("idDepartment"), current.getString("name"))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    response.onSuccess(products);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
