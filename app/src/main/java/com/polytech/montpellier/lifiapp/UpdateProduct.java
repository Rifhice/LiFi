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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProduct extends AppCompatActivity {
    int idDep = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("product",0);
        final String name = intent.getStringExtra("name");
        final String brand = intent.getStringExtra("brand");
        final EditText text_name = (EditText) findViewById(R.id.editText_product_addName);
        final EditText text_brand = (EditText) findViewById(R.id.editText_product_addBrand);
        final MultiAutoCompleteTextView text_description = (MultiAutoCompleteTextView) findViewById(R.id.multiTextView_product_addDescription);
        final EditText text_price = (EditText) findViewById(R.id.editText_product_addPrice);

        text_name.setText(name);
        text_brand.setText(brand);

        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.spinner_ProductDepartement);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();


        //TODO vérifier departement non vide
        AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO().getAll(new ResponseHandler() {

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

        AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO().getById(id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;
                    final String department;

                        try {
                            JSONObject current = array.getJSONObject(0);
                            //new Product(current.getInt(\"idProduct\"), current.getString(\"name\"),  current.getString(\"description"),  Float.parseFloat(current.getString("price")),  current.getString("brand"),new Department(current.getInt("idDepartment"), current.getString("name"))));
                            text_price.setText(current.getString("price"));
                            text_description.setText(current.getString("description"));
                            department = current.getString("department");
                            spinnerDepartment.setSelection(dep.indexOf(department));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                }
            }



            @Override
            public void onError(Object object) {

            }
        });




        Button validate = (Button)findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text_name.getText().toString().isEmpty() && !text_description.getText().toString().isEmpty()
                        && !text_brand.getText().toString().isEmpty() && !text_price.getText().toString().isEmpty() && idDep>=0) {



                    Product product = new Product(0, text_name.getText().toString(), text_description.getText().toString(), Float.parseFloat(text_price.getText().toString()), text_brand.getText().toString(), new Department(idDep));
                    //System.out.println("Name: " + name +" Description: " + description + " price: " + price + " brand: " + brand + " idDep : " +idDep);
                    AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO().update( product, UserConnection.getInstance().getToken(), new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {
                            System.out.println(object.toString());
                            finish();
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });

                }
                else{
                    //TODO: faire une alerte si un des champs est vide
                    System.out.println("Champs vides");
                }

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
                .setTitle("New Lamp")
                .setMessage("You are leaving, are you sure ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
