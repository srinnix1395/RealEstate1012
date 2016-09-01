package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.fragment.ListHouseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/17/2016.
 */
public class AllHouseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String type;
    private ListHouseFragment listHouseFragment;
    private String jsonBoard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_house);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra(ApiConstant.TYPE);
            jsonBoard = intent.getStringExtra(ApiConstant.LIST_BOARD);
        }
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(type);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listHouseFragment = new ListHouseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant.URL_WEB_SERVICE, ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE_OF_KIND);
        bundle.putString(ApiConstant.LIST_BOARD, jsonBoard);
        listHouseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.layout, listHouseFragment).commit();
    }


}
