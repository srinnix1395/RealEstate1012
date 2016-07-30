package com.qtd.realestate1012.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.MainPagerAdapter;
import com.qtd.realestate1012.fragment.FavoriteFragment;
import com.qtd.realestate1012.fragment.HomeFragment;
import com.qtd.realestate1012.fragment.NotificationFragment;
import com.qtd.realestate1012.fragment.SearchFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private MainPagerAdapter adapter;
    private ArrayList<Fragment> arrayListFragments;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private FavoriteFragment favoriteFragment;
    private NotificationFragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        favoriteFragment = new FavoriteFragment();
        notificationFragment = new NotificationFragment();
        arrayListFragments = new ArrayList<>();
        arrayListFragments.add(homeFragment);
        arrayListFragments.add(searchFragment);
        arrayListFragments.add(favoriteFragment);
        arrayListFragments.add(notificationFragment);
        adapter = new MainPagerAdapter(this, getSupportFragmentManager(), arrayListFragments);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_dot_vertical));
    }
}
