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

        Helper.getInstance().GET("http://81.64.139.113:1337/api/Discount/" + id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    if (array.length() == 0) {
                        response.onSuccess(null);
                    } else {
                        try {
                            JSONObject current = array.getJSONObject(0);
                            ProductDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();
                            final int fkProduct = current.getInt("idProduct");
                            final int fidelity = current.getInt("idProduct");

                            Product product;

                            dao.getById(fkProduct, new ResponseHandler() {
                                @Override
                                public void onSuccess(Object object) {
                                    try{
                                        if(object instanceof JSONObject) {
                                            JSONObject product = (JSONObject) object;
                                            System.out.println("GET 1 " + object.toString());
                                            Product prod = new Product(fkProduct, product.getString("name"), product.getString("description"), (float)product.getDouble("price"), product.getString("Brand"),new Department(product.getInt("idDepartment")));
                                            if(fidelity == 0){
                                                try{
                                                    float percentage = (float)current.getDouble("percentage");
                                                    discounts.add(new PercentageDiscount(prod,start,end,creation,percentage));
                                                }catch (JSONException e){
                                                    int bougth = current.getInt("bought");
                                                    int free = current.getInt("free"));

                                                    e.printStackTrace();
                                                }
                                            }
                                            else{

                                            }
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();


                                    }
                                }

                                @Override
                                public void onError(Object object) {
                                    System.out.println("fail getbyid");
                                }
                            });
                            if (current.length() == 8){
                                response.onSuccess(new PercentageDiscount( new Product(curre)  ));
                            }else{
                                response.onSuccess(new QuantityDiscount());
                            }
                           // response.onSuccess(new LaDmp(current.getInt("idLamp"), current.getString("nameLamp"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment"))));
                            //response.onSuccess(new Discount() {
                           // });


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
}
