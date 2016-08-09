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

    private FavoritePagerAdapter adapter;
    private BoardFragment boardFragment;
    private HomesFavoriteFragment homesFavoriteFragment;
    private SearchesFavoriteFragment searchFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        boardFragment = new BoardFragment();
        homesFavoriteFragment = new HomesFavoriteFragment();
        searchFragment = new SearchesFavoriteFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(boardFragment);
        fragments.add(homesFavoriteFragment);
        fragments.add(searchFragment);

        adapter = new FavoritePagerAdapter(view.getContext(), getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
