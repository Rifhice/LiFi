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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oledcomm.soft.androidlifisdk.ILiFiPosition;
import com.oledcomm.soft.androidlifisdk.LiFiSdkManager;
import com.oledcomm.soft.lifiapp.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView testText;
    LiFiSdkManager mLiFiSdkManager;
    ImageView logo;
    private long time;
    private int nbClick = 0;
    private int milliReset = 2000;
    private int nbClickOk = 7;


    final static int PERMISSION_REQUEST_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    setContentView(R.layout.login_layout);
                }
            }
        });


        //TODO: initialize all & register LiFi callback

//        mLiFiSdkManager.start();
//        mLiFiSdkManager.init(this,"");

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
                initLiFiManager();

            }

        }else{
            initLiFiManager();

        }


        //TODO: call an api - GetBuildingInfo & api callback
        JSONObject sendJson = new JSONObject();

//     GetBuildingInfoApi sna = new GetBuildingInfoApi(sendJson);

//        GetFloorInfoApi gfi = new GetFloorInfoApi(sendJson);
//        try {
//            sendJson.put("id", "1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        GetSubzoneInfoApi gsia = new GetSubzoneInfoApi(sendJson);
//        try {
//            sendJson.put("id", "1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        mLiFiSdkManager.register(sna,new IApiListener(){
//            @Override
//            public void onApiResult(JSONObject jsonObject){
//                testText.append("\nonApiResult:"+jsonObject.toString());
//            }
//
//            @Override
//            public void onApiResult(JSONArray jsonArray) {
//                testText.append("\nonApiResult:"+jsonArray.toString());
//            }
//        });
//        mLiFiSdkManager.send();



    }

    @Override
     protected void onResume() {
        super.onResume();
        if(mLiFiSdkManager!=null) {
            if (!mLiFiSdkManager.isStarted()) {
                mLiFiSdkManager.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLiFiSdkManager!=null) {
            if (mLiFiSdkManager.isStarted()) {
                mLiFiSdkManager.stop();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLiFiSdkManager!=null) {
            mLiFiSdkManager.release();
        }
        mLiFiSdkManager = null;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_AUDIO: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "coarse location permission granted");
                    mLiFiSdkManager = new LiFiSdkManager(getApplicationContext(), LiFiSdkManager.SIMPLE_JACK_LIB_VERSION, "", "", new ILiFiPosition() {
                        @Override
                        public void onLiFiPositionUpdate(JSONObject jsonObject) {
                            testText.append("\nonLiFiPositionUpdate:"+jsonObject.toString());
                        }
                    });
                    mLiFiSdkManager.init(this,"");
                    mLiFiSdkManager.start();
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

    private void initLiFiManager(){
        mLiFiSdkManager = new LiFiSdkManager(getApplicationContext(), 1, "", "", new ILiFiPosition() {

            @Override
            public void onLiFiPositionUpdate(JSONObject jsonObject) {
                testText.append("\nLiFi reçu: id = "+jsonObject.toString());
                if(jsonObject.has("id")){
                    try {
                        testText.append("\nLiFi reçu: id = "+jsonObject.get("id"));
                        Intent intent = new Intent(MainActivity.this, FirstLamp.class);
                        intent.putExtra("id_lamp", jsonObject.get("id").toString());
                        startActivity(intent);
                        //TODO: implement your code here to treat LiFi id event

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    testText.append("\nSortie de la zone LiFi");
                    //TODO: implement your code here to treat outside LiFi event
                }

            }
        });
        mLiFiSdkManager.init(this,"");
        mLiFiSdkManager.start();
    }
}
