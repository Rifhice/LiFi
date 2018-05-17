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
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminDiscountView extends Fragment {

    DiscountDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO();

    @Override
    public void onResume() {
        super.onResume();
        updateDataAndView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lampall_display, container, false);
    }


    public void updateDataAndView() {
        Helper.hasActiveInternetConnection(getActivity());
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddDiscount.class);
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
                            UserConnection.getInstance().changePassword(new1.getText().toString(), new ResponseHandler() {
                                @Override
                                public void onSuccess(Object object) {
                                    System.out.println(object);
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
        final TableLayout tl = (TableLayout) getView().findViewById(R.id.main_table);
        tl.removeAllViews();
        dao.getAll(new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof ArrayList) {
                    tl.removeAllViews();
                    ArrayList<Discount> array = (ArrayList<Discount>) object;
                    for (int i = 0; i < array.size(); i++) {
                        final Discount discount = array.get(i);
                        final TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setId(discount.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), DiscountSummary.class);
                                intent.putExtra("idDiscount", discount.getId());
                                startActivity(intent);
                            }
                        });
                        final TextView label_produit = new TextView(getActivity());
                        label_produit.setText(discount.getProduct().getName() + "  " + discount.getProduct().getBrand());
                        label_produit.setTextColor(Color.BLACK);
                        label_produit.setPadding(5, 5, 5, 5);
                        label_produit.setWidth(tl.getWidth() / 4);
                        row.addView(label_produit);

                        final TextView discountValue = new TextView(getActivity());
                        discountValue.setTextColor(Color.BLACK); // set the color
                        discountValue.setPadding(5, 5, 5, 5); // set the padding (if required)
                        discountValue.setWidth(tl.getWidth() / 4);

                        if (discount instanceof QuantityDiscount) {
                            discountValue.setText(((QuantityDiscount) discount).getBought() + getResources().getString(R.string.bought)+ ((QuantityDiscount) discount).getFree() + getResources().getString(R.string.free));
                        } else if (discount instanceof PercentageDiscount) {
                            discountValue.setText(((PercentageDiscount) discount).getPercentage() + getResources().getString(R.string.percentOff));
                        }
                        row.addView(discountValue); // add the column to the table row here
                        Button delete = new Button(getActivity());
                        delete.setText(getResources().getString(R.string.delete));
                        delete.setWidth(tl.getWidth() / 4);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(getResources().getString(R.string.deleteDiscount))
                                        .setMessage(getResources().getString(R.string.deleteDiscountMessage))
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                System.out.println("deletehere " + row.getId() + "  token " + UserConnection.getInstance().getToken());
                                                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getDiscountDAO().delete(row.getId(), UserConnection.getInstance().getToken(), new ResponseHandler() {
                                                    @Override
                                                    public void onSuccess(Object object) {
                                                        if (object instanceof JSONObject) {
                                                            System.out.println("JSON : " + object.toString());
                                                            JSONObject res = (JSONObject) object;
                                                            try {
                                                                if (res.getInt("affectedRows") != 0) {
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
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        });
                        row.addView(delete);

                        Button update = new Button(getActivity());
                        update.setText(getResources().getString(R.string.update));
                        update.setWidth(tl.getWidth() / 4);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), UpdateDiscount.class);
                                intent.putExtra("discountId",row.getId());
                                startActivity(intent);
                            }
                        });
                        row.addView(update);
;
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
