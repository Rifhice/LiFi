package com.polytech.montpellier.lifiapp;

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

import com.oledcomm.soft.lifiapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 03/05/2018.
 */

public class AdminActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    static AdminActivity instance = null;
    static boolean isDisplayed = true;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdminActivity.setIsDisplayed(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdminActivity.setIsDisplayed(true);
    }

    public void openNewLampPopUp(final int lamp){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.newLamp))
                .setMessage(getResources().getString(R.string.newLampDetectedMessage))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(AdminActivity.this, AddLamp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("lamp",lamp);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public static void setIsDisplayed(boolean val){
        isDisplayed = val;
    }

    public static boolean isIsDisplayed(){
        return isDisplayed;
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AdminLampView(), getResources().getString(R.string.lamp));
        adapter.addFragment(new AdminDepartmentView(), getResources().getString(R.string.departement));
        adapter.addFragment(new AdminProductView(), getResources().getString(R.string.product));
        adapter.addFragment(new AdminDiscountView(), getResources().getString(R.string.discount));
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
