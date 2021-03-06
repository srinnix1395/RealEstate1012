package com.qtd.realestate1012.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.asynctask.UpdateFirebaseRegId;
import com.qtd.realestate1012.callback.ActivityCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.DialogSignOut;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.fragment.FavoriteFragment;
import com.qtd.realestate1012.fragment.HomeFragment;
import com.qtd.realestate1012.fragment.NotificationFragment;
import com.qtd.realestate1012.fragment.SearchFragment;
import com.qtd.realestate1012.messageevent.MessageSignOutResult;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ActivityCallback {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private HomeFragment homeFragment;
    private FavoriteFragment favoriteFragment;
    private NotificationFragment notificationFragment;
    private SearchFragment searchFragment;

    private Bundle bundleHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
        updateRegId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.getAction().equals(AppConstant.ACTION_NOTIFICATION)) {
            bundleHomeFragment = intent.getExtras();
        }
    }

    private void updateRegId() {
        boolean isUserLoggedIn = HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false);
        boolean hasDeviceToken = HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.DEVICE_TOKEN, false);

        if (!hasDeviceToken && isUserLoggedIn && ServiceUtils.isNetworkAvailable(this)) {
            String id = HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "");
            String token = FirebaseInstanceId.getInstance().getToken();

            UpdateFirebaseRegId.updateRegId(id, token);
        }
    }

    private void initView() {
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_SELECTED[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[2]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[3]));
        tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_NORMAL[4]));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        homeFragment.setArguments(bundleHomeFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layoutMain, homeFragment);
        transaction.commit();
    }

    private void showPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.viewAnchor));

        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_not_signed_in, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.miLogin: {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, AppConstant.REQUEST_CODE_SIGN_IN);
                            break;
                        }
                        case R.id.miSetting: {

                            break;
                        }
                    }
                    return false;
                }
            });
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.menu_signed_in, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.miProfile: {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.miPostedHouse: {
                            Intent intent = new Intent(MainActivity.this, MyPostedHouseActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.miLogOut: {
                            onClickLogout();
                            break;
                        }
                        case R.id.miSetting: {

                            break;
                        }
                        case R.id.miPostHouse: {
                            Intent intent = new Intent(MainActivity.this, PostActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    return false;
                }
            });
        }
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN && resultCode == RESULT_OK) {
            homeFragment.updateData(null);
        }
    }

    private void onClickLogout() {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(MainActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        DialogSignOut mDialog = new DialogSignOut(this, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant.PROVIDER, ""));
        mDialog.setCancelable(false);
        mDialog.setTitle(R.string.logout);
        mDialog.setMessage(getString(R.string.are_you_sure_to_log_out));
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((DialogSignOut) dialogInterface).logout();
            }
        });
        mDialog.show();
    }

    @Subscribe
    public void handleMessageSignOut(MessageSignOutResult message) {
        processLogout(message.result);
    }

    private void processLogout(String s) {
        if (s.equals(ApiConstant.SUCCESS)) {
            clearData();
        } else {
            Toast.makeText(MainActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearData() {
        HousieApplication.getInstance().getSharedPreUtils().removeUserData();

//        home fragment
        homeFragment.clearUserData();

//        search fragment
        if (searchFragment != null && searchFragment.isVisible()) {
            searchFragment.clearUserData();
        }

//        favorite fragment
        if (favoriteFragment != null) {
            favoriteFragment.clearUserData();
        }

//        notification fragment
        //// TODO: 9/18/2016 notification fragment
        if (notificationFragment != null) {
            notificationFragment.clearUserData();
        }

        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
                databaseHelper.clearUserData();
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Boolean>() {
                    @Override
                    public void onSuccess(Boolean value) {
                        AlertUtils.showToastSuccess(MainActivity.this, R.drawable.ic_account_checked, R.string.logoutSuccessfully);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    private void showFragment(TabLayout.Tab tab) {
        int tabPosition = tab.getPosition();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
        switch (tabPosition) {
            case 0: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tabLayout.setElevation(UiUtils.convertDpToPixel(this, 4));
                }
                transaction.show(homeFragment);
                break;
            }
            case 1: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tabLayout.setElevation(0);
                }
                searchFragment = new SearchFragment();
                transaction.add(R.id.layoutMain, searchFragment);
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
                    tabLayout.setElevation(UiUtils.convertDpToPixel(this, 4));
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


    @Override
    public void showSearchFragment() {
        tabLayout.getTabAt(AppConstant.SEARCH_FRAGMENT_TAB).select();
    }

    @Override
    public void updateDataFavoriteFragment() {
        if (favoriteFragment != null) {
            favoriteFragment.updateDataLikeHouse();
        }
    }
}
