package com.qtd.realestate1012.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qtd.realestate1012.R;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}