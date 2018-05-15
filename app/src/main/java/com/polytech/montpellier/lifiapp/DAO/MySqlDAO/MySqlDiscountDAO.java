package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlDiscountDAO extends DiscountDAO {

    @Override
    public void create(Discount obj,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void getById(int id,final ResponseHandler response) throws DAOException {
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();


        Helper.getInstance().GET("http://81.64.139.113:1337/api/Discount/"+id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
               // System.out.println("GET 1"+object.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) object;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        final JSONObject current = jsonArray.getJSONObject(i);
                        final int fidelity = current.getInt("fidelity");
                        final int fkProduct = current.getInt("fkProduct");
                        final Date start = sdf.parse(current.getString("date_start"));
                        final Date end = sdf.parse(current.getString("date_end"));
                        final Date creation = sdf.parse(current.getString("date_update"));
                        Product prod = new Product(fkProduct, current.getString("name"), current.getString("description"), (float)current.getDouble("price"), current.getString("brand"),new Department(current.getInt("idDepartment")));
                        try{
                            float percentage = (float)current.getDouble("percentage");
                            discounts.add(new PercentageDiscount(prod,start,end,creation,percentage,fidelity));
                        }catch (JSONException e){
                            int bought = current.getInt("Bought");
                            int free = current.getInt("Free");
                            discounts.add(new QuantityDiscount(prod,start, end, creation, bought, free, fidelity));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                response.onSuccess(discounts);
            }

            @Override
            public void onError(Object object) {
                response.onError("ERROR");
            }
        });


    }


    @Override
    public void update(Discount obj,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void delete(int id,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void getAll(final ResponseHandler response) throws DAOException {
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();

        Helper.getInstance().GET("http://81.64.139.113:1337/api/Discount/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("GET 1"+object.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) object;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        final JSONObject current = jsonArray.getJSONObject(i);
                        final int fidelity = current.getInt("fidelity");
                        final int fkProduct = current.getInt("fkProduct");
                        final Date start = sdf.parse(current.getString("date_start"));
                        final Date end = sdf.parse(current.getString("date_end"));
                        final Date creation = sdf.parse(current.getString("date_update"));
                        Product prod = new Product(fkProduct, current.getString("name"), current.getString("description"), (float)current.getDouble("price"), current.getString("brand"),new Department(current.getInt("idDepartment")));
                        try{
                            float percentage = (float)current.getDouble("percentage");
                            discounts.add(new PercentageDiscount(prod,start,end,creation,percentage,fidelity));
                        }catch (JSONException e){
                            int bougth = current.getInt("Bought");
                            int free = current.getInt("Free");
                            discounts.add(new QuantityDiscount(prod,start, end, creation, bougth, free, fidelity));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                response.onSuccess(discounts);
            }

            @Override
            public void onError(Object object) {
                response.onError("ERROR");
            }
        });

    }


    public void getAllByDate(final Date date ,final ResponseHandler response) throws DAOException {
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();

        Helper.getInstance().GET("http://81.64.139.113:1337/api/Discount/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("GET 1"+object.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) object;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        final JSONObject current = jsonArray.getJSONObject(i);
                        final int fidelity = current.getInt("fidelity");
                        final int fkProduct = current.getInt("fkProduct");
                        final Date start = sdf.parse(current.getString("date_start"));
                        final Date end = sdf.parse(current.getString("date_end"));
                        final Date creation = sdf.parse(current.getString("date_update"));
                        Product prod = new Product(fkProduct, current.getString("name"), current.getString("description"), (float)current.getDouble("price"), current.getString("brand"),new Department(current.getInt("idDepartment")));
                        try{
                            if (date < end && date >start) {
                                float percentage = (float) current.getDouble("percentage");
                                discounts.add(new PercentageDiscount(prod, start, end, creation, percentage, fidelity));
                            }
                        }catch (JSONException e){
                            if (date < end && date >start) {

                                int bougth = current.getInt("Bought");
                                int free = current.getInt("Free");
                                discounts.add(new QuantityDiscount(prod, start, end, creation, bougth, free, fidelity));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                response.onSuccess(discounts);
            }

            @Override
            public void onError(Object object) {
                response.onError("ERROR");
            }
        });

    }
}
