package com.qtd.realestate1012.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qtd.realestate1012.fragment.ImageFragment;

import java.util.ArrayList;

/**
 * Created by DELL on 9/27/2016.
 */
public class ImageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> arrayList;

    public ImageAdapter(FragmentManager fm, ArrayList<String> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(arrayList.get(position));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
