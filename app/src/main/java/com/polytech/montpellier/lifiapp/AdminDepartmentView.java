package com.polytech.montpellier.lifiapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminDepartmentView extends AppCompatActivity implements AdminTab {

    final Context context = this;
    DepartmentDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO();

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
                    ArrayList<Department> array = (ArrayList<Department>)object;
                    for(int i = 0 ; i < array.size() ; i++) {
                        Department department = array.get(i);
                        final TableRow row = new TableRow(context);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setId(department.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);

                        final TextView label_lamp = new TextView(context);
                        label_lamp.setText(department.getName());
                        label_lamp.setTextColor(Color.BLACK);
                        label_lamp.setPadding(5, 5, 5, 5);
                        label_lamp.setWidth(tl.getWidth() / 3);
                        row.addView(label_lamp);


                        Button delete = new Button(context);
                        delete.setText("delete");
                        delete.setWidth(tl.getWidth() / 3);
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
                        update.setWidth(tl.getWidth() / 3);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminDepartmentView.this, UpdateLamp.class);
                                intent.putExtra("name",label_lamp.getText());
                                intent.putExtra("department",row.getId());
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
