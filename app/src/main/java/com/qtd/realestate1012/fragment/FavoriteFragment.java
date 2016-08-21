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
public class FavoriteFragment extends Fragment {

    private View view;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

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
        BoardFragment boardFragment = new BoardFragment();
        HomesFavoriteFragment homesFavoriteFragment = new HomesFavoriteFragment();
        SearchesFavoriteFragment searchFragment = new SearchesFavoriteFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(boardFragment);
        fragments.add(homesFavoriteFragment);
        fragments.add(searchFragment);

        FavoritePagerAdapter adapter = new FavoritePagerAdapter(view.getContext(), getChildFragmentManager(), fragments);
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

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
