package com.polytech.montpellier.lifiapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    TextView testText;
    ImageView logo;
    private long time;
    private int nbClick = 0;
    private int milliReset = 2000;
    private int nbClickOk = 7;
    private static LampListener listener = null;
    final static int PERMISSION_REQUEST_AUDIO = 1;
    public static String url;


    public void instanciateListener(){
        if(listener == null){
            listener = new LampListener(this,getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        url = res.getString(R.string.url);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                    if(UserConnection.getInstance().isConnected()) {
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Password");
                        final EditText input = new EditText(context);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserConnection.getInstance().login(input.getText().toString(), new ResponseHandler() {
                                    @Override
                                    public void onSuccess(Object object) {
                                        Intent intent = new Intent(context, AdminActivity.class);
                                        context.startActivity(intent);
                                    }

                                    @Override
                                    public void onError(Object object) {
                                        //input.setError(getResources().getString(R.string.loginFailed));
                                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                        alertDialog.setMessage("Bad Password");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Android M Permission check

            if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(getResources().getString(R.string.audioAccessPermition));
                builder.setMessage(getResources().getString(R.string.audioAccessPermissionMessage));
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_AUDIO: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    instanciateListener();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getResources().getString(R.string.functionalityLimited));
                    builder.setMessage(getResources().getString(R.string.noAudioNoLifi));
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
