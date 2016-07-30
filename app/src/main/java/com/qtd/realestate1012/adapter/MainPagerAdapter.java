package com.qtd.realestate1012.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Dell on 7/30/2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {


    private Context context;
    private ArrayList<Fragment> fragments;

    public MainPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> arrayList) {
        super(fm);
        this.context = context;
        fragments = arrayList;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
