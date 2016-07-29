package com.qtd.realestate1012.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.qtd.realestate1012.R;

import java.util.ArrayList;

/**
 * Created by Dell on 7/30/2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private static final int ICON_TAB[] = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_heart,
            R.drawable.ic_bell,
            R.drawable.ic_dot_vertical
    };

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
        Drawable drawable = ContextCompat.getDrawable(context, ICON_TAB[position]);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
