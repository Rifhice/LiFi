package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

/**
 * Created by Kevin on 03/05/2018.
 */

public class Login extends AppCompatActivity {

    EditText password;
    Button validate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Intent intent = getIntent();
        validate   = (Button)findViewById(R.id.validate);
        password   = (EditText)findViewById(R.id.passwordText);

        validate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UserConnection.getInstance().login(password.getText().toString(), new ResponseHandler() {
                    @Override
                    public void onSuccess(Object object) {
                       loginSuccess();
                    }

                    @Override
                    public void onError(Object object) {
                        loginError();
                    }
                });
                    return true ;
            }
        });
    }

    public void loginSuccess(){
        System.out.println("Login success");
        Intent intent = new Intent(Login.this, AdminActivity.class);
        startActivity(intent);
    }

    public void loginError(){
        AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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

}
