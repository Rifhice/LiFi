package com.polytech.montpellier.lifiapp;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDiscount extends AppCompatActivity {

    final Context context = this;
    private Button startButton, endButton;
    private Calendar startDate, endDate;
    private int currentId;
    private int idDepartment, idProduct;

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
        Helper.hasActiveInternetConnection(this);
        Intent intent = getIntent();
        final int baseIdProd = intent.getIntExtra("idProduct", -1);
        final int baseIdDep = intent.getIntExtra("idDepartment", -1);
        System.out.println("BASEIDRECEIVED" + baseIdDep + " " + baseIdProd);
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

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        startButton.setText(dateToString(startDate));
        endButton.setText(dateToString(endDate));

        final Spinner spinnerDepartment = (Spinner) findViewById(R.id.departmentSpinner);
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
                    if(baseIdDep != -1) {
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

            }
        });

        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String depName = (String) spinnerDepartment.getSelectedItem();
                idDepartment = depMap.get(depName);
                idProduct = -1;
                Department current = new Department(idDepartment, depName);
                final Spinner spinnerProduct = (Spinner) findViewById(R.id.productSpinner);
                final ArrayList<String> prod = new ArrayList<>();
                final ArrayAdapter<String> prodAdapter = new ArrayAdapter<String>( context, R.layout.list,R.id.list1, prod);
                final HashMap<String, Integer> prodMap = new HashMap<String, Integer>();

                //TODO vérifier departement non vide
                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDepartmentDAO().getAllProducts(current, new ResponseHandler() {

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
        updateType(isPercentage);
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

    public void validate(View view){
        int fid = 0;
        if(fidelity.isChecked()){
            fid = 1;
        }
        Discount discount;
        if(isPercentage){
            discount = new PercentageDiscount(0,new Product(idProduct,"","",0,"",null),new Date(startDate.getTimeInMillis()),new Date(endDate.getTimeInMillis()),null,Float.parseFloat(percentage.getText().toString()),fid);
        }
        else{
            discount = new QuantityDiscount(0,new Product(idProduct,"","",0,"",null),new Date(startDate.getTimeInMillis()),new Date(endDate.getTimeInMillis()),null,Integer.parseInt(bought.getText().toString()), Integer.parseInt(free.getText().toString()),fid);
        }
        AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO().create(discount, UserConnection.getInstance().getToken(), new ResponseHandler() {
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

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("New Discount")
                .setMessage("You are leaving, are you sure ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
