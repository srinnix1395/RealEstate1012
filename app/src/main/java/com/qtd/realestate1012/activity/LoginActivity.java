package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;

import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.fragment.LoginPasswordFragment;
import com.qtd.realestate1012.fragment.LoginUsernameFragment;

import butterknife.ButterKnife;

/**
 * Created by Dell on 7/28/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private LoginUsernameFragment usernameFragment;
    private LoginPasswordFragment passwordFragment;
    private String idHouse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        idHouse = intent.getStringExtra(ApiConstant._ID_HOUSE);
    }

    private void initView() {
        usernameFragment = new LoginUsernameFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_login, usernameFragment);
        transaction.commit();
    }


    public void showFragmentPassword(String email, String type) {
        passwordFragment = new LoginPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant.EMAIL, email);
        bundle.putString(ApiConstant.TYPE, type);
        passwordFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_login, passwordFragment);
        transaction.show(passwordFragment);
        transaction.hide(usernameFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        String email = usernameFragment.getEmail();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            HousieApplication.getInstance().getSharedPreUtils().putString(AppConstant.LAST_EMAIL_AT_LOGIN_ACTIVITY, email);
        }
        super.onDestroy();
    }

    public String getIdHouse() {
        return idHouse;
    }
}
