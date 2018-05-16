package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.ArrayList;

public class ProductSummary extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_display);
        Intent intent = getIntent();

        ProductDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();

        final TextView textView_brand = (TextView) findViewById(R.id.textView_detailMarque);
        final TextView textView_name = (TextView) findViewById(R.id.textView_detailName);
        final TextView textView_department = (TextView) findViewById(R.id.textView_detailRayon);
        final TextView textView_price = (TextView) findViewById(R.id.textView_detailPrix);
        final TextView textView_description = (TextView) findViewById(R.id.textView_detailDescription) ;

        final int idProduct = intent.getIntExtra("product",0);
        final String brand = intent.getStringExtra("brand");
        final String price = intent.getStringExtra("price");
        final String name = intent.getStringExtra("name");
        final ArrayList<Integer> ar = new ArrayList<>();

        textView_name.setText(name);
        textView_brand.setText(brand);
        textView_price.setText(price);


        dao.getById(idProduct, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {

                if (object instanceof ArrayList) {

                    ArrayList<Product> array = (ArrayList<Product>) object;
                    Product product = array.get(0);
                    textView_description.setText(product.getDescription());
                    if (product.getDepartment().getName().isEmpty() ||product.getDepartment().getName().equals("") || product.getDepartment().getName() == null ||product.getDepartment().getName() == "null") {
                        String none = "None";
                        textView_department.setText(none);

                    } else {
                        textView_department.setText(product.getDepartment().getName());
                        ar.add(product.getDepartment().getId());

                    }


                }
            }
                @Override
                public void onError (Object object) {
                    System.out.println("ERROR");
                }
        });


        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ProductSummary.this, AddDiscount.class);
                intent.putExtra("idProduct",idProduct);
                if(ar.isEmpty())
                {
                intent.putExtra("idDepartment", null);
                }
                else{
                intent.putExtra("idDepartment", ar.get(0);
                }
                intent.putExtra("idDepartment",);
                startActivity(intent);*/
            }
        });

    }



}
