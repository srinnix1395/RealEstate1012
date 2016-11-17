package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.FullHouse;
import com.qtd.realestate1012.utils.ProcessJson;
import com.qtd.realestate1012.utils.ServiceUtils;
import com.qtd.realestate1012.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DELL on 8/22/2016.
 */
public class HouseDetailActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener {
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

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.tvImages)
    TextView tvImages;

    @BindView(R.id.etContent)
    EditText etContentEmail;

    @BindView(R.id.imvImage)
    ImageView imvImage;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.layoutTotalInfo)
    LinearLayout layoutTotalInfo;

    @BindView(R.id.tvIntro)
    TextView tvIntro;

    @BindView(R.id.tvSeeMoreIntro)
    TextView tvSeeMoreIntro;

    @BindView(R.id.layoutInfo)
    LinearLayout layoutInfo;

    private String id;
    private FullHouse fullHouse;
    private boolean isLiked;

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
        isLiked = intent.getBooleanExtra(ApiConstant.LIKE, false);
        Log.e("fsaj", "initData: " + id);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (Build.VERSION.SDK_INT >= 17) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.topMargin = UiUtils.getStatusBarHeight(this);
            toolbar.requestLayout();
        }

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        layoutTotalInfo.setVisibility(View.INVISIBLE);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        requestData();
    }

    @Override
    public void onScrollChanged() {
        int y = scrollView.getScrollY();
        //// TODO: 10/1/2016 animation button request info
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(this)) {
            Toast.makeText(HouseDetailActivity.this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
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
                            Toast.makeText(HouseDetailActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            progressBar.setEnabled(false);
                            progressBar.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            setData(response);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HouseDetailActivity.this, R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
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

    private void setData(final JSONObject response) {
        Single.fromCallable(new Callable<FullHouse>() {
            @Override
            public FullHouse call() throws Exception {
                return ProcessJson.getDetailInfoHouse(response);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<FullHouse>() {
                    @Override
                    public void onSuccess(FullHouse value) {
                        fullHouse = value;

                        if (fullHouse != null) {
                            Glide.with(HouseDetailActivity.this)
                                    .load(ApiConstant.URL_WEB_SERVICE_GET_IMAGE_HOUSE + fullHouse.getFirstImage())
                                    .crossFade(1000)
                                    .into(imvImage);

                            tvAddressToolbar.setText(String.format("%s %s, %s", fullHouse.getDetailAddress(), fullHouse.getStreet(), fullHouse.getWard()));
                            tvAddressCityToolbar.setText(String.format("%s, %s", fullHouse.getDistrict(), fullHouse.getCity()));

                            tvImages.setText(String.format("%s %s", fullHouse.getImages().size(), getString(R.string.photo)));
                            tvPrice.setText(String.format(Locale.getDefault(), "%,d %s", fullHouse.getPrice(), getString(R.string.currency)));
                            tvInfo.setText(String.format(Locale.getDefault(), "%d %s, %s m2", fullHouse.getNumberOfRoom(), getString(R.string.room), fullHouse.getArea()));
                            tvType.setText(fullHouse.getPropertyType());
                            tvIntro.setText(fullHouse.getDescription());

                            tvStatus.setText(fullHouse.getStatus());
                            tvArea.setText(String.format(Locale.getDefault(), "%,d m2", fullHouse.getArea()));
                            tvPriceInfo.setText(String.format("%s %s", fullHouse.getPrice(), getString(R.string.currency)));
                            tvPriceOverArea.setText(String.format(Locale.getDefault(), "%,d %s/m2", (fullHouse.getPrice() / fullHouse.getArea()), getString(R.string.currency)));
                            tvNumberOfRoom.setText(String.format(Locale.getDefault(), "%d %s", fullHouse.getNumberOfRoom(), getString(R.string.room)));
                            tvPropertyType.setText(fullHouse.getPropertyType());
//        tvAddedOn
                            tvAddressInfo.setText(fullHouse.getAddress());

                            etContentEmail.setText(String.format("%s %s", getString(R.string.iAmInterestingEmail), fullHouse.getAddress()));
                        }

                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);

                        layoutTotalInfo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();

                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);

                        layoutTotalInfo.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isLiked) {
            getMenuInflater().inflate(R.menu.menu_detail_house_activity_like, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_detail_house_activity_not_like, menu);
        }
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
        //// TODO: 10/1/2016 like house
    }

    private void onClickMenuShare() {
        Intent shareIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
        if (shareIntent != null) {
            shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.facebook.katana");
            shareIntent.setType("text/plain");

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Nha minh quan tam");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://stackoverflow.com/questions/7545254/android-and-facebook-share-intent");

            startActivity(shareIntent);
        }
    }

    @OnClick({R.id.layoutImage, R.id.tvImages})
    void onClickLayoutImages() {
        if (fullHouse != null) {
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putStringArrayListExtra(ApiConstant.IMAGE, fullHouse.getImages());
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnSend)
    void onClickBtnSend() {
        if (!etContentEmail.getText().toString().isEmpty()) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"abc@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
            i.putExtra(Intent.EXTRA_TEXT, etContentEmail.getText().toString());
            try {
                startActivity(Intent.createChooser(i, getString(R.string.selectAppToSend)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, R.string.noAppToSend, Toast.LENGTH_SHORT).show();
            }
        } else {
            etContentEmail.setError(getString(R.string.pleaseInputEmailContent));
        }
    }

    @OnClick(R.id.tvCallOwner)
    void onClickTvCallOwner() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:01234567899"));
        startActivity(intent);

    }

    @OnClick(R.id.tvCallAgent)
    void onClickTvCallAgent() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:01234567899"));
        startActivity(intent);

    }

    @OnClick(R.id.tvSeeMoreInfo)
    void onClickTvMoreInfo() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutInfo.getLayoutParams();
        if (params.height == LinearLayout.LayoutParams.WRAP_CONTENT) {
            params.height = AppConstant.HEIGHT_LAYOUT_INFO_COLLAPSED;
            tvSeeMoreInfo.setText(getString(R.string.seeMore));
        } else {
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            tvSeeMoreInfo.setText(getString(R.string.seeLess));
        }

        layoutInfo.requestLayout();
    }

    @OnClick(R.id.tvSeeMoreIntro)
    void onClickTvSeeMoreIntro() {
        if (tvIntro.getMaxLines() == AppConstant.MAX_LINE_COLLAPSE) {
            tvIntro.setMaxLines(AppConstant.MAX_LINE_EXPAND);
            tvSeeMoreIntro.setText(getString(R.string.seeLess));
        } else {
            tvIntro.setMaxLines(AppConstant.MAX_LINE_COLLAPSE);
            tvSeeMoreIntro.setText(getString(R.string.seeMore));
        }
    }

    @OnClick(R.id.tvDirection)
    void onClickTvDirection() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fullHouse.getLat() + "," + fullHouse.getLng() + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @OnClick(R.id.tvMapView)
    void onClickMapView() {
        openMapActivity(GoogleMap.MAP_TYPE_NORMAL);
    }

    @OnClick(R.id.tvEarthView)
    void onClickEarthView() {
        openMapActivity(GoogleMap.MAP_TYPE_HYBRID);
    }

    private void openMapActivity(int mapType) {
        Intent intent = new Intent(this, MapViewActivity.class);
        intent.putExtra(ApiConstant.STATUS, fullHouse.getStatus());
        intent.putExtra(ApiConstant.LATITUDE, Double.parseDouble(fullHouse.getLat()));
        intent.putExtra(ApiConstant.LONGITUDE, Double.parseDouble(fullHouse.getLng()));
        intent.putExtra(ApiConstant.PRICE, fullHouse.getPrice());
        intent.putExtra(ApiConstant.MAP_TYPE, mapType);
        startActivity(intent);
    }
}
