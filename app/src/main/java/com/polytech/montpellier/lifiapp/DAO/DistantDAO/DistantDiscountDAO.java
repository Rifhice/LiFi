package com.polytech.montpellier.lifiapp.DAO.DistantDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.MainActivity;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 30/04/2018.
 */

public class DistantDiscountDAO extends DiscountDAO {

    String url = MainActivity.url + "Discount/" ;
    @Override
    public void create(Discount obj, String token, final ResponseHandler response) throws DAOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fkProduct", obj.getProduct().getId() + "");
        params.put("date_start", new SimpleDateFormat( "yyyy-MM-dd").format(obj.getDateFin()));
        params.put("date_end",  new SimpleDateFormat( "yyyy-MM-dd").format(obj.getDateDebut()));
        params.put("fidelity", obj.getFidelity() + "");
        String url = MainActivity.url + "Discount/";
        if(obj instanceof PercentageDiscount){
            params.put("percentage", ((PercentageDiscount)obj).getPercentage() + "");
            url += "Percentage";
        }
        else if(obj instanceof QuantityDiscount){
            params.put("bought", ((QuantityDiscount)obj).getBought() + "");
            params.put("free", ((QuantityDiscount)obj).getFree() + "");
            url += "Quantity";
        }
        Helper.getInstance().POST(url, token, params, new ResponseHandler() {
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
    public void getById(int id,final ResponseHandler response) throws DAOException {
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();

        Helper.getInstance().GET(MainActivity.url + "Discount/"+id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if(object != null) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = (JSONArray) object;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            final JSONObject current = jsonArray.getJSONObject(i);
                            final int id = current.getInt("idDiscount");
                            final int fidelity = current.getInt("fidelity");
                            final int fkProduct = current.getInt("fkProduct");
                            final Date start = sdf.parse(current.getString("date_start"));
                            final Date end = sdf.parse(current.getString("date_end"));
                            final Date creation = sdf.parse(current.getString("date_update"));
                            Product prod = new Product(fkProduct, current.getString("name"), current.getString("description"), (float) current.getDouble("price"), current.getString("brand"), new Department(current.getInt("idDepartment"),current.getString("nameDepartment")));
                            try {
                                float percentage = (float) current.getDouble("percentage");
                                discounts.add(new PercentageDiscount(id, prod, start, end, creation, percentage, fidelity));
                            } catch (JSONException e) {
                                int bought = current.getInt("bought");
                                int free = current.getInt("free");
                                discounts.add(new QuantityDiscount(id, prod, start, end, creation, bought, free, fidelity));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    response.onSuccess(discounts);
                }
                else {
                    response.onSuccess(object);
                }
            }

            @Override
            public void onError(Object object) {
                response.onError("ERROR");
            }
        });


    }


    @Override
    public void update(Discount obj,String token, final ResponseHandler response) throws DAOException {
        StringBuilder url = new StringBuilder();
        url.append(this.url);

        Map<String, String> params = new HashMap<String, String>();
        params.put("fkProduct", String.valueOf(obj.getProduct().getId()));
        params.put("date_start", new SimpleDateFormat( "yyyy-MM-dd").format(obj.getDateFin()));
        params.put("date_end",  new SimpleDateFormat( "yyyy-MM-dd").format(obj.getDateDebut()));
        params.put("fidelity", String.valueOf(obj.getFidelity()));

            if(obj instanceof PercentageDiscount){
                params.put("percentage",  String.valueOf(((PercentageDiscount) obj).getPercentage()));
                url.append("Percentage/");
            }else if(obj instanceof QuantityDiscount){
                params.put("bought", String.valueOf(((QuantityDiscount) obj).getBought()));
                params.put("free", String.valueOf(((QuantityDiscount) obj).getFree()));
                url.append("Quantity/");
            }

        url.append(obj.getId());
        String urlString = url.toString();

        Helper.getInstance().PUT(urlString, token, params, new ResponseHandler() {

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
        Helper.getInstance().DELETE(MainActivity.url + "Discount/" + id, token, new ResponseHandler() {
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
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();

        Helper.getInstance().GET(MainActivity.url + "Discount/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) object;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        final JSONObject current = jsonArray.getJSONObject(i);
                        final int id = current.getInt("idDiscount");
                        final int fidelity = current.getInt("fidelity");
                        final int fkProduct = current.getInt("fkProduct");
                        final Date start = sdf.parse(current.getString("date_start"));
                        final Date end = sdf.parse(current.getString("date_end"));
                        final Date creation = sdf.parse(current.getString("date_update"));
                        Product prod = new Product(fkProduct, current.getString("name"), current.getString("description"), (float)current.getDouble("price"), current.getString("brand"),new Department(current.optInt("idDepartment")));
                        try{
                            float percentage = (float)current.getDouble("percentage");
                            discounts.add(new PercentageDiscount(id,prod,start,end,creation,percentage,fidelity));
                        }catch (JSONException e){
                            int bougth = current.getInt("Bought");
                            int free = current.getInt("Free");
                            discounts.add(new QuantityDiscount(id,prod,start, end, creation, bougth, free, fidelity));
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
    public void getAllByDate(final Date date ,final ResponseHandler response) throws DAOException {
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();

        Helper.getInstance().GET(MainActivity.url + "Discount/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) object;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        final JSONObject current = jsonArray.getJSONObject(i);
                        final int fidelity = current.getInt("fidelity");
                        final int id = current.getInt("idDiscount");

                        final int fkProduct = current.getInt("fkProduct");
                        final Date start = sdf.parse(current.getString("date_start"));
                        final Date end = sdf.parse(current.getString("date_end"));
                        final Date creation = sdf.parse(current.getString("date_update"));
                        Product prod = new Product(fkProduct, current.getString("name"), current.getString("description"), (float)current.getDouble("price"), current.getString("brand"),new Department(current.optInt("idDepartment")));
                        try{
                            if (date.compareTo(start)<= 0 && date.compareTo(end)>= 0) {
                                float percentage = (float) current.getDouble("percentage");
                                discounts.add(new PercentageDiscount(id,prod, start, end, creation, percentage, fidelity));
                            }
                        }catch (JSONException e){
                            if (date.compareTo(start)<= 0 && date.compareTo(end)>= 0) {

                                int bougth = current.getInt("Bought");
                                int free = current.getInt("Free");
                                discounts.add(new QuantityDiscount(id,prod, start, end, creation, bougth, free, fidelity));
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
