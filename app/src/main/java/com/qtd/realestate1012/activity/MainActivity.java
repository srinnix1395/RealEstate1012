package com.qtd.realestate1012.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.MainPagerAdapter;
import com.qtd.realestate1012.constant.AppConstant;
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
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_SELECTED[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[2]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[3]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[4]));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case AppConstant.LAST_TAB: {

                        break;
                    }
                    default: {
                        showFragment(tab);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layoutMain, homeFragment);
        transaction.commit();
    }

    private void showFragment(TabLayout.Tab tab) {
        int tabPosition = tab.getPosition();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }
        switch (tabPosition) {
            case 0: {
                transaction.show(homeFragment);
                break;
            }
            case 1: {
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    transaction.add(R.id.layoutMain, searchFragment);
                }
                transaction.show(searchFragment);
                break;
            }
            case 2: {
                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                    transaction.add(R.id.layoutMain, favoriteFragment);
                }
                transaction.show(favoriteFragment);
                break;
            }
            case 3: {
                if (notificationFragment == null) {
                    notificationFragment = new NotificationFragment();
                    transaction.add(R.id.layoutMain, notificationFragment);
                }
                transaction.show(notificationFragment);
                break;
            }
        }
        transaction.commit();

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i != tabPosition) {
                tabLayout.getTabAt(i).setIcon(AppConstant.ICON_TAB_NORMAL[i]);
            }
        }
        tab.setIcon(AppConstant.ICON_TAB_SELECTED[tab.getPosition()]);
    }
}
