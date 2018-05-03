package com.polytech.montpellier.lifiapp;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.oledcomm.soft.lifiapp.R;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminActivity extends ActivityGroup {
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_display);

        tabHost = (TabHost)findViewById(R.id.menu);
        tabHost.setup(this.getLocalActivityManager());
        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Lamp");
        spec.setContent(new Intent(this, AdminLampView.class));
        spec.setIndicator("Lamp");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Product");
        spec.setContent(new Intent(this, AdminProductView.class));
        spec.setIndicator("Product");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Discount");
        spec.setContent(R.id.discount);
        spec.setIndicator("Discount");
        tabHost.addTab(spec);

        //Tab 4
        spec = tabHost.newTabSpec("Department");
        spec.setContent(R.id.department);
        spec.setIndicator("Department");
        tabHost.addTab(spec);
    }

}
