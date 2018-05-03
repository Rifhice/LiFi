package com.polytech.montpellier.lifiapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.oledcomm.soft.androidlifisdk.ILiFiPosition;
import com.oledcomm.soft.androidlifisdk.LiFiSdkManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kevin on 03/05/2018.
 */

public class LampListener {

    LiFiSdkManager mLiFiSdkManager;

    public LampListener(Activity activity, Context context){
        initLiFiManager(activity, context);
    }

    private void initLiFiManager(Activity activity, Context context){
        mLiFiSdkManager = new LiFiSdkManager(context, LiFiSdkManager.SIMPLE_JACK_LIB_VERSION, "", "", new ILiFiPosition() {

            @Override
            public void onLiFiPositionUpdate(JSONObject jsonObject) {
                try {
                    LampController.getInstance().onNewLamp(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mLiFiSdkManager.init(activity,"");
        mLiFiSdkManager.start();
    }

    public void setBehaviour(ILiFiPosition behaviour){

    }

    public void resume(){
        if(mLiFiSdkManager!=null) {
            if (!mLiFiSdkManager.isStarted()) {
                mLiFiSdkManager.start();
            }
        }
    }

    public void pause(){
        if(mLiFiSdkManager!=null) {
            if (mLiFiSdkManager.isStarted()) {
                mLiFiSdkManager.stop();
            }
        }
    }

    public void destroy(){
        if(mLiFiSdkManager!=null) {
            mLiFiSdkManager.release();
        }
        mLiFiSdkManager = null;
    }

}
