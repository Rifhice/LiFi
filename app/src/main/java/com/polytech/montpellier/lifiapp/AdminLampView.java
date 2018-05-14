package com.polytech.montpellier.lifiapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminLampView extends AppCompatActivity implements AdminTab {

    final Context context = this;
    LampDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO();

    @Override
    protected void onResume() {
        super.onResume();
        updateDataAndView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lampall_display);
        updateDataAndView();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void openNewLampPopUp(final int lamp) {
        new AlertDialog.Builder(context)
                .setTitle("New Lamp")
                .setMessage("You are standing under a new lamp, do you want to add it ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(context, AddLamp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("lamp",lamp);
                        context.startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void updateDataAndView(){
        final TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        tl.removeAllViews();
        dao.getAll(new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if(object instanceof ArrayList) {
                    tl.removeAllViews();
                    ArrayList<Lamp> array = (ArrayList<Lamp>)object;
                    for(int i = 0 ; i < array.size() ; i++) {
                        Lamp lamp = array.get(i);
                        final TableRow row = new TableRow(context);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setId(lamp.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);

                        final TextView label_lamp = new TextView(context);
                        label_lamp.setText(lamp.getName());
                        label_lamp.setTextColor(Color.BLACK);
                        label_lamp.setPadding(5, 5, 5, 5);
                        label_lamp.setWidth(tl.getWidth() / 4);
                        row.addView(label_lamp);

                        final TextView label_department = new TextView(context);
                        String text;
                        if (lamp.getDepartment() != null) {
                            text = lamp.getDepartment().getName();
                        } else {
                            text = "N/A";
                        }
                        label_department.setText(text);
                        label_department.setTextColor(Color.BLACK); // set the color
                        label_department.setPadding(5, 5, 5, 5); // set the padding (if required)
                        label_department.setWidth(tl.getWidth() / 4);
                        row.addView(label_department); // add the column to the table row here

                        Button delete = new Button(context);
                        delete.setText("delete");
                        delete.setWidth(tl.getWidth() / 4);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Delete Lamp")
                                        .setMessage("Are you sure you want to delete this lamp?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO().delete(row.getId(), UserConnection.getInstance().getToken(), new ResponseHandler() {
                                                    @Override
                                                    public void onSuccess(Object object) {
                                                        if(object instanceof JSONObject){
                                                            System.out.println("JSON : " + object.toString());
                                                            JSONObject res = (JSONObject) object;
                                                            try {
                                                                if(res.getInt("affectedRows") != 0){
                                                                    tl.removeView(tl.findViewById(row.getId()));
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Object object) {

                                                    }
                                                });
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        });
                        row.addView(delete);

                        Button update = new Button(context);
                        update.setText("update");
                        update.setWidth(tl.getWidth() / 4);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminLampView.this, UpdateLamp.class);
                                intent.putExtra("name",label_lamp.getText());
                                intent.putExtra("lamp",row.getId());
                                intent.putExtra("name_department", label_department.getText());
                                startActivity(intent);
                            }
                        });
                        row.addView(update);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println(v.getId());
                            }
                        });
                        tl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
