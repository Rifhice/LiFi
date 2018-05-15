package com.polytech.montpellier.lifiapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
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
//import com.oledcomm.soft.androidlifisdk.ILiFiPosition;
//import com.oledcomm.soft.androidlifisdk.LiFiSdkManager;
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

public class MainActivity extends AppCompatActivity {

    TextView testText;
    ImageView logo;
    private long time;
    private int nbClick = 0;
    private int milliReset = 2000;
    private int nbClickOk = 7;
    private static LampListener listener = null;
    final static int PERMISSION_REQUEST_AUDIO = 1;

    public void instanciateListener(){
        if(listener == null){
            listener = new LampListener(this,getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helper.getInstance(this);

        testText = (TextView)findViewById(R.id.testText);
        logo = (ImageView) findViewById(R.id.imageView_logo_main);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nbClick == 0){
                    nbClick=+1;
                    time = Calendar.getInstance().getTimeInMillis();
                }
                else{
                    long rightNow = Calendar.getInstance().getTimeInMillis();
                    if(rightNow - time >= milliReset){
                        nbClick = 0;
                    }
                    else{
                        nbClick++;
                    }
                }
                if(nbClick == nbClickOk){
                    if(!UserConnection.getInstance().isConnected()) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Android M Permission check

            if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("This app needs audio record access");
                builder.setMessage("Please grant audio record access so this app can detect LiFi.");
                builder.setPositiveButton(android.R.string.ok, null);

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_AUDIO);
                    }


                });

                builder.show();

            }else{
                instanciateListener();
            }

        }else{
            instanciateListener();
        }
    }

    @Override
     protected void onResume() {
        super.onResume();
        /*
        if(listener != null) {
            listener.resume();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();/*
        if(listener != null) {
            listener.pause();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        if(listener != null) {
            listener.destroy();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_AUDIO: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "coarse location permission granted");
                    instanciateListener();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since audio access has not been granted, this app will not be able to discover LiFi.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
