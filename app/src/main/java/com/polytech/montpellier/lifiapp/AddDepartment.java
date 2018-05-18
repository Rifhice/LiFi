package com.polytech.montpellier.lifiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_department);
        //Check for the internet
        Helper.hasActiveInternetConnection(this);

        //TextField for the name
        final EditText text =  findViewById(R.id.nametf);
        text.setText(getResources().getString(R.string.newDepartement));
        Button validate = findViewById(R.id.validate);
        //Action on click
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the text field is empty we show error
                if (!text.getText().toString().isEmpty()) {
                    //We create a department with a default id and a name corresponding to the textfield
                    Department dep = new Department(0, text.getText().toString());
                    //We insert the department in the database
                    AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO().create(dep, UserConnection.getInstance().getToken(), new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {
                            //We check if the response is a JSON
                            if (object instanceof JSONObject) {
                                JSONObject obj = (JSONObject) object;
                                try {
                                    //If the code is 200, the request succeeded
                                    if (obj.getInt("code") == 200) {
                                        //If we inserted more than one line, it succeeded
                                        if (obj.getInt("insertId") != 0) {
                                            finish();
                                        } else {
                                            new AlertDialog.Builder(context)
                                                    .setTitle(getResources().getString(R.string.error))
                                                    .setMessage(getResources().getString(R.string.erroroccured))
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                                        public void onClick(DialogInterface dialog, int whichButton) {

                                                        }}).show();
                                        }
                                    } else {
                                        new AlertDialog.Builder(context)
                                                .setTitle(getResources().getString(R.string.error))
                                                .setMessage(getResources().getString(R.string.erroroccured))
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int whichButton) {

                                                    }}).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
                else{
                    //If the textfield is empty
                    if(text.getText().toString().isEmpty()) {
                        //We show an error
                        text.setError(getResources().getString(R.string.name) + getResources().getString(R.string.leftBlank));
                    }
                }
            }

        });

    }

    @Override
    public void onBackPressed(){
        //Notify the user that he will lose it's modification
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
