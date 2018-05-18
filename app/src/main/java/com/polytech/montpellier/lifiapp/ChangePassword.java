package com.polytech.montpellier.lifiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

public class ChangePassword  extends AppCompatActivity {

    final Context context = this;

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
            UserConnection.getInstance().changePassword(new1.getText().toString(), new ResponseHandler() {
                @Override
                public void onSuccess(Object object) {
                    finish();
                }

                @Override
                public void onError(Object object) {
                    new AlertDialog.Builder(context)
                            .setTitle(getResources().getString(R.string.error))
                            .setMessage(getResources().getString(R.string.erroroccured))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                }}).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
