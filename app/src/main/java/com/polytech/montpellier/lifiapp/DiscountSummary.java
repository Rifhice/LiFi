package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;

public class DiscountSummary extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         DiscountDAO discountDAO = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO();



        Intent intent = getIntent();
        final int idDiscount = intent.getIntExtra("idDiscount",0);
        final int idProduct = intent.getIntExtra("idProduct",0);
        final int idDepartement = intent.getIntExtra("idDepartement",0);

        //
        // final EditText text = (EditText) findViewById(R.id.nametf);


        discountDAO.getById(idDiscount, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                final TextView brand = (TextView) findViewById(R.id.productBrand);
                System.out.println("textView alalal" + object );

                //brand.setText();
            }

            @Override
            public void onError(Object object) {

            }
        });

    }
}
