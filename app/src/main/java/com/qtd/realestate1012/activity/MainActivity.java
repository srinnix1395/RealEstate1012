package com.qtd.realestate1012.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

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
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(AppConstant.ICON_TAB_SELECTED[0]);
        tabLayout.getTabAt(1).setIcon(AppConstant.ICON_TAB_NORMAL[1]);
        tabLayout.getTabAt(2).setIcon(AppConstant.ICON_TAB_NORMAL[2]);
        tabLayout.getTabAt(3).setIcon(AppConstant.ICON_TAB_NORMAL[3]);
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[4]));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case AppConstant.LAST_TAB: {

                        break;
                    }
                    default: {
                        int tabPosition = tab.getPosition();
                        for (int i = 0; i < tabLayout.getTabCount(); i++) {
                            if (i != tabPosition) {
                                tabLayout.getTabAt(i).setIcon(AppConstant.ICON_TAB_NORMAL[i]);
                            }
                        }
                        tab.setIcon(AppConstant.ICON_TAB_SELECTED[tab.getPosition()]);
                        viewPager.setCurrentItem(tab.getPosition(), false);
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
    }
}
