package com.huaqin.mybill.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Day on 2017/1/4.
 */

public class OutterFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    public OutterFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);

        this.mFragmentList = fragmentList;

    }

    @Override
    public Fragment getItem(int position) {
        Log.i("dengpos", "position = " + position);

        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}