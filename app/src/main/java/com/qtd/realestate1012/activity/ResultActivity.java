package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.HouseHasHeartAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.CompactHouse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 9/26/2016.
 */
public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.tvError)
    TextView tvError;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<CompactHouse> arrayList;
    private HouseHasHeartAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(R.string.listResult);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (arrayList.size() == 0) {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        arrayList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<CompactHouse> houses = intent.getParcelableArrayListExtra(ApiConstant.LIST_HOUSE);
            arrayList.addAll(houses);
        }

        adapter = new HouseHasHeartAdapter(arrayList);
    }
}
