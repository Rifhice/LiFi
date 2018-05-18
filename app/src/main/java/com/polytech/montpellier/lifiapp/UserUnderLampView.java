package com.polytech.montpellier.lifiapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.text.*;
import java.util.Locale;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kevin on 03/05/2018.
 */

public class UserUnderLampView extends AppCompatActivity {


    final Context context = this;

    DiscountDAO daoDiscount = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO();
    DepartmentDAO daodep = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO();

    ProductDAO daoProduit = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helper.getInstance(this);

        setContentView(R.layout.user_under_lamp);
        Helper.hasActiveInternetConnection(this);
        final TableLayout tl = findViewById(R.id.promotionsJourTable);
        final TextView rayonTV = findViewById(R.id.rayonTV);

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

        final Intent intent = getIntent();
        intent.getStringExtra("lampName");
        final int pkDep = intent.getIntExtra("lampDep", 0);

        // fetchingn Department object via the id in instance
        daodep.getById(pkDep, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof Department) {
                    final Department department = (Department) object;


                    rayonTV.append(": " + intent.getStringExtra("lampDepName"));

                    final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    final Date dateToday = new Date();


                    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

                    // fetching all the products of the department

                    daodep.getAllProducts(department, new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {
                            if (object == null) {
                                System.out.println("NO PRODUCTS IN DEP");
                            } else {
                                System.out.println("object on create userunderlamp" + object);
                                if (object instanceof ArrayList) {
                                    System.out.println("object on create userunderlampin instance of " + object.toString());

                                    ArrayList<Product> depProducts = (ArrayList<Product>) object;

                                    for (int i = 0; i < depProducts.size(); i++) {

                                        final Product currentProduct = depProducts.get(i);
                                        System.out.println("current Product  " + currentProduct);


                                        // fetching all the discounts' ID of the product

                                        daoProduit.getProductDiscounts(currentProduct, new ResponseHandler() {

                                            @Override
                                            public void onSuccess(Object object) {
                                                System.out.println("RESPRODDISC" + object.toString());
                                                if (object == null) {
                                                    System.out.println("NO PROMOTIONS");
                                                } else {
                                                    if (object instanceof ArrayList) {
                                                        ArrayList<Discount> productDiscounts = (ArrayList) object;

                                                        //System.out.println("productDiscout " + object.toString());
                                                        for (int i = 0; i < productDiscounts.size(); i++) {


                                                            final Discount currentDiscount = productDiscounts.get(i);
                                                            System.out.println("current Discount  " + currentDiscount);

                                                            int fidelity = currentDiscount.getFidelity();

                                                            int color;
                                                            if (fidelity == 0) {
                                                                color = Color.rgb(60, 134, 252);
                                                            } else {
                                                                color = Color.rgb(85, 252, 60);
                                                            }
                                                            if (currentDiscount instanceof PercentageDiscount) {
                                                                System.out.println("here walla 8");

                                                                float percentage = ((PercentageDiscount) currentDiscount).getPercentage();

                                                                TableRow row = new TableRow(context);
                                                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                                row.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                row.setBackgroundColor(color);
                                                                row.setId(currentDiscount.getId());
                                                                row.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Intent intent = new Intent(UserUnderLampView.this, DiscountSummary.class);
                                                                        intent.putExtra("idDiscount", currentDiscount.getId());
                                                                        intent.putExtra("idProduct", currentDiscount.getProduct().getId());
                                                                        intent.putExtra("idDepartement", pkDep);
                                                                        startActivity(intent);
                                                                    }
                                                                });


                                                                TextView label_Product = new TextView(context);
                                                                label_Product.setText(currentProduct.getName());
                                                                label_Product.setTextColor(Color.BLACK);
                                                                label_Product.setPadding(5, 5, 5, 5);
                                                                label_Product.setWidth(tl.getWidth() / 3);
                                                                row.addView(label_Product);

                                                                TextView percentage_Discount = new TextView(context);
                                                                percentage_Discount.setText(percentage + "% off");
                                                                percentage_Discount.setTextColor(Color.BLACK);
                                                                percentage_Discount.setPadding(5, 5, 5, 5);
                                                                percentage_Discount.setWidth(tl.getWidth() / 3);

                                                                row.addView(percentage_Discount);

                                                                tl.addView(row);
                                                            } else if (currentDiscount instanceof QuantityDiscount) {

                                                                int bought = ((QuantityDiscount) currentDiscount).getBought();
                                                                int free = ((QuantityDiscount) currentDiscount).getFree();

                                                                TableRow row = new TableRow(context);
                                                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                                                row.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                row.setBackgroundColor(color);
                                                                row.setId(currentDiscount.getId());
                                                                row.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Intent intent = new Intent(UserUnderLampView.this, DiscountSummary.class);
                                                                        intent.putExtra("idDiscount", currentDiscount.getId());
                                                                        intent.putExtra("idProduct", currentDiscount.getProduct().getId());
                                                                        intent.putExtra("idDepartement", pkDep);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                                System.out.println("here walla");


                                                                TextView label_Product = new TextView(context);
                                                                label_Product.setText(currentProduct.getName());
                                                                label_Product.setTextColor(Color.BLACK);
                                                                label_Product.setPadding(5, 5, 5, 5);
                                                                label_Product.setWidth(tl.getWidth() / 3);
                                                                row.addView(label_Product);

                                                                TextView boughtView = new TextView(context);
                                                                Resources res = getResources();
                                                                String text = bought + " " + res.getString(R.string.bought) + " " + free + " " + res.getString(R.string.free);
                                                                boughtView.setText(text);
                                                                boughtView.setTextColor(Color.BLACK);
                                                                boughtView.setPadding(5, 5, 5, 5);
                                                                boughtView.setWidth(tl.getWidth() / 3);
                                                                row.addView(boughtView);

                                                                tl.addView(row);

                                                            }// lseif lenght 9


                                                        }// for productDiscountd
                                                    }
                                                }// else
                                            }// On success Product discounts

                                            @Override
                                            public void onError(Object object) {
                                                System.out.println("Product discounts ");


                                            }


                                        });//for depProducts

                                    }// if
                                }//else On success
                            } //on success
                        }

                        ;//getProducts of dep

                        @Override
                        public void onError(Object object) {

                        }

                    });
                }
            }

            @Override
            public void onError(Object object) {

            }//daodep getbyid


        });//on create
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserUnderLampView.this, MainActivity.class);
        startActivity(intent);
    }


}//userunderLamp

