package com.huaqin.mybill.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.huaqin.mybill.R;

import java.util.ArrayList;
import java.util.List;


public class MeFragment extends Fragment {
    private String TAG = "SecFragment";
    ViewPager mSecond_viewPager;
    TabLayout msecond_tablayout;
    private List<String> mTitleList = new ArrayList<>(2);
    private List<Fragment> fragments = new ArrayList<>(2);
    private View v;
    private static MeFragment meFragment;

    public static MeFragment newInstance() {
        if (meFragment == null) {
            meFragment = new MeFragment();
        }
        return meFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_settings, container, false);
        Log.i(TAG, "onCreateView执行");
        return v;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
