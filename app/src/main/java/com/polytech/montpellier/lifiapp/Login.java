package com.polytech.montpellier.lifiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;

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
                if(UserConnection.getInstance().login(password.getText().toString())){
                    loginSuccess();
                    return true;
                }
                return false;
            }
        });
    }

    public void loginSuccess(){
        System.out.println("Login success");
        Intent intent = new Intent(Login.this, AdminActivity.class);
        startActivity(intent);
    }

}
