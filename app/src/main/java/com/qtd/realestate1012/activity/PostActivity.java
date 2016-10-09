package com.qtd.realestate1012.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.District;
import com.qtd.realestate1012.model.Street;
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
public class PostActivity extends AppCompatActivity implements GeoCoderCallback {
    @BindView(R.id.radioSale)
    RadioButton radioSale;

    @BindView(R.id.tvImages)
    TextView tvImages;

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

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.tvArea)
    TextView tvArea;

    @BindView(R.id.tvNumberOfRoom)
    TextView tvNumberOfRoom;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvPropertyType)
    TextView tvProperty;

    @BindView(R.id.tvStreet)
    TextView tvStreet;

    @BindView(R.id.tvDistrict)
    TextView tvDistrict;

    @BindView(R.id.tvCity)
    TextView tvCity;

    private ArrayList<Image> images;
    ProgressDialog progressDialog;

    private ArrayAdapter districtAdapter;
    private ArrayList<District> districtArrayList;
    private ArrayList<Street> streetArrayList;
    private ArrayAdapter streetAdapter;
    private ArrayList<Street> streetsByDistrict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_house);
        ButterKnife.bind(this);
        initViews();
        initData();
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);

    }

    private void initData() {
        DatabaseHelper myDatabase = DatabaseHelper.getInstance(this);

        streetsByDistrict.clear();
        districtArrayList = myDatabase.getListDistrict();
        streetArrayList = myDatabase.getListStreet();
        districtAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, districtArrayList);
        spinnerDistrict.setAdapter(districtAdapter);
        displayText(districtArrayList.get(0).getDistrictName(), tvDistrict);
        getStreetsByDistrict(0);
        streetAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, streetsByDistrict);
        spinnerStreet.setAdapter(streetAdapter);

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStreetsByDistrict(position);
                streetAdapter.notifyDataSetChanged();
                displayText(", " + districtArrayList.get(position).getDistrictName(), tvDistrict);
                displayText(", " + streetsByDistrict.get(spinnerStreet.getSelectedItemPosition()).getStreetName(), tvStreet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayText(", " + streetsByDistrict.get(position).getStreetName(), tvStreet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getStreetsByDistrict(int position) {
        streetsByDistrict.clear();
        int idDistrict = districtArrayList.get(position).getIdDistrict();
        for (Street str : streetArrayList) {
            if (str.getIdDistrict() == idDistrict) {
                streetsByDistrict.add(str);
            }
        }
    }

    public void displayText(String text, TextView textView) {
        if (textView == null)
            return;
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            textView.setText(text);
        } else {
            textView.setText("");
        }

    }


    @OnClick(R.id.layoutProperty)
    void onClickProperty() {
        showDialogSelectProperty();
    }

    private void showDialogSelectProperty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.propertyType);

        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_property_type, null);
        builder.setView(view);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioProperty);
        RadioButton radioAny = (RadioButton) view.findViewById(R.id.radioAny);
        radioAny.setVisibility(View.GONE);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioButton radioSelected = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                tvProperty.setText(radioSelected.getText());
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
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

        if (!validateData()) {
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
        if (latLng != null) {
            String url = ApiConstant.URL_WEB_SERVICE_POST_HOUSE;

            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put(ApiConstant.AREA, tvArea.getText().toString());
                jsonRequest.put(ApiConstant.CITY, tvCity.getText().toString());
                jsonRequest.put(ApiConstant.DESCRIPTION, etDescription.getText().toString());
                jsonRequest.put(ApiConstant.DETAIL_ADDRESS, etDetailAddress.getText().toString());
                jsonRequest.put(ApiConstant.DISTRICT, tvDistrict.getText().toString());
                jsonRequest.put(ApiConstant.LATITUDE, latLng.latitude);
                jsonRequest.put(ApiConstant.LONGITUDE, latLng.longitude);
                jsonRequest.put(ApiConstant.NUMBER_OF_ROOMS, tvNumberOfRoom.getText().toString());
//                price
                jsonRequest.put(ApiConstant.PROPERTY_TYPE, tvProperty.getText().toString());
                jsonRequest.put(ApiConstant.STATUS, radioSale.isChecked() ? getString(R.string.forSale) : getString(R.string.forRent));
                jsonRequest.put(ApiConstant.STREET, tvStreet.getText().toString());
                jsonRequest.put(ApiConstant.WARD, "");

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
        } else {
            //// TODO: 10/9/2016 không tìm thấy tọa độ
        }
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
}
