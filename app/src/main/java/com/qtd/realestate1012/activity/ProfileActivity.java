package com.qtd.realestate1012.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.User;
import com.qtd.realestate1012.utils.AlertUtils;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 9/7/2016.
 */
public class ProfileActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvAccountType)
    TextView tvAccountType;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.switchNoti)
    SwitchCompat switchNoti;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.layoutInfo)
    RelativeLayout layoutInfo;

    ProgressDialog progressDialog;

    private User userOld;
    private Image avatar;
    private boolean isEditAvatar;
    private boolean isEditMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initViews();
        requestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_normal, menu);
        return true;
    }

    private void initViews() {
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 17) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.topMargin = UiUtils.getStatusBarHeight(this);
            toolbar.requestLayout();
        }

        getSupportActionBar().setTitle(R.string.yourProfile);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(this);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(ProfileActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
//            jsonRequest.put(ApiConstant._ID, HousieApplication.getInstance().getSharedPreUtils()
//                    .getString(ApiConstant._ID, ""));
            jsonRequest.put(ApiConstant._ID, "57d6b208f07077132325fed7");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant
                .URL_WEB_SERVICE_USER_INFO, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            Toast.makeText(ProfileActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseSuccess(response);
                            break;
                        }
                    }

                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);

                    layoutInfo.setVisibility(View.VISIBLE
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ProfileActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseSuccess(JSONObject response) throws JSONException {
        userOld = ProcessJson.getUserInfo(response);
        setData();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miEdit: {
                editProfile();
                break;
            }
            case R.id.miSave: {
                saveProfile();
                break;
            }
        }
        return false;
    }

    private void saveProfile() {
        User userNew = new User(userOld.getId(), etName.getText().toString(), userOld.getImage(), userOld.getEmail(),
                userOld.getProvider(), etPhone.getText().toString(), switchNoti.isChecked());

        if (userNew.equals(userOld) && !isEditAvatar) {
            disableEditMode();
            return;
        }

        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(ProfileActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        if (!isEditAvatar) {
            sendDataWithoutUpdateAvatar(userNew);
        } else {
            sendDataHasAvatar(userNew);
        }
    }

    private void disableEditMode() {
        isEditMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_profile_normal);

        tvName.setVisibility(View.VISIBLE);
        etName.setVisibility(View.INVISIBLE);
        tvPhone.setVisibility(View.VISIBLE);
        etPhone.setVisibility(View.INVISIBLE);
        switchNoti.setEnabled(false);
    }

    private void sendDataHasAvatar(User userNew) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, userNew.getId());
            jsonRequest.put(ApiConstant.EMAIL, userNew.getId());
            jsonRequest.put(ApiConstant.PROVIDER, userNew.getProvider());
            jsonRequest.put(ApiConstant.NAME, userNew.getName());
            jsonRequest.put(ApiConstant.TELEPHONE, userNew.getPhoneNumber());
            jsonRequest.put(ApiConstant.NOTIFICATION, userNew.isNoti());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ion.with(this)
                .load(ApiConstant.URL_WEB_SERVICE_UPDATE_INFO_HAS_AVATAR)
                .addQuery("data", jsonRequest.toString())
                .setMultipartFile("image", new File(avatar.getPath()))
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                        if (e != null) {
                            Toast.makeText(ProfileActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            JSONObject response = new JSONObject(result.getResult());
                            if (response.getString(ApiConstant.RESULT).equals(ApiConstant.FAILED)) {
                                Toast.makeText(ProfileActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            } else {
                                handleResponseSuccess(response);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        disableEditMode();
                        progressDialog.dismiss();

                        AlertUtils.showToastSuccess(ProfileActivity.this, R.drawable.ic_account_checked,
                                R.string.updateSuccessfully);
                    }
                });
    }

    private void sendDataWithoutUpdateAvatar(User userNew) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, userNew.getId());
            jsonRequest.put(ApiConstant.EMAIL, userNew.getId());
            jsonRequest.put(ApiConstant.PROVIDER, userNew.getProvider());
            jsonRequest.put(ApiConstant.NAME, userNew.getName());
            jsonRequest.put(ApiConstant.TELEPHONE, userNew.getPhoneNumber());
            jsonRequest.put(ApiConstant.NOTIFICATION, userNew.isNoti());
            jsonRequest.put(ApiConstant.AVATAR, userNew.getImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant
                .URL_WEB_SERVICE_UPDATE_INFO, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            setData();
                            disableEditMode();
                            Toast.makeText(ProfileActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseSuccess(response);
                            disableEditMode();
                            break;
                        }
                    }
                    progressDialog.dismiss();
                    AlertUtils.showToastSuccess(ProfileActivity.this, R.drawable.ic_account_checked,
                            R.string.updateSuccessfully);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                disableEditMode();
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void setData() {
        //nếu là ảnh của facebook hoặc google
        if (userOld.getImage().contains("http")) {
            Glide.with(this)
                    .load(userOld.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade(1000)
                    .error(R.drawable.ic_account_profile)
                    .into(imvImage);
        } else {
            //là ảnh của được lưu trên web service
            Glide.with(this)
                    .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_USER + userOld.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade(1000)
                    .error(R.drawable.ic_account_profile)
                    .into(imvImage);
        }

        tvName.setText(userOld.getName());
        etName.setText(userOld.getName());
        tvEmail.setText(userOld.getEmail());
        tvAccountType.setText(userOld.getProvider());
        tvPhone.setText(userOld.getPhoneNumber());
        etPhone.setText(userOld.getPhoneNumber());
        switchNoti.setChecked(userOld.isNoti());
    }

    private void editProfile() {
        isEditMode = true;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_profile_edit);

        tvName.setVisibility(View.INVISIBLE);
        etName.setVisibility(View.VISIBLE);
        tvPhone.setVisibility(View.INVISIBLE);
        etPhone.setVisibility(View.VISIBLE);
        switchNoti.setEnabled(true);
    }

    @OnClick(R.id.imvImage)
    void onClickImage() {
        if (isEditMode) {
            ImagePicker.create(this)
                    .single()
                    .showCamera(true)
                    .start(AppConstant.REQUEST_CODE_IMAGES_PICKER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_IMAGES_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            avatar = images.get(0);
            Glide.with(this)
                    .load(avatar.getPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imvImage);
            isEditAvatar = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isEditMode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.updateInfo)
                    .setMessage(R.string.confirmToFinish)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.create().show();
        } else {
            finish();
        }
    }
}
