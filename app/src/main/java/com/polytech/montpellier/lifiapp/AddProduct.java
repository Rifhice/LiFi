package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;
import com.polytech.montpellier.lifiapp.Model.Product;

import android.widget.AdapterView.*;
import java.util.Map.Entry;
import java.util.Set;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddProduct extends AppCompatActivity{

    EditText editText_price;
    EditText editText_brand;
    EditText editText_name;
    MultiAutoCompleteTextView editText_description;

    Float price;
    //String price;
    String brand;
    String description;
    String name;
    Button validate;
    int idDep = -1;
    DepartmentDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO();
    ProductDAO daoP = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        Helper.getInstance(this);
        initializeUI();

        editText_price = (EditText) findViewById(R.id.editText_product_addPrice);
        editText_brand = (EditText) findViewById(R.id.editText_product_addBrand);
        editText_name = (EditText) findViewById(R.id.editText_product_addName);
        editText_description = (MultiAutoCompleteTextView) findViewById(R.id.multiTextView_product_addDescription);

        validate = (Button) findViewById(R.id.button_addProduct);

        validate.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!editText_name.getText().toString().isEmpty() && !editText_description.getText().toString().isEmpty()
                        && !editText_brand.getText().toString().isEmpty() && !editText_price.getText().toString().isEmpty() && idDep>=0) {

                    name = editText_name.getText().toString();
                    description = editText_description.getText().toString();
                    price = Float.parseFloat(editText_price.getText().toString());
                    brand = editText_brand.getText().toString();


                    Product product = new Product(0, name, description, price, brand, new Department(idDep));
                    daoP.create(product, UserConnection.getInstance().getToken(), new ResponseHandler() {
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
                    //TODO: faire une alerte si un des champs est vide
                    System.out.println("Champs vides");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.newProduct))
                .setMessage(getResources().getString(R.string.addLeaveMessage))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void initializeUI() {
        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.spinner_ProductDepartement);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();

        //TODO vérifier departement non vide
        dao.getAll(new ResponseHandler() {

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

        spinnerDepartment.setOnItemSelectedListener(new OnItemSelectedListener() {
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

}
