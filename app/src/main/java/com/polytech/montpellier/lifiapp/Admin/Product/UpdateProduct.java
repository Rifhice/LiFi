package com.polytech.montpellier.lifiapp.Admin.Product;

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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Product;
import com.polytech.montpellier.lifiapp.UserConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProduct extends AppCompatActivity {
    int idDep = -1;
    final Context context = this;
    //Declaration DAO
    DepartmentDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDepartmentDAO();
    ProductDAO daoP = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getProductDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        Helper.hasActiveInternetConnection(this);
        Intent intent = getIntent();

        //initialization fields
        final int id = intent.getIntExtra("product",0);
        final String name = intent.getStringExtra("name");
        final String brand = intent.getStringExtra("brand");
        final String price = intent.getStringExtra("price");
        final EditText text_name =  findViewById(R.id.editText_product_addName);
        final EditText text_brand =  findViewById(R.id.editText_product_addBrand);
        final MultiAutoCompleteTextView text_description = findViewById(R.id.multiTextView_product_addDescription);
        final EditText text_price = findViewById(R.id.editText_product_addPrice);
        final Spinner spinnerDepartment =  findViewById(R.id.spinner_ProductDepartement);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();

        text_name.setText(name);
        text_brand.setText(brand);
        text_price.setText(price);


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
                new AlertDialog.Builder(context)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.erroroccured))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                            }}).show();
            }
        });

        daoP.getById(id, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {

                if (object instanceof ArrayList) {

                    ArrayList<Product> array = (ArrayList<Product>) object;
                    final String department;
                    Product product = array.get(0);
                    text_description.setText(product.getDescription());

                    department = product.getDepartment().getName();
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


        Button validate = findViewById(R.id.button_addProduct);
        validate.setText(R.string.update);
        validate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!text_name.getText().toString().isEmpty() && !text_description.getText().toString().isEmpty()
                        && !text_brand.getText().toString().isEmpty() && !text_price.getText().toString().isEmpty() && idDep>=0) {

                    Product product = new Product(id, text_name.getText().toString(), text_description.getText().toString(), Float.parseFloat(text_price.getText().toString()), text_brand.getText().toString(), new Department(idDep));
                    daoP.update( product, UserConnection.getInstance().getToken(), new ResponseHandler() {
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
                    if(text_name.getText().toString().isEmpty()) {
                        text_name.setError( getResources().getString(R.string.name) + getResources().getString(R.string.leftBlank));
                    }
                    if(text_brand.getText().toString().isEmpty()) {
                        text_brand.setError(getResources().getString(R.string.marque) +getResources().getString(R.string.leftBlank));
                    }
                    if(text_description.getText().toString().isEmpty()) {
                        text_description.setError(getResources().getString(R.string.description) + getResources().getString(R.string.leftBlank));
                    }
                    if(text_price.getText().toString().isEmpty()) {
                        text_price.setError(getResources().getString(R.string.price) + getResources().getString(R.string.leftBlank));
                    }

                }

            }
        });
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.updateProduct))
                .setMessage(getResources().getString(R.string.updateLeaveMessage))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
