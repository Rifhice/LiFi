package com.polytech.montpellier.lifiapp;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.appcompat.* ;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;


import com.oledcomm.soft.lifiapp.R;

/**
 * Created by Kevin on 03/05/2018.
 */

public class UserUnderLampView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_under_lamp);
        TextView text = (TextView)findViewById(R.id.text);
        text.setText("salut");

        TableLayout tl = (TableLayout) findViewById(R.id.promotionsJourTable);





    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
