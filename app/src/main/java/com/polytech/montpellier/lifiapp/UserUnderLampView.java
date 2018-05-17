package com.polytech.montpellier.lifiapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.Date;
import java.text.*;
import java.util.Locale;


import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kevin on 03/05/2018.
 */

public class UserUnderLampView extends AppCompatActivity {


    final Context context = this;

    DiscountDAO daoDiscount = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO();


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
        intent.getStringExtra("lampName");
        final int pkDep = intent.getIntExtra("lampDep", 0);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date dateToday = new Date();


        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);



        Helper.getInstance().GET("http://81.64.139.113:1337/api/Department/" + pkDep + "/Products/", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object == null) {
                    System.out.println("NO PRODUCTS IN DEP");
                } else {
                    System.out.println("object on create userunderlamp"+object);
                    if (object instanceof JSONArray) {
                        System.out.println("object on create userunderlampin instance of " + object.toString());

                        JSONArray depProducts = (JSONArray) object;

                        for (int i = 0; i < depProducts.length(); i++) {
                            try {
                                JSONObject currentProduct = depProducts.getJSONObject(i);
                                System.out.println("current Product  " + currentProduct);
                                final int idProduct = currentProduct.getInt("idProduct");
                                final String nameProduct = currentProduct.getString("name");


                                Helper.getInstance().GET("http://81.64.139.113:1337/api/Product/" + idProduct + "/Discounts/", new ResponseHandler() {

                                    @Override
                                    public void onSuccess(Object object) {
                                        if (object == null) {
                                            System.out.println("NO PROMOTIONS");
                                        } else {
                                            JSONArray productDiscounts = (JSONArray) object;

                                            //System.out.println("productDiscout " + object.toString());
                                            for (int i = 0; i < productDiscounts.length(); i++) {


                                                try {
                                                    JSONObject currentDiscount = productDiscounts.getJSONObject(i);
                                                    System.out.println("current Discounr  " + currentDiscount);
                                                    final int idDiscount = currentDiscount.getInt("idDiscount");

                                                    daoDiscount.getAllByDate(dateToday, new ResponseHandler() {

                                                        @Override
                                                        public void onSuccess(Object object) {
                                                            if (object == null) {
                                                                System.out.println("NO PROMOTIONS");
                                                            } else {



                                                                JSONArray discounts = (JSONArray) object;

                                                                for (int i = 0; i < discounts.length(); i++) {

                                                                    try {
                                                                        JSONObject discount = discounts.getJSONObject(i);

                                                                        String date_end = discount.getString("date_end");

                                                                        String date_start = discount.getString("date_start");


                                                                        int fidelity = discount.getInt("fidelity");

                                                                        int color;
                                                                        if (fidelity == 0) {
                                                                            color = Color.BLUE;
                                                                        } else {
                                                                            color = Color.GREEN;
                                                                        }

                                                                         if (discount.length() == 14) {
                                                                                System.out.println("here walla 8");

                                                                                float percentage = (float) discount.getDouble("percentage");

                                                                                TableRow row = new TableRow(context);
                                                                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                                                row.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                                row.setBackgroundColor(color);
                                                                                row.setId(discount.getInt("idDiscount"));
                                                                                row.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        Intent intent = new Intent(UserUnderLampView.this, DiscountSummary.class);
                                                                                        intent.putExtra("idDiscount", idDiscount);
                                                                                        intent.putExtra("idProduct", idProduct);
                                                                                        intent.putExtra("idDepartement", pkDep);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                });


                                                                                TextView label_Product = new TextView(context);
                                                                                label_Product.setText(nameProduct);
                                                                                label_Product.setTextColor(Color.BLACK);
                                                                                label_Product.setPadding(5, 5, 5, 5);
                                                                                label_Product.setWidth(tl.getWidth() / 4);
                                                                                row.addView(label_Product);

                                                                                TextView percentage_Discount = new TextView(context);
                                                                                percentage_Discount.setText(percentage + "% off");
                                                                                percentage_Discount.setTextColor(Color.BLACK);
                                                                                percentage_Discount.setPadding(5, 5, 5, 5);
                                                                                percentage_Discount.setWidth(tl.getWidth() / 4);

                                                                                row.addView(percentage_Discount);

                                                                                tl.addView(row);
                                                                            } else if (discount.length() == 15) {


                                                                                int bought = discount.getInt("Bought");
                                                                                int free = discount.getInt("Free");

                                                                                TableRow row = new TableRow(context);
                                                                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                                                row.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                                row.setBackgroundColor(color);
                                                                                row.setId(discount.getInt("idDiscount"));
                                                                                row.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        Intent intent = new Intent(UserUnderLampView.this, DiscountSummary.class);
                                                                                        intent.putExtra("idDiscount", idDiscount);
                                                                                        intent.putExtra("idProduct", idProduct);
                                                                                        intent.putExtra("idDepartement", pkDep);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                });
                                                                                System.out.println("here walla");


                                                                                TextView label_Product = new TextView(context);
                                                                                label_Product.setText(nameProduct);
                                                                                label_Product.setTextColor(Color.BLACK);
                                                                                label_Product.setPadding(5, 5, 5, 5);
                                                                                label_Product.setWidth(tl.getWidth() / 4);
                                                                                row.addView(label_Product);

                                                                                TextView boughtView = new TextView(context);
                                                                                boughtView.setText(bought + " acheté");
                                                                                boughtView.setTextColor(Color.BLACK);
                                                                                boughtView.setPadding(5, 5, 5, 5);
                                                                                boughtView.setWidth(tl.getWidth() / 4);
                                                                                row.addView(boughtView);

                                                                                TextView freeView = new TextView(context);
                                                                                freeView.setText(free + " offert");
                                                                                freeView.setTextColor(Color.BLACK);
                                                                                freeView.setPadding(5, 5, 5, 5);
                                                                                freeView.setWidth(tl.getWidth() / 4);
                                                                                row.addView(freeView);

                                                                                tl.addView(row);

                                                                            }// lseif lenght 9



                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                        System.out.println("JSON exception date_end");
                                                                    }
                                                                }
                                                            }
                                                        }// on succeess idDiscount

                                                        @Override
                                                        public void onError(Object object) {
                                                            System.out.println("Product discounts ");


                                                        }
                                                    }); // helper idDiscount


                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                    System.out.println("JSON exception current discount");


                                                }//catch
                                            }// for productDiscountd
                                        }// else
                                    }// On success Product discounts

                                    @Override
                                    public void onError(Object object) {
                                        System.out.println("Product discounts ");


                                    }
                                });// helper product dicoounts


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }//for depProducts
                    }// if
                }//else On success
            } //on success

            @Override
            public void onError(Object object) {
                System.out.println("Error in getting all Products of dep ");
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(UserUnderLampView.this, MainActivity.class);
        startActivity(intent);
    }



}

