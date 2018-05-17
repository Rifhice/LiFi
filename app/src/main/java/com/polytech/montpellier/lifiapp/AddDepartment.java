package com.polytech.montpellier.lifiapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDepartment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_department);
        Helper.hasActiveInternetConnection(this);
        final EditText text =  findViewById(R.id.nametf);
        text.setText(getResources().getString(R.string.newDepartement));
        Button validate = findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!text.getText().toString().isEmpty()) {
                    Department dep = new Department(0, text.getText().toString());
                    AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO().create(dep, UserConnection.getInstance().getToken(), new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {
                            if (object instanceof JSONObject) {
                                JSONObject obj = (JSONObject) object;
                                try {
                                    if (obj.getInt("code") == 200) {
                                        if (obj.getInt("insertId") != 0) {
                                            finish();
                                        } else {
                                            System.out.println("Error inserting");
                                        }
                                    } else {
                                        System.out.println("Error inserting");
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
                }
                else{
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AddDepartment.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.alert));
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setMessage(getResources().getString(R.string.blankFieldMessage));
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

        });

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.addDepartement))
                .setMessage(getResources().getString(R.string.addLeaveMessage))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}
