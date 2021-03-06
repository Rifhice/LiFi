package com.polytech.montpellier.lifiapp.Admin.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Product;
import com.polytech.montpellier.lifiapp.UserConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdminProductView extends Fragment{

    //Declaration DAO

    ProductDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getProductDAO();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Place the view
        return inflater.inflate(R.layout.lampall_display, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDataAndView();
    }




    public void updateDataAndView(){
        //Check for the internet
        Helper.hasActiveInternetConnection(getActivity());

        //Set buttons
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddProduct.class);
                startActivity(intent);
            }
        });
        FloatingActionButton changepass = getView().findViewById(R.id.changepassword);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Password");

                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                // Add a TextView here for the "Title" label, as noted in the comments
                final EditText new1 = new EditText(getActivity());
                new1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new1.setHint("New password");
                layout.addView(new1); // Notice this is an add method

                // Add another TextView here for the "Description" label
                final EditText new2 = new EditText(getActivity());
                new2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new2.setHint("New password");
                layout.addView(new2); // Another add method

                builder.setView(layout); // Again this is a set method, not add
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!new1.getText().toString().equals(new2.getText().toString())){
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setMessage("Password not equals");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            //Change the password
                            UserConnection.getInstance().changePassword(new1.getText().toString(), new ResponseHandler() {
                                @Override
                                public void onSuccess(Object object) {

                                }

                                @Override
                                public void onError(Object object) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    alertDialog.setMessage("Error");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        final TableLayout tl = getView().findViewById(R.id.main_table);
        tl.removeAllViews();

        //Retrieve all the products
        dao.getAll(new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if(object instanceof ArrayList) {
                    tl.removeAllViews();

                    // Adding table header
                    final TableRow headerRow = new TableRow(getActivity());

                    TextView label_header_brand = new TextView(getActivity());
                    label_header_brand.setId(200);
                    Resources res = getResources();
                    String brand = res.getString(R.string.product) + res.getString(R.string.andSign) + res.getString(R.string.marque);
                    label_header_brand.setText(brand);
                    label_header_brand.setTextColor(Color.BLUE);
                    label_header_brand.setPadding(5, 5, 5, 5);
                    label_header_brand.setWidth(tl.getWidth() / 7);
                    headerRow.addView(label_header_brand);

                    TextView label_header_promotion = new TextView(getActivity());
                    label_header_promotion.setId(200);
                    label_header_promotion.setText(R.string.price);
                    label_header_promotion.setTextColor(Color.BLUE);
                    label_header_promotion.setPadding(5, 5, 5, 5);
                    label_header_promotion.setWidth(tl.getWidth() / 7);
                    headerRow.addView(label_header_promotion);

                    TextView label_header_department = new TextView(getActivity());
                    label_header_department.setId(200);
                    label_header_department.setText(R.string.departement);
                    label_header_department.setTextColor(Color.BLUE);
                    label_header_department.setPadding(5, 5, 5, 5);
                    label_header_department.setWidth(tl.getWidth() / 6);
                    headerRow.addView(label_header_department);

                    tl.addView(headerRow);// add the column to the table row here

                    //Retrieve Discount of the products
                    ArrayList<Product> array = (ArrayList<Product>)object;
                    for(int i = 0 ; i < array.size() ; i++) {
                        final Product product = array.get(i);
                        dao.getProductDiscounts(product, new ResponseHandler() {
                            @Override
                            public void onSuccess(Object object) {

                            }

                            @Override
                            public void onError(Object object) {
                            }
                        });

                        final TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setId(product.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);

                        String text = product.getName() +"\n" + product.getBrand();
                        final TextView label_product = new TextView(getActivity());
                        label_product.setText(text);
                        label_product.setTextColor(Color.BLACK);
                        label_product.setPadding(5, 5, 5, 5);
                        label_product.setWidth(tl.getWidth() / 5);
                        row.addView(label_product);

                        final TextView label_price = new TextView(getActivity());
                        String price = String.valueOf(product.getPrice())+getResources().getString(R.string.currency);
                        label_price.setText(price);
                        label_price.setTextColor(Color.BLACK);
                        label_price.setPadding(5, 5, 5, 5);
                        label_price.setWidth(tl.getWidth() / 8);
                        row.addView(label_price);

                        final TextView label_dep = new TextView(getActivity());
                        label_dep.setText(product.getDepartment().getName());
                        label_dep.setTextColor(Color.BLACK);
                        label_dep.setPadding(5, 5, 5, 5);
                        label_dep.setWidth(tl.getWidth() / 6);
                        row.addView(label_dep);

                        //Delete a product
                        Button delete = new Button(getActivity());
                        delete.setText(getResources().getString(R.string.delete));
                        delete.setWidth(tl.getWidth() / 6);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(getResources().getString(R.string.deleteProduct))
                                        .setMessage(getResources().getString(R.string.deleteMessage))
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                AbstractDAOFactory.getFactory(AbstractDAOFactory.DISTANT_DAO_FACTORY).getProductDAO().delete(row.getId(), UserConnection.getInstance().getToken(), new ResponseHandler() {
                                                    @Override
                                                    public void onSuccess(Object object) {
                                                        if(object instanceof JSONObject){
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
                        update.setText(getResources().getString(R.string.update));
                        update.setWidth(tl.getWidth() / 6);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Prepare variables to give another file
                                Intent intent = new Intent(getActivity(), UpdateProduct.class);
                                intent.putExtra("name",product.getName());
                                intent.putExtra("product",row.getId());
                                intent.putExtra("brand", product.getBrand());
                                intent.putExtra("price", String.valueOf(product.getPrice()));
                                startActivity(intent);
                            }
                        });
                        row.addView(update);

                        //Button to display information about a product
                        Button info = new Button(getActivity());
                        info.setText(getResources().getString(R.string.info));
                        info.setWidth(tl.getWidth() /8);
                        info.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                Intent intent = new Intent(getActivity(), ProductSummary.class);
                                intent.putExtra("name",product.getName());
                                intent.putExtra("product",row.getId());
                                intent.putExtra("brand", product.getBrand());
                                intent.putExtra("price", String.valueOf(product.getPrice()));
                                startActivity(intent);
                            }
                        });
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ProductSummary.class);
                                intent.putExtra("name",product.getName());
                                intent.putExtra("product",row.getId());
                                intent.putExtra("brand", product.getBrand());
                                intent.putExtra("price", String.valueOf(product.getPrice()));
                                startActivity(intent);
                            }
                        });
                        row.addView(info);


                        tl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
            }

            @Override
            public void onError(Object object) {
                new AlertDialog.Builder(getActivity())
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
