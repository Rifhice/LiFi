package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.FidelityPercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.FidelityQuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.RegularPercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.RegularQuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void getById(int id, ResponseHandler response) throws DAOException {

    }

    @Override
    public void update(Discount obj,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void delete(int id,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void getAll(final ResponseHandler response) throws DAOException {


       /* String url = "http://81.64.139.113:1337/api/Discount";
        final ArrayList<Discount> discounts =  new ArrayList<Discount>();

        Helper.getInstance().GET("http://81.64.139.113:1337/api/Discount/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("GET 1"+object.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) object;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        JSONObject current = jsonArray.getJSONObject(i);
                        System.out.println("GET 2 "+ current.toString());
                        int fidelity = current.getInt("fidelity");
                        final int fkProduct = current.getInt("idProduct");
                        final Product product ;

                        Helper.getInstance(this);
                        ProductDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();

                        dao.getById(2, new ResponseHandler() {
                            @Override
                            public void onSuccess(Object object) {
                                try{
                                    JSONObject produit = JSONObject.getJSONObject();
                                    System.out.println("GET 1 "+object.toString());
                                    product = new Product(fkProduct, produit.getString("name"), produit.getString("description"), produit.getDouble("price"), produit.getString("Brand") )

                                }catch (JSONException e){
                                    e.printStackTrace();


                                }
                              }

                            @Override
                            public void onError(Object object) {
                                System.out.println("fail getbyid");
                            }
                        });


                        if (fidelity == 0) {
                            try {

                                Date dateDbt = sdf.parse(current.getString("date_start"));
                                Date dateFin = sdf.parse(current.getString("date_fin"));
                                Date dateCreation = sdf.parse(current.getString("date_update"));


                                discounts.add(new RegularQuantityDiscount(new Product(current.getInt("fkProduct"), current.getString("nameProduct"), current.getString("description"), (float) (current.getDouble("price")), current.getString("brand"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment")))
                                        , dateDbt, dateFin, dateCreation, current.getInt("Bought"), current.getInt("Free")));

                            } catch (java.text.ParseException e) {
                                e.printStackTrace();

                            }

                        }else{
                            try {

                                Date dateDbt = sdf.parse(current.getString("date_start"));
                                Date dateFin = sdf.parse(current.getString("date_fin"));
                                Date dateCreation = sdf.parse(current.getString("date_update"));
                                discounts.add(new FidelityQuantityDiscount(new Product(current.getInt("fkProduct"), current.getString("nameProduct"), current.getString("description"), (float) (current.getDouble("price")), current.getString("brand"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment")))
                                        , dateDbt, dateFin, dateCreation, current.getInt("Bought"), current.getInt("Free")));

                            } catch (java.text.ParseException e) {
                                e.printStackTrace();

                            }
                        }


                    } catch (JSONException e) {


                        try {
                            JSONObject current = jsonArray.getJSONObject(i);
                            System.out.println("GET 2 "+ current.toString());
                            int fidelity = current.getInt("fidelity");


                            if (fidelity == 0) {
                                try {

                                    Date dateDbt = sdf.parse(current.getString("date_start"));
                                    Date dateFin = sdf.parse(current.getString("date_fin"));
                                    Date dateCreation = sdf.parse(current.getString("date_update"));


                                    discounts.add(new RegularPercentageDiscount(new Product(current.getInt("fkProduct"), current.getString("nameProduct"), current.getString("description"), (float) (current.getDouble("price")), current.getString("brand"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment")))
                                            , dateDbt, dateFin, dateCreation, current.getInt("Percentage")));
                                } catch (java.text.ParseException e3) {
                                    e.printStackTrace();

                                }
                            } else try {

                                Date dateDbt = sdf.parse(current.getString("date_start"));
                                Date dateFin = sdf.parse(current.getString("date_fin"));
                                Date dateCreation = sdf.parse(current.getString("date_update"));
                                discounts.add(new FidelityPercentageDiscount(new Product(current.getInt("fkProduct"), current.getString("nameProduct"), current.getString("description"), (float) (current.getDouble("price")), current.getString("brand"), new Department(current.getInt("idDepartment"), current.getString("nameDepartment")))
                                        , dateDbt, dateFin, dateCreation, current.getInt("Percentage")));

                            } catch (java.text.ParseException e4) {
                                e.printStackTrace();

                            }

                        }catch (JSONException e2) {
                            e.printStackTrace();
                        }


                    }
                }
                response.onSuccess(jsonArray);


            }

            @Override
            public void onError(Object object) {
                response.onError("ERROR");
            }
        });


*/
    }
}
