package com.polytech.montpellier.lifiapp.Admin.Discount;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Product;
import com.polytech.montpellier.lifiapp.UserConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDiscount extends AppCompatActivity {

    final Context context = this;

    private Calendar startDate, endDate;
    private int currentId;

    //Potential idDepartment an product if coming from the product summary page
    private int idDepartment, idProduct;
    //All graphics elements

    private Button startButton, endButton;
    TextView percentageText;
    TextView boughtText;
    TextView freeText;
    EditText percentage;
    EditText bought;
    EditText free;
    CheckBox fidelity;
    Switch swi;
    boolean isPercentage = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount);
        //Check for internet
        Helper.hasActiveInternetConnection(this);

        //Get the intent to retreive potential arguments
        Intent intent = getIntent();
        //Retrieve potential argument (only get them if coming from the product summary page), value by default : -1
        final int baseIdProd = intent.getIntExtra("idProduct", -1);
        final int baseIdDep = intent.getIntExtra("idDepartment", -1);

        //Retrieve all the graphical elements
        percentageText = (TextView)findViewById(R.id.percentagetext);
        boughtText = (TextView)findViewById(R.id.boughttext);
        freeText = (TextView)findViewById(R.id.freetext);
        percentage = (EditText)findViewById(R.id.percentage);
        bought = (EditText)findViewById(R.id.bought);
        free = (EditText)findViewById(R.id.free);
        fidelity = (CheckBox)findViewById(R.id.checkBox);
        swi = (Switch) findViewById(R.id.switch1);

        startButton = (Button) findViewById(R.id.endDateButton);
        endButton = (Button) findViewById(R.id.startDateButton);

        //Set up the end date and start date to today
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        startButton.setText(dateToString(startDate));
        endButton.setText(dateToString(endDate));

        //Iniatialize the spinner
        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.departmentSpinner);
        //ArrayList containint all the deparment name
        final ArrayList<String> dep = new ArrayList<>();
        //Adapter used for the layout of the spinner
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list,R.id.list1, dep);
        //Hashmap containing as keys the name of department and as value their id in the database
        final HashMap<String, Integer> depMap=new HashMap<String, Integer>();

        //Uses the DAO to get all department to then populate the spinner
        AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDepartmentDAO().getAll(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                //On response we populate the arraylist and the hashmap
                if (object instanceof ArrayList) {

                    ArrayList<Department> array = (ArrayList<Department>) object;
                    for (int i = 0; i < array.size(); i++) {
                        Department department = array.get(i);
                        //Populate the name arraylist
                        dep.add(department.getName());
                        //Populate the hashmap
                        depMap.put(department.getName(), department.getId());
                    }
                    //Set the layout of the adapter
                    adapter.setDropDownViewResource(R.layout.list);
                    spinnerDepartment.setAdapter(adapter);

                    //If we are coming from the poduct summary page, we preselect the department corresponding to the product
                    if(baseIdDep != -1) {
                        //Search in the hashmap by value to retrieve the key
                        for (Map.Entry<String, Integer> entry : depMap.entrySet()) {
                            if (entry.getValue().equals(baseIdDep)) {
                                spinnerDepartment.setSelection(dep.indexOf(entry.getKey()));
                            }
                        }
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

        //Function trigerred whenever a element is selected in the spinner
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //We retrieve the name of the current element
                String depName = (String) spinnerDepartment.getSelectedItem();
                //Set the id of the current element
                idDepartment = depMap.get(depName);

                idProduct = -1;
                Department current = new Department(idDepartment, depName);

                //Set up the second spinner for the products the same way than above
                final Spinner spinnerProduct = (Spinner) findViewById(R.id.productSpinner);
                final ArrayList<String> prod = new ArrayList<>();
                final ArrayAdapter<String> prodAdapter = new ArrayAdapter<String>( context, R.layout.list,R.id.list1, prod);
                final HashMap<String, Integer> prodMap = new HashMap<String, Integer>();

                //Populating the spinner with all the products in the department
                AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDepartmentDAO().getAllProducts(current, new ResponseHandler() {

                    @Override
                    public void onSuccess(Object object) {

                        if (object instanceof ArrayList) {
                            ArrayList<Product> array = (ArrayList<Product>) object;
                            for (int i = 0; i < array.size(); i++) {
                                Product product= array.get(i);
                                prod.add(product.getName());
                                prodMap.put(product.getName(), product.getId());
                            }
                            prodAdapter.setDropDownViewResource(R.layout.list);
                            spinnerProduct.setAdapter(prodAdapter);
                            if(baseIdProd != -1) {
                                for (Map.Entry<String, Integer> entry : prodMap.entrySet()) {
                                    if (entry.getValue().equals(baseIdProd)) {
                                        spinnerProduct.setSelection(prod.indexOf(entry.getKey()));
                                    }
                                }
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

                spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String productName = (String) spinnerProduct.getSelectedItem();
                        idProduct = prodMap.get(productName);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        //Updatetype allow to adapt the UI to the selected type of discount
        updateType(isPercentage);
        //Whenever the switch changes value, we change the ui and the attribute accordingly
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   isPercentage = true;
                   updateType(isPercentage);
               }
               else{
                   isPercentage = false;
                   updateType(isPercentage);
               }
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDateStart(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    public void setDateEnd(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        currentId = id;
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        }
        else if(id == 998){
            return new DatePickerDialog(this,
                    myDateListener,  endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        if(currentId == 999) {
            startDate.set(Calendar.YEAR, year);
            startDate.set(Calendar.MONTH, month);
            startDate.set(Calendar.DAY_OF_MONTH, day);
            startButton.setText(dateToString(startDate));
        }
        else{
            endDate.set(Calendar.YEAR, year);
            endDate.set(Calendar.MONTH, month);
            endDate.set(Calendar.DAY_OF_MONTH, day);
            endButton.setText(dateToString(endDate));
        }
    }

    public StringBuilder dateToString(Calendar calendar){
        if(calendar.get(Calendar.MONTH) < 10){
            return new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-0")
                    .append(calendar.get(Calendar.MONTH) + 1).append("-").append(calendar.get(Calendar.DAY_OF_MONTH));
        }
        return new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-")
                .append(calendar.get(Calendar.MONTH) + 1).append("-").append(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void updateType(boolean isPercentage){
        if(isPercentage){
            swi.setText("Percentage");
            bought.setVisibility(View.INVISIBLE);
            boughtText.setVisibility(View.INVISIBLE);
            free.setVisibility(View.INVISIBLE);
            freeText.setVisibility(View.INVISIBLE);
            percentage.setVisibility(View.VISIBLE);
            percentageText.setVisibility(View.VISIBLE);
        }
        else{
            swi.setText("Quantity");
            bought.setVisibility(View.VISIBLE);
            boughtText.setVisibility(View.VISIBLE);
            free.setVisibility(View.VISIBLE);
            freeText.setVisibility(View.VISIBLE);
            percentage.setVisibility(View.INVISIBLE);
            percentageText.setVisibility(View.INVISIBLE);
        }
    }

    //Function trigerred whenever the user clicks the "validate" button
    public void validate(View view){
        //If this function we initialize a discount according to the data in the form
        int fid = 0;
        if(fidelity.isChecked()){
            fid = 1;
        }
        Discount discount;
        if(endDate.getTimeInMillis()>startDate.getTimeInMillis()){
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AddDiscount.this).create();
            alertDialog.setTitle(getResources().getString(R.string.alert));
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setMessage(getResources().getString(R.string.dateWrong));
            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            if(isPercentage){
                if(!percentage.getText().toString().isEmpty()) {
                    discount = new PercentageDiscount(0, new Product(idProduct, "", "", 0, "", null), new Date(startDate.getTimeInMillis()), new Date(endDate.getTimeInMillis()), null, Float.parseFloat(percentage.getText().toString()), fid);
                    AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDiscountDAO().create(discount, UserConnection.getInstance().getToken(), new ResponseHandler() {
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
                }else{
                    if(percentage.getText().toString().isEmpty()){
                        percentage.setError( getResources().getString(R.string.boughtProduct) + getResources().getString(R.string.leftBlank));
                    }
                }
            }
            else{
                if(!bought.getText().toString().isEmpty() && !free.getText().toString().isEmpty()) {
                    discount = new QuantityDiscount(0, new Product(idProduct, "", "", 0, "", null), new Date(startDate.getTimeInMillis()), new Date(endDate.getTimeInMillis()), null, Integer.parseInt(bought.getText().toString()), Integer.parseInt(free.getText().toString()), fid);
                    AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDiscountDAO().create(discount, UserConnection.getInstance().getToken(), new ResponseHandler() {
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
                }else{
                    if(bought.getText().toString().isEmpty()){
                        bought.setError( getResources().getString(R.string.boughtProduct) + getResources().getString(R.string.leftBlank));
                    }
                    if(free.getText().toString().isEmpty()){
                        free.setError( getResources().getString(R.string.freeProduct) + getResources().getString(R.string.leftBlank));
                    }
                }
            }

        }
    }

    @Override
    public void onBackPressed(){
        //Notify the user that he will lose all modifications
        new AlertDialog.Builder(this)
                .setTitle(R.string.newDiscount)
                .setMessage(R.string.addLeaveMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
