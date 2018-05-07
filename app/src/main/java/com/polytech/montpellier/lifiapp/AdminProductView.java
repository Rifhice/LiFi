package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.oledcomm.soft.lifiapp.R;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminProductView extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_display);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
