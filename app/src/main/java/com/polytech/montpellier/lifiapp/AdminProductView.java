package com.polytech.montpellier.lifiapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminProductView extends Fragment{

    ProductDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lampall_display, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDataAndView();
    }

    /*
    @Override
    public void openNewLampPopUp(final int lamp) {
        new AlertDialog.Builder(this)
                .setTitle("New Lamp")
                .setMessage("You are standing under a new lamp, do you want to add it ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(context, AddLamp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("lamp",lamp);
                        context.startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
    */
    public void updateDataAndView(){
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddProduct.class);
                startActivity(intent);
            }
        });
        final TableLayout tl = (TableLayout) getView().findViewById(R.id.main_table);
        tl.removeAllViews();
        final Button add = (Button) getView().findViewById(R.id.button_add);

        dao.getAll(new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if(object instanceof ArrayList) {
                    tl.removeAllViews();
                    ArrayList<Product> array = (ArrayList<Product>)object;
                    for(int i = 0 ; i < array.size() ; i++) {
                        final Product product = array.get(i);
                        final TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setId(product.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);

                        final TextView label_product = new TextView(getActivity());
                        label_product.setText(product.getName());
                        label_product.setTextColor(Color.BLACK);
                        label_product.setPadding(5, 5, 5, 5);
                        label_product.setWidth(tl.getWidth() / 5);
                        row.addView(label_product);



                        final TextView label_marque = new TextView(getActivity());
                        String text = product.getBrand();

                        label_marque.setText(text);
                        label_marque.setTextColor(Color.BLACK); // set the color
                        label_marque.setPadding(5, 5, 5, 5); // set the padding (if required)
                        label_marque.setWidth(tl.getWidth() / 5);
                        row.addView(label_marque); // add the column to the table row here

                        final TextView label_price = new TextView(getActivity());
                        label_price.setText(String.valueOf(product.getPrice())+ "€");
                        label_price.setTextColor(Color.BLACK);
                        label_price.setPadding(5, 5, 5, 5);
                        label_price.setWidth(tl.getWidth() / 5);
                        row.addView(label_price);

                        Button delete = new Button(getActivity());
                        delete.setText("delete");
                        delete.setWidth(tl.getWidth() / 5);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Delete Product")
                                        .setMessage("Are you sure you want to delete this product?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getProductDAO().delete(row.getId(), UserConnection.getInstance().getToken(), new ResponseHandler() {
                                                    @Override
                                                    public void onSuccess(Object object) {
                                                        if(object instanceof JSONObject){
                                                            System.out.println("JSON : " + object.toString());
                                                            JSONObject res = (JSONObject) object;
                                                            try {
                                                                if(res.getInt("affectedRows") != 0){
                                                                    tl.removeView(tl.findViewById(row.getId()));
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Object object) {

                                                    }
                                                });
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        });
                        row.addView(delete);

                        Button update = new Button(getActivity());
                        update.setText("update");
                        update.setWidth(tl.getWidth() / 5);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), UpdateProduct.class);
                                intent.putExtra("name",label_product.getText());
                                intent.putExtra("product",row.getId());
                                intent.putExtra("brand", label_marque.getText());
                                intent.putExtra("price", String.valueOf(product.getPrice()));
                                startActivity(intent);
                            }
                        });
                        row.addView(update);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println(v.getId());
                            }
                        });
                        tl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

}
