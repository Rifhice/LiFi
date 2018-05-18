package com.polytech.montpellier.lifiapp;

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
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Lamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminLampView extends Fragment{

    LampDAO dao = AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO();

    public AdminLampView(){}

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

    public void updateDataAndView(){
        Helper.hasActiveInternetConnection(getActivity());
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
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
        final TableLayout tl =  getView().findViewById(R.id.main_table);
        tl.removeAllViews();
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
                    label_header_brand.setText(R.string.lamp);
                    label_header_brand.setTextColor(Color.BLUE);
                    label_header_brand.setPadding(5, 5, 5, 5);
                    label_header_brand.setWidth(tl.getWidth() / 4);
                    headerRow.addView(label_header_brand);

                    TextView label_header_promotion = new TextView(getActivity());
                    label_header_promotion.setId(200);
                    label_header_promotion.setText(R.string.departement);
                    label_header_promotion.setTextColor(Color.BLUE);
                    label_header_promotion.setPadding(5, 5, 5, 5);
                    label_header_promotion.setWidth(tl.getWidth() / 4);
                    headerRow.addView(label_header_promotion);

                    tl.addView(headerRow);// add the column to the table row here

                    ArrayList<Lamp> array = (ArrayList<Lamp>)object;
                    for(int i = 0 ; i < array.size() ; i++) {
                        Lamp lamp = array.get(i);
                        final TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setId(lamp.getId());
                        row.setGravity(Gravity.CENTER_HORIZONTAL);

                        final TextView label_lamp = new TextView(getActivity());
                        label_lamp.setText(lamp.getName());
                        label_lamp.setTextColor(Color.BLACK);
                        label_lamp.setPadding(5, 5, 5, 5);
                        label_lamp.setWidth(tl.getWidth() / 4);
                        row.addView(label_lamp);

                        final TextView label_department = new TextView(getActivity());
                        String text;
                        if (lamp.getDepartment() != null) {
                            text = lamp.getDepartment().getName();
                        } else {
                            text = "N/A";
                        }
                        label_department.setText(text);
                        label_department.setTextColor(Color.BLACK); // set the color
                        label_department.setPadding(5, 5, 5, 5); // set the padding (if required)
                        label_department.setWidth(tl.getWidth() / 4);
                        row.addView(label_department); // add the column to the table row here

                        Button delete = new Button(getActivity());
                        delete.setText(getResources().getString(R.string.delete));
                        delete.setWidth(tl.getWidth() / 4);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(getResources().getString(R.string.deleteLamp))
                                        .setMessage(getResources().getString(R.string.deleteLampMessage))
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO().delete(row.getId(), UserConnection.getInstance().getToken(), new ResponseHandler() {
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
                        update.setText(getResources().getString(R.string.update));
                        update.setWidth(tl.getWidth() / 4);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), UpdateLamp.class);
                                intent.putExtra("name",label_lamp.getText());
                                intent.putExtra("lamp",row.getId());
                                intent.putExtra("name_department", label_department.getText());
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
