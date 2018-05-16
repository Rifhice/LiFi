package com.polytech.montpellier.lifiapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;
import com.polytech.montpellier.lifiapp.Model.Discounts.PercentageDiscount;
import com.polytech.montpellier.lifiapp.Model.Discounts.QuantityDiscount;
import com.polytech.montpellier.lifiapp.Model.Lamp;

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

    /*
@Override
public void openNewLampPopUp(final int lamp) {
    new AlertDialog.Builder(context)
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
    public void updateDataAndView() {
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
                        System.out.println("discountprint" + discount);
                        System.out.println("discountnaem" + discount.getDateDebut());
                        final TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        System.out.println("row id " + discount.getId());
                        row.setId(discount.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), DiscountSummary.class);
                                intent.putExtra("idDiscount", discount.getId());
                                // intent.putExtra("idProduct",idProduct);
                                //intent.putExtra("idDepartement", pkDep);
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
                            discountValue.setText(((QuantityDiscount) discount).getBought() + "  acheté   " + ((QuantityDiscount) discount).getFree() + " gratuit");
                        } else if (discount instanceof PercentageDiscount) {
                            discountValue.setText(((PercentageDiscount) discount).getPercentage() + " % off");
                        }
                        row.addView(discountValue); // add the column to the table row here
                        Button delete = new Button(getActivity());
                        delete.setText("delete");
                        delete.setWidth(tl.getWidth() / 4);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Delete Lamp")
                                        .setMessage("Are you sure you want to delete this discount?")
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
                        update.setText("update");
                        update.setWidth(tl.getWidth() / 4);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), UpdateDiscount.class);
                                intent.putExtra("discountId",row.getId());
                               // intent.putExtra("lamp",row.getId());
                                //intent.putExtra("name_department", label_department.getText());
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
