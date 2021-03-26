package com.huaqin.mybill.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.huaqin.mybill.R;
import com.huaqin.mybill.fragment.BillFragment;
import com.huaqin.mybill.fragment.MeFragment;
import com.huaqin.mybill.model.IOItemAdapter;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    public static List<Fragment> mFragmntList = new ArrayList<>();

    private RecyclerView    ioItemRecyclerView;
    private IOItemAdapter ioAdapter;
    private Button          showBtn;
    private CircleButton addBtn;
    private ImageView       headerImg;
    private TextView        monthlyCost, monthlyEarn;
    public static String PACKAGE_NAME;
    public static Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        resources = getResources();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_header = headerView.findViewById(R.id.nav_header);
        nav_header.setOnClickListener(this);
//        fab = findViewById(R.id.fab_main);
//        fab.setOnClickListener(this);
//        View view1 = getLayoutInflater().inflate(R.layout.item_view_pager_1, null);
//        View view2 = getLayoutInflater().inflate(R.layout.item_view_pager_2, null);
//        View view3 = getLayoutInflater().inflate(R.layout.item_view_pager_3, null);
//        List<View> viewList = new ArrayList<>();
//        viewList.add(view1);
//        viewList.add(view2);
//        viewList.add(view3);
        viewPager = findViewById(R.id.view_pager_main);


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new BillFragment());
        fragments.add(new MeFragment());
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, null);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(mFragmentAdapter);
//        mTabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(pageChangeListener);
        //账单界面
//        showBtn = (Button) view1.findViewById(R.id.show_money_button);
//        ioItemRecyclerView = (RecyclerView) view1.findViewById(R.id.in_and_out_items);
//        headerImg = (ImageView) view1.findViewById(R.id.header_img);
//        monthlyCost = (TextView) view1.findViewById(R.id.monthly_cost_money);
//        monthlyEarn = (TextView) view1.findViewById(R.id.monthly_earn_money);



        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(this);
    }
    private void addFragmet() {
        mFragmntList.clear();
        mFragmntList.add(BillFragment.newInstance());
        mFragmntList.add(MeFragment.newInstance());
    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ArgbEvaluator evaluator = new ArgbEvaluator();
            int evaluate = getResources().getColor(R.color.app_blue);
            if (position == 0) {
                evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.app_blue), getResources().getColor(R.color.app_green));
            } else if (position == 1) {
                evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.app_green), getResources().getColor(R.color.app_yellow));
            } else if (position == 2) {
                evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.app_yellow), getResources().getColor(R.color.app_red));
            } else {
                evaluate = getResources().getColor(R.color.app_red);
            }
            ((View) viewPager.getParent()).setBackgroundColor(evaluate);
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.bottom_navigation_blue);
                    fab.show();
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.bottom_navigation_red);
                    fab.hide();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_main_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
//            case R.id.action_menu_main_donate:
//                Intent donateIntent = new Intent(this, DonateActivity.class);
//                startActivity(donateIntent);
//                break;
//            case R.id.action_menu_main_my_app:
//                Intent myAppsIntent = new Intent(this, MyAppsActivity.class);
//                startActivity(myAppsIntent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {

            case R.id.nav_settings:
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
//            case R.id.nav_donate:
//                intent.setClass(this, DonateActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.nav_my_apps:
//                intent.setClass(this, MyAppsActivity.class);
//                startActivity(intent);
//                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
                break;
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_blue:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.bottom_navigation_red:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };


    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTitles;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

    }
}
