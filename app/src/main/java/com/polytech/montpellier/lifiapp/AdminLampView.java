package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminLampView extends Activity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp_display);
        final TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        Helper.getInstance().GET("http://81.64.139.113:1337/api/Lamp", new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if(object instanceof JSONArray){
                    JSONArray array = (JSONArray)object;
                    for(int i = 0; i < array.length(); i++){
                        try {
                            JSONObject current = array.getJSONObject(i);
                            Department department = null;
                            if(current.get("idDepartment") != null){
                                department = new Department(current.getInt("idDepartment"),current.getString("nameDepartment"));
                            }
                            Lamp lamp = new Lamp(current.getInt("idLamp"),current.getString("nameLamp"),department);

                            TableRow row = new TableRow(context);
                            row.setLayoutParams(new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.FILL_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                            TextView label_lamp = new TextView(context);
                            label_lamp.setText(lamp.getName());
                            label_lamp.setTextColor(Color.BLACK);
                            label_lamp.setPadding(5, 5, 5, 5);
                            row.addView(label_lamp);

                            TextView label_department = new TextView(context);
                            String text;
                            if(lamp.getDepartment() != null) {
                                 text = lamp.getDepartment().getName();
                            }
                            else{
                                text = "N/A";
                            }
                            label_department.setText(text);
                            label_department.setTextColor(Color.BLACK); // set the color
                            label_department.setPadding(5, 5, 5, 5); // set the padding (if required)
                            row.addView(label_department); // add the column to the table row here

                            Button delete = new Button(context);
                            delete.setText("del");
                            row.addView(delete);

                            Button update = new Button(context);
                            update.setText("upd");
                            row.addView(update);

                            tl.addView(row, new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.FILL_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    System.out.println("Malformed response");
                }
            }

            @Override
            public void onError(Object object) {
                System.out.println("error get lamp");
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
