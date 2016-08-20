package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.fragment.FavoriteFragment;
import com.qtd.realestate1012.fragment.HomeFragment;
import com.qtd.realestate1012.fragment.NotificationFragment;
import com.qtd.realestate1012.fragment.SearchFragment;
import com.qtd.realestate1012.utils.ImageUtils;
import com.qtd.realestate1012.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private FavoriteFragment favoriteFragment;
    private NotificationFragment notificationFragment;
    private PopupMenu popupMenu;

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
                        showPopUpMenu();
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
                if (tab.getPosition() == AppConstant.LAST_TAB) {
                    showPopUpMenu();
                }
            }
        });

        homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layoutMain, homeFragment);
        transaction.commit();

        if (ServiceUtils.isNetworkAvailable(this) && !HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AppConstant.REQUEST_CODE_SIGN_IN);
        }
    }

    private void showPopUpMenu() {
        if (popupMenu == null) {
            popupMenu = new PopupMenu(this, findViewById(R.id.viewAnchor));
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.miLogin: {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.miSetting: {

                            break;
                        }
                    }
                    return false;
                }
            });
        }
        popupMenu.show();
    }

    private void showFragment(TabLayout.Tab tab) {
        int tabPosition = tab.getPosition();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }
        switch (tabPosition) {
            case 0: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tabLayout.setElevation(ImageUtils.convertDpToPixel(this, 4));
                }
                transaction.show(homeFragment);
                break;
            }
            case 1: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tabLayout.setElevation(0);
                }
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    transaction.add(R.id.layoutMain, searchFragment);
                }
                transaction.show(searchFragment);
                break;
            }
            case 2: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tabLayout.setElevation(0);
                }
                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                    transaction.add(R.id.layoutMain, favoriteFragment);
                }
                transaction.show(favoriteFragment);
                break;
            }
            case 3: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tabLayout.setElevation(ImageUtils.convertDpToPixel(this, 4));
                }
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
