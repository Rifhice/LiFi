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
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
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
    DepartmentDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO();
    ProductDAO daoP = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("product",0);
        //System.out.print("id du produit:" +id);
        final String name = intent.getStringExtra("name");
        final String brand = intent.getStringExtra("brand");
        final String price = intent.getStringExtra("price");
        final EditText text_name = (EditText) findViewById(R.id.editText_product_addName);
        final EditText text_brand = (EditText) findViewById(R.id.editText_product_addBrand);
        final MultiAutoCompleteTextView text_description = (MultiAutoCompleteTextView) findViewById(R.id.multiTextView_product_addDescription);
        final EditText text_price = (EditText) findViewById(R.id.editText_product_addPrice);
        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.spinner_ProductDepartement);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();

        text_name.setText(name);
        text_brand.setText(brand);
        text_price.setText(price);




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

        daoP.getById(id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {

                if (object instanceof ArrayList) {

                    ArrayList<Product> array = (ArrayList<Product>) object;
                    final String department;
                    Product product = array.get(0);
                    //System.out.println(" HIH Description : " + product.getDescription() + "Departement : " + product.getDepartment().getName());
                    text_description.setText(product.getDescription());

                    department = product.getDepartment().getName();
                    spinnerDepartment.setSelection(dep.indexOf(department));



                }

            }
            @Override
            public void onError(Object object) {
                System.out.println("ERROR");
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


        Button validate = (Button)findViewById(R.id.button_addProduct);
        validate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!text_name.getText().toString().isEmpty() && !text_description.getText().toString().isEmpty()
                        && !text_brand.getText().toString().isEmpty() && !text_price.getText().toString().isEmpty() && idDep>=0) {

                    System.out.println("Cliiiick");

                    Product product = new Product(id, text_name.getText().toString(), text_description.getText().toString(), Float.parseFloat(text_price.getText().toString()), text_brand.getText().toString(), new Department(idDep));
                    System.out.println("ID: " + product.getId() + "Name: " + product.getName() +" Description: " + product.getDescription() + " price: " + product.getPrice() + " brand: " + product.getBrand() + " idDep : " +product.getDepartment().getId());
                    daoP.update( product, UserConnection.getInstance().getToken(), new ResponseHandler() {
                        @Override
                        public void onSuccess(Object object) {

                            System.out.println(object.toString());
                            finish();
                        }

                        @Override
                        public void onError(Object object) {
                            System.out.println("ERROR");
                        }
                    });

                }
                else{
                    //TODO: faire une alerte si un des champs est vide

                }

            }
        });







    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Update Product")
                .setMessage("You are leaving, are you sure ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
