package com.qtd.realestate1012.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.asynctask.GeoCoderGetLatLngAsyncTask;
import com.qtd.realestate1012.callback.GeoCoderCallback;
import com.qtd.realestate1012.callback.OnDismissDialogCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.NumberPickerDialog;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 9/5/2016.
 */
public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GeoCoderCallback {
    @BindView(R.id.radioSale)
    RadioButton radioSale;

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
    EditText etDetailAddress;

    @BindView(R.id.tvLargeAddress)
    TextView tvLargeAddress;

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.tvArea)
    TextView tvArea;

    @BindView(R.id.tvNumberOfRoom)
    TextView tvNumberOfRoom;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<Image> images;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_house);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.postANewHouse);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinnerCity.setOnItemSelectedListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);

    }

    private void initData() {

    }

    @OnClick(R.id.layoutImage)
    void onClick() {
        ImagePicker.create(this)
                .multi()
                .limit(5)
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
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(PostActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateData()){
            Toast.makeText(PostActivity.this, R.string.pleaseInputInfo, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        String address = null;
        GeoCoderGetLatLngAsyncTask asyncTask = new GeoCoderGetLatLngAsyncTask(this, this);
        asyncTask.execute(address);
    }

    @Override
    public void onResult(LatLng latLng) {
        String url = ApiConstant.URL_WEB_SERVICE_POST_HOUSE;

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant.STATUS, radioSale.isChecked() ? getString(R.string.forSale)
                    : getString(R.string.forRent));
            jsonRequest.put(ApiConstant.PROPERTY_TYPE, spinnerProperty.getSelectedItem().toString());
            jsonRequest.put(ApiConstant.AREA, tvArea.getText().toString());
//            jsonRequest.put(ApiConstant.DETAIL_ADDRESS, )
            jsonRequest.put(ApiConstant.NUMBER_OF_ROOMS, tvNumberOfRoom.getText().toString());
            jsonRequest.put(ApiConstant.DESCRIPTION, etDescription.getText().toString());
            jsonRequest.put(ApiConstant.LATITUDE, latLng.latitude);
            jsonRequest.put(ApiConstant.LONGITUDE, latLng.longitude);
            jsonRequest.put(ApiConstant._ID_OWNER, HousieApplication.getInstance().getSharedPreUtils().getString
                    (ApiConstant._ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ion.with(this)
                .load(url)
                .addQuery("data", jsonRequest.toString())
                .addMultipartParts(getListImage())
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        progressDialog.dismiss();

                        String result = null;
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.getResult());
                            result = jsonObject.getString(ApiConstant.RESULT);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        if (e != null || result.equals(ApiConstant.FAILED)) {
                            Toast.makeText(PostActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        AlertUtils.showToastSuccess(PostActivity.this, R.drawable.ic_cloud_check,
                                R.string.postSuccessfully);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2500);
                    }
                });

    }


    private boolean validateData() {

        return true;
    }

    private List<Part> getListImage() {
        List<Part> parts = new ArrayList<>();
        for (Image image : images) {
            parts.add(new FilePart("image", new File(image.getPath())));
        }
        return parts;
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
