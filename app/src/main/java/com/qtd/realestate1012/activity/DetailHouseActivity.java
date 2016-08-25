package com.qtd.realestate1012.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.FullHouse;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/22/2016.
 */
public class DetailHouseActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvAddressToolbar)
    TextView tvAddressToolbar;

    @BindView(R.id.tvAddressCityToolbar)
    TextView tvAddressCityToolbar;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindView(R.id.tvType)
    TextView tvType;

    @BindView(R.id.tvIntro)
    TextView tvIntro;

    @BindView(R.id.tvSeeMoreIntro)
    TextView tvSeeMoreIntro;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvArea)
    TextView tvArea;

    @BindView(R.id.tvPriceInfo)
    TextView tvPriceInfo;

    @BindView(R.id.tvPriceOverArea)
    TextView tvPriceOverArea;

    @BindView(R.id.tvNumberOfRoom)
    TextView tvNumberOfRoom;

    @BindView(R.id.tvPropertyType)
    TextView tvPropertyType;

    @BindView(R.id.tvAddedOn)
    TextView tvAddedOn;

    @BindView(R.id.tvAddressInfo)
    TextView tvAddressInfo;

    @BindView(R.id.tvSeeMoreInfo)
    TextView tvSeeMoreInfo;

    @BindView(R.id.tvPhoneAgent)
    TextView tvPhoneAgent;

    @BindView(R.id.tvNameOwner)
    TextView tvNameOwner;

    @BindView(R.id.tvPhoneOwner)
    TextView tvPhoneOwner;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.layoutInfo)
    LinearLayout layoutInfo;
    private String id;
    private FullHouse fullHouse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra(ApiConstant._ID);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        requestData();
    }

    @Override
    public void onScrollChanged() {
        int y = scrollView.getScrollY();
        Log.e("scrool", "onScrollChanged: " + y);

    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(this)) {

            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_DETAIL_HOUSE, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {

                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            setData(response);
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

            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void setData(JSONObject response) {
        fullHouse = ProcessJson.getDetailInfoHouse(response);

        tvAddressToolbar.setText(fullHouse.getAddress());

        tvPrice.setText(fullHouse.getPrice());
        tvInfo.setText(fullHouse.getNumberOfRoom() + ", " + fullHouse.getArea());
        tvType.setText(fullHouse.getPropertyType());
        tvIntro.setText(fullHouse.getDescription());

        tvStatus.setText(fullHouse.getStatus());
        tvArea.setText(String.valueOf(fullHouse.getArea()));
        tvPriceInfo.setText(fullHouse.getPrice() + getString(R.string.currency));
        tvPriceOverArea.setText(((int) (fullHouse.getPrice() / fullHouse.getArea())) + getString(R.string.currency));
        tvNumberOfRoom.setText(String.valueOf(fullHouse.getNumberOfRoom()));
        tvPropertyType.setText(fullHouse.getPropertyType());
//        tvAddedOn
        tvAddressInfo.setText(fullHouse.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_house_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miHeart: {
                onClickMenuLike();
                break;
            }
            case R.id.miShare: {
                onClickMenuShare();
                break;
            }
        }
        return true;
    }

    private void onClickMenuLike() {

    }

    private void onClickMenuShare() {

    }

    @OnClick({R.id.tvDirection, R.id.tvMapView, R.id.tvEarthView, R.id.tvSeeMoreIntro, R.id.tvSeeMoreInfo
            , R.id.tvCallAgent, R.id.tvCallOwner, R.id.btnSend})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDirection: {
                onClickTvDirection();
                break;
            }
            case R.id.tvMapView: {
                onClickTvMapView();
                break;
            }
            case R.id.tvEarthView: {
                onClickTvEarthView();
                break;
            }
            case R.id.tvSeeMoreIntro: {
                onClickTvSeeMoreIntro();
                break;
            }
            case R.id.tvSeeMoreInfo: {
                onClickTvMoreInfo();
                break;
            }
            case R.id.tvCallAgent: {
                onClickTvCallAgent();
                break;
            }
            case R.id.tvCallOwner: {
                onClickTvCallOwner();
                break;
            }
            case R.id.btnSend: {
                onClickBtnSend();
                break;
            }
        }
    }

    private void onClickBtnSend() {

    }

    private void onClickTvCallOwner() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tvPhoneOwner.getText().toString()));
        startActivity(intent);


//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + tvPhoneOwner.getText().toString()));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, AppConstant.REQUEST_CODE_CALL_PHONE_PERMISSION_OWNER);
//            } else {
//                startActivity(intent);
//            }
//        } else {
//            startActivity(intent);
//        }
    }

    private void onClickTvCallAgent() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tvPhoneOwner.getText().toString()));
        startActivity(intent);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, AppConstant.REQUEST_CODE_CALL_PHONE_PERMISSION_AGENT);
//            } else {
//                startActivity(intent);
//            }
//        } else {
//            startActivity(intent);
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case AppConstant.REQUEST_CODE_CALL_PHONE_PERMISSION_AGENT: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + tvPhoneOwner.getText().toString()));
//                    startActivity(intent);
//                }
//                break;
//            }
//            case AppConstant.REQUEST_CODE_CALL_PHONE_PERMISSION_OWNER: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + tvPhoneOwner.getText().toString()));
//                    startActivity(intent);
//                }
//                break;
//            }
//        }
//    }

    private void onClickTvMoreInfo() {
        if (layoutInfo.getLayoutParams().height == AppConstant.HEIGHT_LAYOUT_INFO_COLLAPSED) {
            ObjectAnimator animator = ObjectAnimator.ofInt(layoutInfo, "layout_height", AppConstant.HEIGHT_LAYOUT_INFO_EXPANDED);
            animator.setDuration(2000).start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofInt(layoutInfo, "layout_height", AppConstant.HEIGHT_LAYOUT_INFO_COLLAPSED);
            animator.setDuration(2000).start();
        }
    }

    private void onClickTvSeeMoreIntro() {

        if (tvIntro.getMaxLines() == AppConstant.MAX_LINES_TV_INTRO_COLLAPSED) {
            ObjectAnimator animator = ObjectAnimator.ofInt(tvIntro, "maxLines", AppConstant.MAX_LINES_TV_INTRO_EXPANDED);
            animator.setDuration(1000);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();

            tvSeeMoreIntro.setText(R.string.seeLess);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvIntro.setMaxLines(AppConstant.MAX_LINES_TV_INTRO_EXPANDED);
                }
            }, 1000);
            return;
        }

        if (tvIntro.getMaxLines() == AppConstant.MAX_LINES_TV_INTRO_EXPANDED) {
            ObjectAnimator animator = ObjectAnimator.ofInt(tvIntro, "maxLines", AppConstant.MAX_LINES_TV_INTRO_COLLAPSED);
            animator.setDuration(1000);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();

            tvSeeMoreIntro.setText(R.string.seeMore);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvIntro.setMaxLines(AppConstant.MAX_LINES_TV_INTRO_COLLAPSED);
                }
            }, 1000);

        }
    }

    private void onClickTvEarthView() {

    }

    private void onClickTvMapView() {
        Intent intent = new Intent(this, MapViewActivity.class);
        startActivity(intent);
    }

    private void onClickTvDirection() {

    }


}
