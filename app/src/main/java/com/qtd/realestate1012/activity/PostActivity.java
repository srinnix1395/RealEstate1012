package com.qtd.realestate1012.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.callback.OnDismissDialogCallback;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.NumberPickerDialog;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 9/5/2016.
 */
public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.tvImages)
    TextView tvImages;

    @BindView(R.id.spinnerProperty)
    AppCompatSpinner spinnerProperty;

    @BindView(R.id.spinnerCity)
    AppCompatSpinner spinnerCity;

    @BindView(R.id.spinnerDistrict)
    AppCompatSpinner spinnerDistrict;

    @BindView(R.id.spinnerWard)
    AppCompatSpinner spinnerWard;

    @BindView(R.id.spinnerStreet)
    AppCompatSpinner spinnerStreet;

    @BindView(R.id.etDetailAddress)
    EditText etDetailAdress;

    @BindView(R.id.tvLargeAddress)
    TextView tvLargeAddress;

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.tvArea)
    TextView tvArea;

    @BindView(R.id.tvNumberOfRoom)
    TextView tvNumberOfRoom;

    private ArrayList<Image> images;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_house);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initViews() {
        spinnerCity.setOnItemSelectedListener(this);
    }

    private void initData() {

    }

    @OnClick(R.id.layoutImage)
    void onClick() {
        Log.e("sdf", "onClick: he");
        ImagePicker.create(this)
                .multi()
                .limit(7)
                .origin(images)
                .showCamera(true)
                .start(AppConstant.REQUEST_CODE_IMAGES_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_IMAGES_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            tvImages.setText(images.size() + getString(R.string.imageSelected));
        }
    }

    @OnClick(R.id.layoutArea)
    void onClickLayoutArea() {
        NumberPickerDialog dialog = new NumberPickerDialog(this, AppConstant.TYPE_PICKER_AREA, new
                OnDismissDialogCallback() {
                    @Override
                    public void onDismissListener(int result, int data) {
                        if (result == RESULT_OK) {
                            tvArea.setText(String.format(Locale.getDefault(), "%d m2", data));
                        }
                    }
                });
        dialog.show();
    }

    @OnClick(R.id.layoutNumberOfRoom)
    void onClickLayoutNumberOfRoom() {
        NumberPickerDialog dialog = new NumberPickerDialog(this, AppConstant.TYPE_PICKER_NUMBER_OF_ROOM, new
                OnDismissDialogCallback() {
                    @Override
                    public void onDismissListener(int result, int data) {
                        if (result == RESULT_OK) {
                            tvArea.setText(String.format(Locale.getDefault(), "%d %s", data, getString(R.string.room)));
                        }
                    }
                });
        dialog.show();
    }

    @OnClick(R.id.tvPost)
    void onClickTvPost() {

    }

    @OnClick(R.id.tvReset)
    void onClickTvReset() {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
