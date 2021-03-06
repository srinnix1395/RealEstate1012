package com.qtd.realestate1012.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.database.DatabaseHelper;
import com.qtd.realestate1012.model.ItemSavedSearch;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    @BindView(R.id.tvPropertyType)
    TextView tvProperty;

    @BindView(R.id.tvSearch)
    TextView tvSearch;

    @BindView(R.id.radioSale)
    RadioButton radioSale;

    @BindView(R.id.radioRent)
    RadioButton radioRent;

    ProgressDialog progressDialog;

    private ArrayList<TextView> arrayListText;

    private int numberOfRooms;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (request != null && !request.isCanceled()) {
            request.cancel();
        }
        super.onDestroy();
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(true);
    }

    @OnClick({R.id.radioSale, R.id.radioRent, R.id.tvRoomAny, R.id.tvRoom1, R.id.tvRoom2, R.id.tvRoom3, R.id.tvRoom4,
            R.id.tvCancel, R.id.tvReset, R.id.tvSearch, R.id.layoutProperty})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioSale: {
                changeDataSpinnerPriceRange(R.id.radioSale);
                break;
            }
            case R.id.radioRent: {
                changeDataSpinnerPriceRange(R.id.radioRent);
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
            case R.id.layoutProperty: {
                showDialogSelectProperty();
                break;
            }
        }
    }

    private void showDialogSelectProperty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.propertyType);

        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_property_type, null);
        builder.setView(view);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioProperty);

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

    private JSONObject getCriteria() {
        String status = radioGroup.getCheckedRadioButtonId() == R.id.radioSale ? ApiConstant.SALE : ApiConstant.RENT;

        int priceFrom;
        if (spinnerPriceFrom.getSelectedItem().toString().equals(ApiConstant.ANY)) {
            priceFrom = 0;
        } else {
            priceFrom = StringUtils.getPriceNumber(spinnerPriceFrom.getSelectedItem().toString());
        }
        int priceTo;
        if (spinnerPriceTo.getSelectedItem().toString().equals(ApiConstant.ANY)) {
            priceTo = 0;
        } else {
            priceTo = StringUtils.getPriceNumber(spinnerPriceTo.getSelectedItem().toString());
        }

        String areaFrom;
        if (spinnerAreaFrom.getSelectedItem().toString().equals(ApiConstant.ANY)) {
            areaFrom = "0";
        } else {
            areaFrom = StringUtils.getAreaNumber(spinnerAreaFrom.getSelectedItem().toString());
        }

        String areaTo;
        if (spinnerAreaTo.getSelectedItem().toString().equals(ApiConstant.ANY)) {
            areaTo = "0";
        } else {
            areaTo = StringUtils.getAreaNumber(spinnerAreaTo.getSelectedItem().toString());
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiConstant.STATUS, status);
            jsonObject.put(ApiConstant.PRICE_FROM, priceFrom);
            jsonObject.put(ApiConstant.PRICE_TO, priceTo);
            jsonObject.put(ApiConstant.NUMBER_OF_ROOMS, numberOfRooms);
            jsonObject.put(ApiConstant.AREA_FROM, areaFrom);
            jsonObject.put(ApiConstant.AREA_TO, areaTo);
            jsonObject.put(ApiConstant.PROPERTY_TYPE, tvProperty.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private void performSearch() {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(FilterActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = getCriteria();

        Intent intent = new Intent(FilterActivity.this, ResultActivity.class);
        intent.putExtra(ApiConstant.CRITERIA, jsonObject.toString());
        startActivity(intent);
    }


    private void resetForm() {
        radioGroup.check(R.id.radioSale);
        changeDataSpinnerPriceRange(R.id.radioSale);
        tvRoomAny.performClick();

        spinnerAreaFrom.setSelection(0, true);
        spinnerAreaTo.setSelection(0, true);
        tvProperty.setText(R.string.any);
    }

    private void changeSelectedRoom(TextView textViewSelected) {
        for (TextView tv : arrayListText) {
            if (tv == textViewSelected) {
                tv.setBackgroundResource(R.drawable.background_number_room_selected);
                tv.setTextColor(Color.WHITE);
                numberOfRooms = arrayListText.indexOf(tv);
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
                adapterFrom = ArrayAdapter.createFromResource(this, R.array.price_range_from_sale, android.R.layout.simple_spinner_dropdown_item);
                adapterTo = ArrayAdapter.createFromResource(this, R.array.price_range_to_sale, android.R.layout.simple_spinner_dropdown_item);
                break;
            }
            case R.id.radioRent: {
                adapterFrom = ArrayAdapter.createFromResource(this, R.array.price_range_from_rent, android.R.layout.simple_spinner_dropdown_item);
                adapterTo = ArrayAdapter.createFromResource(this, R.array.price_range_to_rent, android.R.layout.simple_spinner_dropdown_item);
                break;
            }
        }
        spinnerPriceFrom.setAdapter(adapterFrom);
        spinnerPriceTo.setAdapter(adapterTo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSaveSearch: {
                onClickMenuItemSaveSearch();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickMenuItemSaveSearch() {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(FilterActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean userLoggedIn = HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false);
        if (userLoggedIn) {
            showDialogSaveSearch();
        } else {
            Intent intent = new Intent(FilterActivity.this, LoginActivity.class);
            startActivityForResult(intent, AppConstant.REQUEST_CODE_SIGN_IN);
        }
    }

    private void showDialogSaveSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.search);
        builder.setMessage(R.string.confirmSaveSearch);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickSaveSearch();
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

    private void clickSaveSearch() {
        progressDialog.show();

        JSONObject jsonRequest = getCriteria();
        try {
            jsonRequest.put(ApiConstant._ID_OWNER, HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_SAVE_SEARCH, jsonRequest
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            progressDialog.dismiss();
                            Toast.makeText(FilterActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseSaveSearchSuccess(response);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(FilterActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
            }
        });

        HousieApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_SIGN_IN && resultCode == RESULT_OK) {
            onClickMenuItemSaveSearch();
        }
    }

    private void handleResponseSaveSearchSuccess(final JSONObject response) {
        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ItemSavedSearch itemSavedSearch = ProcessJson.getItemSavedSearch(response);

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(FilterActivity.this);
                databaseHelper.insertSavedSearch(itemSavedSearch);

                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Boolean>() {
                    @Override
                    public void onSuccess(Boolean value) {
                        progressDialog.dismiss();
                        AlertUtils.showToastSuccess(FilterActivity.this, R.drawable.ic_playlist_check, R.string.saveSearchSuccessfully);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(FilterActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
