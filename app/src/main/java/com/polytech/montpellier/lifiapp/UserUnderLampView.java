package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

import android.support.v7.appcompat.* ;
import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Kevin on 03/05/2018.
 */

public class UserUnderLampView extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_under_lamp);
        //TextView text = (TextView)findViewById(R.id.text);
        //text.setText("salut");

        TableLayout tl = (TableLayout) findViewById(R.id.promotionsJourTable);

        TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        TextView label_header = new TextView(this);
        label_header.setId(200);
        label_header.setText(R.string.promotionsJour);
        label_header.setTextColor(Color.WHITE);
        label_header.setPadding(5, 5, 5, 5);
        tr_head.addView(label_header);// add the column to the table row here


        tl.addView(tr_head, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));


        Helper.getInstance(this);
        String url = "http://81.64.139.113:1337/api/Discount";

        Helper.getInstance(this).GET(url, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("GET 1"+object.toString());
                JSONArray getJSON = new JSONArray();
                getJSON = (JSONArray) object;

                for (int i = 0; i < getJSON.length(); i++) {
                    try {
                        JSONObject jsonObj = getJSON.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Object object) {

            }
        });



    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
