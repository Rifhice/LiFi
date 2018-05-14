package com.polytech.montpellier.lifiapp;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.oledcomm.soft.lifiapp.R;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminActivity extends ActivityGroup {
    TabHost tabHost;
    Intent lamp;
    Intent product;
    Intent discount;
    Intent department;

    static AdminActivity instance = null;

    public static AdminActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_display);
        instance = this;
        lamp = new Intent(this, AdminLampView.class);
        product = new Intent(this, AdminProductView.class);
        discount = new Intent(this, AdminDiscountView.class);
        department = new Intent(this, AdminDepartmentView.class);


        tabHost = (TabHost)findViewById(R.id.menu);
        tabHost.setup(this.getLocalActivityManager());

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Lamp");
        spec.setContent(lamp);
        spec.setIndicator("Lamp");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Product");
        spec.setContent(product);
        spec.setIndicator("Product");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Discount");
        spec.setContent(discount);
        spec.setIndicator("Discount");
        tabHost.addTab(spec);

        //Tab 4
        spec = tabHost.newTabSpec("Department");
        spec.setContent(department);
        spec.setIndicator("Department");
        tabHost.addTab(spec);
    }

    public void openNewLampPopUp(int lamp){
        ((AdminTab)getCurrentActivity()).openNewLampPopUp(lamp);
    }

}
