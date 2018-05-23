package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.content.Context;

import com.oledcomm.soft.androidlifisdk.ILiFiPosition;
import com.oledcomm.soft.androidlifisdk.LiFiSdkManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kevin on 03/05/2018.
 */

public class LampListener{

    LiFiSdkManager mLiFiSdkManager;

    public LampListener(Activity activity, Context context){
        initLiFiManager(activity, context);
    }

    private void initLiFiManager(Activity activity, final Context context){
        //Instantiating the manager, choosing the jack audio as input for the signal.
        mLiFiSdkManager = new LiFiSdkManager(context, LiFiSdkManager.SIMPLE_JACK_LIB_VERSION, "", "", new ILiFiPosition() {
            //Function trigerred whenever a new lamp signal is detected. The Json received is like { 'id' : X }
            @Override
            public void onLiFiPositionUpdate(JSONObject jsonObject) {
                //We are passing the received JSON to the LampController class which contains the business logic
                try {
                    LampController.getInstance().onNewLamp(jsonObject,context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //Init the manager
        mLiFiSdkManager.init(activity,"");
        //Start the manager
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
