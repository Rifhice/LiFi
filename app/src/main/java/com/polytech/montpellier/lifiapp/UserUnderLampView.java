package com.polytech.montpellier.lifiapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.FidelityPercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.FidelityQuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.RegularPercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.RegularQuantityDiscount;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * Created by Kevin on 03/05/2018.
 */

public class UserUnderLampView extends AppCompatActivity {


    final Context context = this;

    ProductDAO daoProduct = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helper.getInstance(this);

        setContentView(R.layout.user_under_lamp);

        final TableLayout tl = findViewById(R.id.promotionsJourTable);

        TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_header = new TextView(this);
        label_header.setId(200);
        label_header.setText(R.string.promotionsJour);
        label_header.setTextColor(Color.WHITE);
        label_header.setPadding(5, 5, 5, 5);
        tr_head.addView(label_header);// add the column to the table row here


        tl.addView(tr_head);

        Intent intent = getIntent();
        intent.getStringExtra("lampName") ;
        int pkDep = intent.getIntExtra("lampDep", 0) ;

        Helper.getInstance().GET("http://81.64.139.113:1337/api/Department/" + pkDep+ "/Products/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {

                System.out.println("object on create userunderlamp");
                if (object instanceof JSONArray){
                    System.out.println("object on create userunderlampin instance of " +object.toString());

                    JSONArray depProducts= (JSONArray) object ;

                    for (int i = 0; i < depProducts.length(); i++) {
                        try {
                            JSONObject currentProduct = depProducts.getJSONObject(i);
                            System.out.println("current Product  " + currentProduct);
                            int idProduct  = currentProduct.getInt("idProduct");
                            final String nameProduct  = currentProduct.getString("name");


                            Helper.getInstance().GET("http://81.64.139.113:1337/api/Product/" + idProduct + "/Discounts/", new ResponseHandler() {

                                @Override
                                public void onSuccess(Object object) {
                                    if (object == null){
                                        System.out.println("NO PROMOTIONS");
                                    }else{
                                    JSONArray productDiscounts = (JSONArray) object;

                                    //System.out.println("productDiscout " + object.toString());
                                    for (int i = 0; i < productDiscounts.length(); i++) {

                                        try {
                                            JSONObject currentDiscount = productDiscounts.getJSONObject(i);
                                            System.out.println("current Product  " + currentDiscount);
                                            int idDiscount = currentDiscount.getInt("idDiscount");

                                            TableRow row = new TableRow(context);
                                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                            row.setGravity(Gravity.CENTER_HORIZONTAL);


                                            TextView label_Product = new TextView(context);
                                            label_Product.setText(nameProduct);
                                            label_Product.setTextColor(Color.BLACK);
                                            label_Product.setPadding(5, 5, 5, 5);
                                            label_Product.setWidth(tl.getWidth() / 4);
                                            row.addView(label_Product);

                                            TextView id_Discount = new TextView(context);
                                            id_Discount.setText(idDiscount + "");
                                            id_Discount.setTextColor(Color.BLACK);
                                            id_Discount.setPadding(5, 5, 5, 5);
                                            id_Discount.setWidth(tl.getWidth() / 4);
                                            row.addView(id_Discount);

                                            tl.addView(row);



                                        } catch (JSONException e1) {
                                            e1.printStackTrace();

                                        }//catch

                                    }//for

                                }
                                }
                                //onsuccess

                                @Override
                                public void onError(Object object) {
                                    System.out.println("Error in getting all discounts od Product ");

                                }
                            });
                        }catch (JSONException e ){
                            e.printStackTrace();
                        }


                    }
                }
            }

            @Override
            public void onError(Object object) {
                System.out.println("Error in getting all products od departement ");
            }
        });





        Helper.getInstance(this);

        daoProduct.getById(2, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("GET 1 "+object.toString());

            }

            @Override
            public void onError(Object object) {
                System.out.println("fail getbyid");
            }
        });

        /*dao.getAll(new ResponseHandler(){
                @Override
                public void onSuccess(Object object) {
                    System.out.println("here dans on succes de uderunderlamp");
                    if (object instanceof ArrayList) {
                        ArrayList<Discount> array = (ArrayList<Discount>) object;
                        System.out.println("here dans instance arraylist  ");

                        for (int i = 0; i < array.size(); i++) {
                            Discount discount = array.get(i);
                            TableRow row = new TableRow(context);
                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            // row.setId(discount.);
                            row.setGravity(Gravity.CENTER_HORIZONTAL);

                            TextView label_Product = new TextView(context);
                            label_Product.setText(discount.getProduct().getName());
                            label_Product.setTextColor(Color.BLACK);
                            label_Product.setPadding(5, 5, 5, 5);
                            label_Product.setWidth(tl.getWidth() / 4);
                            row.addView(label_Product);

                            if (discount instanceof PercentageDiscount) {

                                PercentageDiscount percentageDiscount;
                                int color;
                                if (discount instanceof RegularPercentageDiscount) {
                                    percentageDiscount = (RegularPercentageDiscount) discount;
                                    color = Color.GREEN;

                                }//instance reg percentage
                                else {
                                    percentageDiscount = (FidelityPercentageDiscount) discount;
                                    color = Color.RED;

                                }

                                TextView label_prixAvant = new TextView(context);
                                label_prixAvant.setText((int) percentageDiscount.oldPrice());
                                label_prixAvant.setTextColor(color);
                                label_prixAvant.setPadding(5, 5, 5, 5);
                                label_prixAvant.setWidth(tl.getWidth() / 4);
                                row.addView(label_prixAvant);

                                TextView label_prix_apres = new TextView(context);
                                label_prix_apres.setText((int) percentageDiscount.newPrice());
                                label_prix_apres.setTextColor(color);
                                label_prix_apres.setPadding(5, 5, 5, 5);
                                label_prix_apres.setWidth(tl.getWidth() / 4);
                                row.addView(label_prix_apres);

                            }//instance Percentage
                            else if (discount instanceof QuantityDiscount) {

                                QuantityDiscount qtyDiscount;
                                int color;
                                if (discount instanceof RegularQuantityDiscount) {
                                    qtyDiscount = (RegularQuantityDiscount) discount;
                                    color = Color.GREEN;

                                }//instance reg percentage
                                else {
                                    qtyDiscount = (FidelityQuantityDiscount) discount;
                                    color = Color.RED;

                                }

                                TextView label_prixAvant = new TextView(context);
                                label_prixAvant.setText((int) qtyDiscount.oldPrice());
                                label_prixAvant.setTextColor(color);
                                label_prixAvant.setPadding(5, 5, 5, 5);
                                label_prixAvant.setWidth(tl.getWidth() / 4);
                                row.addView(label_prixAvant);

                                TextView label_prix_apres = new TextView(context);
                                label_prix_apres.setText((int) qtyDiscount.newPrice());
                                label_prix_apres.setTextColor(color);
                                label_prix_apres.setPadding(5, 5, 5, 5);
                                label_prix_apres.setWidth(tl.getWidth() / 4);
                                row.addView(label_prix_apres);


                            }


                            TextView label_datefin = new TextView(context);
                            label_datefin.setText(discount.getDateFin().toString());
                            label_datefin.setTextColor(Color.BLACK);
                            label_datefin.setPadding(5, 5, 5, 5);
                            label_datefin.setWidth(tl.getWidth() / 4);
                            row.addView(label_datefin);


                        }//for

                    }

                }//Onsuccess

                @Override
                public void onError(Object object) {
                    System.out.println("ERROR in getAllDiscounts");
                }
            });

*/
        }


}
