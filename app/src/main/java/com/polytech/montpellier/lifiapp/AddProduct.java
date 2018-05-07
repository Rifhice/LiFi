package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.Helper;

public class AddProduct extends AppCompatActivity {
    TextView textView_name;
    TextView textView_brand;
    TextView textView_price;
    TextView textView_description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);

        Helper.getInstance(this);

        textView_brand = (TextView)findViewById(R.id.textView_product_addBrand);
        textView_name = (TextView)findViewById(R.id.textView_product_addName);
        textView_description = (TextView)findViewById(R.id.textView_product_addDescription);
        textView_price = (TextView)findViewById(R.id.textView_product_addPrice);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed(){

    }

}
