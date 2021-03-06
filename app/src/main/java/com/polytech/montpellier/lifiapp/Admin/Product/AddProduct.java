package com.polytech.montpellier.lifiapp.Admin.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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


public class AddProduct extends AppCompatActivity{

    //Declaration Component
    EditText editText_price;
    EditText editText_brand;
    EditText editText_name;
    MultiAutoCompleteTextView editText_description;
    Button validate;

    //Declaration variables
    Float price;
    String brand;
    String description;
    String name;
    int idDep = -1;

    //Declaration DAO
    DepartmentDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDepartmentDAO();
    ProductDAO daoP = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getProductDAO();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        Helper.getInstance(this);
        //Check for the internet
        Helper.hasActiveInternetConnection(this);
        initializeUI();

        //Retrieve graphical elements
        editText_price = (EditText) findViewById(R.id.editText_product_addPrice);
        editText_brand = (EditText) findViewById(R.id.editText_product_addBrand);
        editText_name = (EditText) findViewById(R.id.editText_product_addName);
        editText_description = (MultiAutoCompleteTextView) findViewById(R.id.multiTextView_product_addDescription);

        validate = (Button) findViewById(R.id.button_addProduct);
        validate.setText(R.string.add);
        validate.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Form verification
                if (!editText_name.getText().toString().isEmpty() && !editText_description.getText().toString().isEmpty()
                        && !editText_brand.getText().toString().isEmpty() && !editText_price.getText().toString().isEmpty() && idDep>=0) {

                    name = editText_name.getText().toString();
                    description = editText_description.getText().toString();
                    price = Float.parseFloat(editText_price.getText().toString());
                    brand = editText_brand.getText().toString();
                    //Creating the product according to the data in the form
                    Product product = new Product(0, name, description, price, brand, new Department(idDep));
                    //Insert the product in the database
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
                    if(editText_name.getText().toString().isEmpty()) {
                        editText_name.setError( getResources().getString(R.string.name) + getResources().getString(R.string.leftBlank));
                    }
                    if(editText_brand.getText().toString().isEmpty()) {
                        editText_brand.setError(getResources().getString(R.string.marque) +getResources().getString(R.string.leftBlank));
                    }
                    if(editText_description.getText().toString().isEmpty()) {
                        editText_description.setError(getResources().getString(R.string.description) + getResources().getString(R.string.leftBlank));
                    }
                    if(editText_price.getText().toString().isEmpty()) {
                        editText_price.setError(getResources().getString(R.string.price) + getResources().getString(R.string.leftBlank));
                    }
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
        //Initialize the department spinner
        final Spinner spinnerDepartment = findViewById(R.id.spinner_ProductDepartement);
        final ArrayList<String> dep = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();
        //Get all the deparment to populate the spinner
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

        //Get the ID of the selected element of the spinner
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
