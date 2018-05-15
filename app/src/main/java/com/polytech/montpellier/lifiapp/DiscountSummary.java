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

import com.google.gson.JsonObject;
import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;

import org.json.JSONArray;

import java.util.ArrayList;

public class DiscountSummary extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_summary);
        Intent intent = getIntent();

        DiscountDAO discountDAO = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO();

        final int idDiscount = intent.getIntExtra("idDiscount",0);
        final int idProduct = intent.getIntExtra("idProduct",0);
        final int idDepartement = intent.getIntExtra("idDepartement",0);

        System.out.println("idDissscoutn" + idDiscount);
        final TextView brand = (TextView) findViewById(R.id.productBrand);
        final TextView new_Price = (TextView) findViewById(R.id.newPrice);
        final TextView old_Price = (TextView) findViewById(R.id.oldPrice);


        System.out.println("textView alalal"   );

        brand.setText("coucou");
        //new_Price.setText("new wow ");
        //old_Price.setText("old bouh");

        discountDAO.getById(idDiscount, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                final TextView brand = (TextView) findViewById(R.id.productBrand);
                System.out.println("textView alalal" + object );
                brand.setText("coucou");
                ArrayList<Discount> discountArrayList = ( ArrayList<Discount>) object;
                Discount discount = discountArrayList.get(0);

                System.out.println("instant of " + (object instanceof PercentageDiscount));

                if (discount instanceof PercentageDiscount){

                    PercentageDiscount discountper = new PercentageDiscount(discount.getProduct() , discount.getDateDebut(), discount.getDateFin(), discount.getDateCreation(),
                            ((PercentageDiscount) discount).getPercentage(), discount.getFidelity());
                    //brand.setText(discount.get);
                    String newString = Float.toString(discount.newPrice());
                    String oldString = Float.toString(discount.oldPrice());
                    System.out.println("disc new price"+newString);

                    new_Price.setText(newString);
                    old_Price.setText(oldString);

                }else if (discount instanceof QuantityDiscount){

                    QuantityDiscount discountqte = new QuantityDiscount(discount.getProduct() , discount.getDateDebut(), discount.getDateFin(), discount.getDateCreation(),
                            ((QuantityDiscount) discount).getBought(), ((QuantityDiscount) discount).getFree(), discount.getFidelity());

                    //brand.setText(discount.get);
                    String newString = Float.toString(discount.newPrice());
                    String oldString = Float.toString(discount.oldPrice());
                    System.out.println("disc newé price"+newString);

                    new_Price.setText(newString);
                    old_Price.setText(oldString);
                }
            }

            @Override
            public void onError(Object object) {
                System.out.println("onError discount summary ");
            }
        });

    }
}
