package com.polytech.montpellier.lifiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateDepartment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_department);
        final EditText text = (EditText) findViewById(R.id.nametf);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final int id = intent.getIntExtra("id",-1);
        text.setText(name);
        Button validate = (Button)findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Department dep = new Department(id,text.getText().toString());
                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO().update(dep,UserConnection.getInstance().getToken(), new ResponseHandler() {
                    @Override
                    public void onSuccess(Object object) {
                        if (object instanceof JSONObject) {
                            JSONObject obj = (JSONObject)object;
                            try {
                                if (obj.getInt("code") == 200) {
                                    if(obj.getInt("affectedRows") != 0){
                                        finish();
                                    }
                                    else{
                                        System.out.println("Error updating");
                                    }
                                } else {
                                    System.out.println("Error updating");
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
        });

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Update Department")
                .setMessage("You are leaving, are you sure ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}
