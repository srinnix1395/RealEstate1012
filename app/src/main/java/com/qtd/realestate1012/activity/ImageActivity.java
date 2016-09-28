package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.qtd.realestate1012.R;
import com.qtd.realestate1012.adapter.ImageAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 9/27/2016.
 */
public class ImageActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.circlePageIndicator)
    CirclePageIndicator circlePageIndicator;

    private ImageAdapter adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initData();
        initViews();
    }

    private void initViews() {
        viewPager.setAdapter(adapter);

        circlePageIndicator.setFillColor(ContextCompat.getColor(this, R.color.colorGray));
        circlePageIndicator.setPageColor(ContextCompat.getColor(this, R.color.colorDarkGray));
        circlePageIndicator.setViewPager(viewPager);
    }

    private void initData() {
        arrayList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            String[] temp = intent.getStringArrayExtra(ApiConstant.IMAGE);
            for (int i = 0; i < temp.length; i++) {
                arrayList.add(temp[i]);
            }
        }

        adapter = new ImageAdapter(getSupportFragmentManager(), arrayList);
    }
}
