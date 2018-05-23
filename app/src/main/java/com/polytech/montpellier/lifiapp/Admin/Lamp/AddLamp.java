package com.polytech.montpellier.lifiapp.Admin.Lamp;

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

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.UserConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class AddLamp extends AppCompatActivity {

    int idDep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp_add);
        //Check for the internet
        Helper.hasActiveInternetConnection(this);

        //Retrieve arguments from previous page
        Intent intent = getIntent();
        final int id = intent.getIntExtra("lamp",0);

        //Retrieve graphic elements
        final EditText text = (EditText) findViewById(R.id.nametf);
        text.setText("");
        Button validate = (Button)findViewById(R.id.validate);

        //Action on click for the button validate
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Field verification
                if (!text.getText().toString().isEmpty()) {
                    //Creating object according to data in form
                    Lamp lamp = new Lamp(id, text.getText().toString(), new Department(idDep));
                    //Inserting the lamp in the database
                    AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getLampDAO().create(lamp, UserConnection.getInstance().getToken(), new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {
                            finish();
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });
                }
                else{
                    if(text.getText().toString().isEmpty()) {
                        text.setError(getResources().getString(R.string.name) + getResources().getString(R.string.leftBlank));
                    }
                }
            }
        });

        //Initialize the department spinner
        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.spinner_department);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();

        //Get all the department to populate the spinner
        AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDepartmentDAO().getAll(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {

                if (object instanceof ArrayList) {

                    ArrayList<Department> array = (ArrayList<Department>) object;
                    for (int i = 0; i < array.size(); i++) {
                        Department department = array.get(i);
                        dep.add(department.getName());
                        depMap.put(department.getName(), department.getId());
                    }
                    adapter.setDropDownViewResource(R.layout.list);
                    spinnerDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });

        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String depName = (String) spinnerDepartment.getSelectedItem();
                idDep = depMap.get(depName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.newLamp))
                .setMessage(getResources().getString(R.string.addLeaveMessage))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}
