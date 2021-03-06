package com.polytech.montpellier.lifiapp.Admin.Lamp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.UserConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateLamp extends AppCompatActivity {

    final Context context = this;
    int idDep = -1;

    //DAO declaration
    LampDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getLampDAO();
    DepartmentDAO daoD = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDepartmentDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp_add);
        //Check connection
        Helper.hasActiveInternetConnection(this);
        Helper.getInstance(this);
        //Get the information of the mmother page
        Intent intent = getIntent();
        final int id = intent.getIntExtra("lamp",0);
        final String name = intent.getStringExtra("name");
        final String department = intent.getStringExtra("name_department");
        final EditText text = (EditText) findViewById(R.id.nametf);
        text.setText(name);
        Button validate = (Button)findViewById(R.id.validate);
        //Button to validate the update of the lamp
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().isEmpty()) {
                    Lamp lamp = new Lamp(id, text.getText().toString(), new Department(idDep));
                    dao.update(lamp, UserConnection.getInstance().getToken(), new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {
                            finish();
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
                    if(text.getText().toString().isEmpty()) {
                        text.setError(getResources().getString(R.string.name) + getResources().getString(R.string.leftBlank));
                    }
                }
            }
        });

        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.spinner_department);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();

        //Get all departments
        daoD.getAll(new ResponseHandler() {

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
                    spinnerDepartment.setSelection(dep.indexOf(department));
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

        //Get the ID of the department selected with the spinner
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
                .setTitle(getResources().getString(R.string.updateLamp))
                .setMessage(getResources().getString(R.string.updateLeaveMessage))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
