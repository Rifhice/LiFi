package com.polytech.montpellier.lifiapp;

import android.app.ActivityGroup;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TabHost;

import com.oledcomm.soft.lifiapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminActivity extends AppCompatActivity {
    TabHost tabHost;
    Intent lamp;
    Intent product;
    Intent discount;
    Intent department;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    static AdminActivity instance = null;

    public static AdminActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_display);
        instance = this;
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.mytabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void openNewLampPopUp(final int lamp){
        new AlertDialog.Builder(this)
                .setTitle("New Lamp")
                .setMessage("You are standing under a new lamp, do you want to add it ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(AdminActivity.this, AddLamp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("lamp",lamp);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AdminLampView(), "Lamp");
        adapter.addFragment(new AdminProductView(), "Product");
        adapter.addFragment(new AdminDiscountView(), "Discount");
        adapter.addFragment(new AdminDepartmentView(), "Department");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
