package com.polytech.montpellier.lifiapp.DAO.DistantDAO;


import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
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

public class DistantProductDAO extends ProductDAO {

    @Override
    public void create(Product obj, String token, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());
        params.put("description", obj.getDescription());
        params.put("price", String.valueOf(obj.getPrice()));
        params.put("brand", obj.getBrand());
        params.put("idDepartment", String.valueOf(obj.getDepartment().getId()));

        Helper.getInstance().POST(MainActivity.url + "Product", token, params, new ResponseHandler() {
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
        final ArrayList<Product> product = new ArrayList<Product>();
        Helper.getInstance().GET(MainActivity.url + "Product/" + id, new ResponseHandler() {
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
                            if (iddep == null) {
                                product.add(new Product(current.getInt("idProduct"), current.getString("nameProduct"), current.getString("descriptionProduct"), Float.parseFloat(current.getString("priceProduct")), current.getString("brandProduct"), null));
                            } else {
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
    public void update(Product obj, String token, final ResponseHandler response) throws DAOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", obj.getName());
        params.put("description", obj.getDescription());
        params.put("price", String.valueOf(obj.getPrice()));
        params.put("brand", obj.getBrand());
        params.put("idDepartment", String.valueOf(obj.getDepartment().getId()));

        Helper.getInstance().PUT(MainActivity.url + "Product/" + obj.getId(), token, params, new ResponseHandler() {

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
    public void delete(int id, String token, final ResponseHandler response) throws DAOException {
        Helper.getInstance().DELETE(MainActivity.url + "Product/" + id, token, new ResponseHandler() {
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
        final ArrayList<Product> products = new ArrayList<Product>();
        Helper.getInstance().GET(MainActivity.url + "Product/", new ResponseHandler() {
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
                            if (iddep == null) {
                                products.add(new Product(current.getInt("idProduct"), current.getString("nameProduct"), current.getString("descriptionProduct"), Float.parseFloat(current.getString("priceProduct")), current.getString("brandProduct"), null));
                            } else {
                                products.add(new Product(current.getInt("idProduct"), current.getString("nameProduct"), current.getString("descriptionProduct"), Float.parseFloat(current.getString("priceProduct")), current.getString("brandProduct"), new Department(current.optInt("idDepartment"), current.getString("nameDepartment"))));
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


    @Override
    public void getProductDiscounts(final Product product, final ResponseHandler response) throws DAOException {

        final DiscountDAO discountdao = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDiscountDAO();
        final ArrayList<Discount> discounts = new ArrayList<Discount>();
        Helper.getInstance().GET(MainActivity.url + "Product/" + product.getId() + "/Discounts/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    System.out.println("SQL in onsucces2 ");

                    for (int i = 0; i < array.length(); i++) {

                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        try {


                            final JSONObject current = array.getJSONObject(i);
                            final int id = current.getInt("idDiscount");

                            final int fidelity = current.getInt("fidelity");
                            final Date start = sdf.parse(current.getString("date_start"));
                            final Date end = sdf.parse(current.getString("date_end"));
                            final Date update = sdf.parse(current.getString("date_update"));


                            if (current.has("percentage")) {
                                float percentage = (float) current.getDouble("percentage");


                                discounts.add(new PercentageDiscount(id, product, start, end, update, percentage, fidelity));
                                System.out.println("discounts afteradd" + discounts);

                            } else if (current.has("bought") && current.has("free")) {
                                int bought = current.getInt("bought");
                                int free = current.getInt("free");


                                discounts.add(new QuantityDiscount(id, product, start, end, update, bought, free, fidelity));
                                System.out.println("discounts afteradd" + discounts);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    response.onSuccess(discounts);
                } else {

                }
                System.out.println("discountss" + discounts);
            }// on success getProducts

            @Override
            public void onError(Object object) {

            }
        });


    }
}
