package com.qtd.realestate1012.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qtd.realestate1012.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/17/2016.
 */
public class FilterActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinnerPriceFrom)
    AppCompatSpinner spinnerPriceFrom;

    @BindView(R.id.spinnerPriceTo)
    AppCompatSpinner spinnerPriceTo;

    @BindView(R.id.spinnerAreaFrom)
    AppCompatSpinner spinnerAreaFrom;

    @BindView(R.id.spinnerAreaTo)
    AppCompatSpinner spinnerAreaTo;

    @BindView(R.id.tvRoomAny)
    TextView tvRoomAny;

    @BindView(R.id.tvRoom1)
    TextView tvRoom1;

    @BindView(R.id.tvRoom2)
    TextView tvRoom2;

    @BindView(R.id.tvRoom3)
    TextView tvRoom3;

    @BindView(R.id.tvRoom4)
    TextView tvRoom4;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private ArrayList<TextView> arrayListText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(R.string.search);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        arrayListText = new ArrayList<>();
        arrayListText.add(tvRoomAny);
        arrayListText.add(tvRoom1);
        arrayListText.add(tvRoom2);
        arrayListText.add(tvRoom3);
        arrayListText.add(tvRoom4);
    }

    @OnClick({R.id.radioSale, R.id.radioRent, R.id.tvRoomAny, R.id.tvRoom1, R.id.tvRoom2, R.id.tvRoom3, R.id.tvRoom4,
            R.id.tvCancel, R.id.tvReset, R.id.tvSearch})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioSale: {
                if (radioGroup.getCheckedRadioButtonId() != R.id.radioSale) {
                    changeDataSpinnerPriceRange(R.id.radioSale);
                }
                break;
            }
            case R.id.radioRent: {
                if (radioGroup.getCheckedRadioButtonId() != R.id.radioRent) {
                    changeDataSpinnerPriceRange(R.id.radioRent);
                }
                break;
            }
            case R.id.tvRoomAny: {
                changeSelectedRoom(tvRoomAny);
                break;
            }
            case R.id.tvRoom1: {
                changeSelectedRoom(tvRoom1);
                break;
            }
            case R.id.tvRoom2: {
                changeSelectedRoom(tvRoom2);
                break;
            }
            case R.id.tvRoom3: {
                changeSelectedRoom(tvRoom3);
                break;
            }
            case R.id.tvRoom4: {
                changeSelectedRoom(tvRoom4);
                break;
            }
            case R.id.tvCancel: {
                finish();
            }
            case R.id.tvReset: {
                resetForm();
                break;
            }
            case R.id.tvSearch: {
                performSearch();
                break;
            }
        }
    }

    private void performSearch() {

    }

    private void resetForm() {
        radioGroup.check(R.id.radioSale);
        changeDataSpinnerPriceRange(R.id.radioSale);
        tvRoomAny.performClick();
        spinnerAreaFrom.setSelection(0, true);
        spinnerAreaTo.setSelection(0, true);
    }

    private void changeSelectedRoom(TextView textViewSelected) {
        for (TextView tv : arrayListText) {
            if (tv == textViewSelected) {
                tv.setBackgroundResource(R.drawable.background_number_room_selected);
                tv.setTextColor(Color.WHITE);
            } else {
                tv.setBackgroundResource(R.drawable.background_number_room);
                tv.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
            }
        }

    }

    private void changeDataSpinnerPriceRange(int resID) {
        ArrayAdapter<CharSequence> adapterFrom = null;
        ArrayAdapter<CharSequence> adapterTo = null;
        switch (resID) {
            case R.id.radioSale: {
                adapterFrom = ArrayAdapter.createFromResource(this, R.array.price_range_from_sale, android.R.layout.simple_spinner_item);
                adapterTo = ArrayAdapter.createFromResource(this, R.array.price_range_to_sale, android.R.layout.simple_spinner_item);
                break;
            }
            case R.id.radioRent: {
                adapterFrom = ArrayAdapter.createFromResource(this, R.array.price_range_from_rent, android.R.layout.simple_spinner_item);
                adapterTo = ArrayAdapter.createFromResource(this, R.array.price_range_to_rent, android.R.layout.simple_spinner_item);
                break;
            }
        }
        spinnerPriceFrom.setAdapter(adapterFrom);
        spinnerPriceTo.setAdapter(adapterTo);
    }
}
