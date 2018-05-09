package com.polytech.montpellier.lifiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.oledcomm.soft.lifiapp.R;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminProductView extends AppCompatActivity implements AdminTab {

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

    @Override
    public void openNewLampPopUp(final int lamp) {
        new AlertDialog.Builder(this)
                .setTitle("New Lamp")
                .setMessage("You are standing under a new lamp, do you want to add it ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.out.println("It works " + lamp);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}
