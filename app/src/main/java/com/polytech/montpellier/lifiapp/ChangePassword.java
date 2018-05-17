package com.polytech.montpellier.lifiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import android.support.v7.app.AppCompatActivity;

public class ChangePassword  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
    }

    public void validate(View view){
        EditText current = (EditText) findViewById(R.id.current);
        EditText new1 = (EditText) findViewById(R.id.new1);
        EditText new2 = (EditText) findViewById(R.id.new2);
        if(!new1.getText().toString().equals(new2.getText().toString())){
            //New password not equals
        }
        else{
            System.out.println("CHANGE PASSWORD");
            UserConnection.getInstance().changePassword(new1.getText().toString(), new ResponseHandler() {
                @Override
                public void onSuccess(Object object) {
                    System.out.println(object);
                    finish();
                }

                @Override
                public void onError(Object object) {
                    System.out.println("ERRORREQUEST : " + object);
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
