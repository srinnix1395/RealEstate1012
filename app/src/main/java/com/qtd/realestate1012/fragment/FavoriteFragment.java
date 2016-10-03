package com.qtd.realestate1012.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.FavoritePagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 7/30/2016.
 */
public class FavoriteFragment extends Fragment{

    private View view;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private FavoritePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new BoardFragment());
        fragments.add(new HomesFavoriteFragment());
        fragments.add(new SearchesFavoriteFragment());

        adapter = new FavoritePagerAdapter(view.getContext(), getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void clearUserData() {
        ((BoardFragment) adapter.getItem(0)).clearUserData();
        ((HomesFavoriteFragment) adapter.getItem(1)).clearUserData();
        ((SearchesFavoriteFragment) adapter.getItem(2)).clearUserData();
    }

    public void updateDataLikeHouse() {
        ((BoardFragment) adapter.getItem(0)).getDataFromDatabase();
        ((HomesFavoriteFragment) adapter.getItem(1)).getDataFromDatabase();
    }
}
