package com.polytech.montpellier.lifiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;

/**
 * Created by Kevin on 04/04/2018.
 */

public class FirstLamp extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_lamp);
        Intent intent = getIntent();
        TextView idText = (TextView) findViewById(R.id.lampId);
        if(intent != null){
            idText.setText("Lampe : #" + intent.getStringExtra("id_lamp"));
        }

    }
}
