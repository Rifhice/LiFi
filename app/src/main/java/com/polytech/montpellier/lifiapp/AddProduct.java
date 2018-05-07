package com.polytech.montpellier.lifiapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oledcomm.soft.androidlifisdk.ILiFiPosition;
import com.oledcomm.soft.androidlifisdk.LiFiSdkManager;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity{
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
