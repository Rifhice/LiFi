package com.polytech.montpellier.lifiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.oledcomm.soft.lifiapp.R;
import com.polytech.montpellier.lifiapp.DAO.DAOFactory.AbstractDAOFactory;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;
import com.polytech.montpellier.lifiapp.Model.Lamp;

public class AddLamp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp_add);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("lamp",0);
        final EditText text = (EditText) findViewById(R.id.nametf);
        Button validate = (Button)findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lamp lamp = new Lamp(id,text.getText().toString(),new Department(2));
                AbstractDAOFactory.getFactory(AbstractDAOFactory.MYSQL_DAO_FACTORY).getLampDAO().create(lamp,"5f4dcc3b5aa765d61d8327deb882cf99", new ResponseHandler() {
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
        });
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("New Lamp")
                .setMessage("You are leaving, are you sure ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}
