package com.polytech.montpellier.lifiapp.Admin.Discount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DiscountSummary extends Activity {

    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_summary);
        Helper.hasActiveInternetConnection(this);
        Intent intent = getIntent();

        DiscountDAO discountDAO = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getDiscountDAO();

        final int idDiscount = intent.getIntExtra("idDiscount",0);
        final int idProduct = intent.getIntExtra("idProduct",0);
        final int idDepartement = intent.getIntExtra("idDepartement",0);


        discountDAO.getById(idDiscount, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                final TextView brandTV = (TextView) findViewById(R.id.productBrand);
                final TextView prodNameTV = (TextView) findViewById(R.id.productName);
                final TextView dateFinTV = (TextView) findViewById(R.id.date_fin);
                final ScrollView desciptionSV = (ScrollView) findViewById(R.id.descriptionScroll);
                final TextView descriptionTV = (TextView) findViewById(R.id.descriptionView) ;
                final TextView discountTV = (TextView) findViewById(R.id.discount) ;



                final TextView new_Price = (TextView) findViewById(R.id.newPrice);
                final TextView old_Price = (TextView) findViewById(R.id.oldPrice);
                ArrayList<Discount> discountArrayList = ( ArrayList<Discount>) object;
                Discount discount = discountArrayList.get(0);



                if (discount instanceof PercentageDiscount){

                    PercentageDiscount discountper = new PercentageDiscount(discount.getId(),discount.getProduct() , discount.getDateDebut(), discount.getDateFin(), discount.getDateCreation(),
                            ((PercentageDiscount) discount).getPercentage(), discount.getFidelity());

                    String brand = discountper.getProduct().getBrand();
                    String prodName = discountper.getProduct().getName();
                    String dateFin = new SimpleDateFormat( "yyyy-MM-dd").format(discountper.getDateFin());
                    String desc = discountper.getProduct().getDescription();
                    float percentage = discountper.getPercentage();


                    brandTV.setText(brand);
                    prodNameTV.setText(prodName);
                    dateFinTV.setText(dateFin);
                    descriptionTV.setText(desc);
                    discountTV.setText(Float.toString(percentage) +" "+ getResources().getString(R.string.percentOff));

                    Float newString = discountper.newPrice();
                    Float oldString = discountper.oldPrice();

                    String roundOffnew = Double.toString(Math.round(newString * 100.0) / 100.0);
                    double roundOffold = Math.round(oldString * 100.0) / 100.0;



                    new_Price.setText(roundOffnew);
                    new_Price.append(getResources().getString(R.string.currency));
                    old_Price.setText(Double.toString( roundOffold) + getResources().getString(R.string.currency));


                }else if (discount instanceof QuantityDiscount){

                    QuantityDiscount discountqte = new QuantityDiscount(discount.getId(),discount.getProduct() , discount.getDateDebut(), discount.getDateFin(), discount.getDateCreation(),
                            ((QuantityDiscount) discount).getBought(), ((QuantityDiscount) discount).getFree(), discount.getFidelity());

                    String brand = discountqte.getProduct().getBrand();
                    String prodName = discountqte.getProduct().getName();
                    String promo = discountqte.getBought()+" " +getResources().getString(R.string.bought)+ " " + discountqte.getFree()+" " + getResources().getString(R.string.free);

                    brandTV.setText(brand);
                    prodNameTV.setText(prodName);
                    String dateFin = new SimpleDateFormat( "yyyy-MM-dd").format(discountqte.getDateFin());
                    String desc = discountqte.getProduct().getDescription();

                    Float newString = discountqte.newPrice();
                    Float oldString = discountqte.oldPrice();

                    double roundOffnew = Math.round(newString * 100.0) / 100.0;
                    double roundOffold = Math.round(oldString * 100.0) / 100.0;



                    new_Price.setText(Double.toString( roundOffnew) + getResources().getString(R.string.currency));
                    old_Price.setText(Double.toString( roundOffold) + getResources().getString(R.string.currency));

                    dateFinTV.setText(dateFin);
                    discountTV.setText(promo);
                    descriptionTV.setText(desc);
 
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

    }
}
