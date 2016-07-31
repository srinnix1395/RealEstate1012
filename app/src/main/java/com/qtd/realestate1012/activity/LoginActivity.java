package com.qtd.realestate1012.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.fragment.LoginUsernameFragment;

import butterknife.ButterKnife;

/**
 * Created by Dell on 7/28/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private LoginUsernameFragment usernameFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        usernameFragment = new LoginUsernameFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layoutLogin, usernameFragment);
        transaction.commit();
    }


}
