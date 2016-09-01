package com.qtd.realestate1012.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.FilterActivity;
import com.qtd.realestate1012.callback.SearchFragmentCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.BottomSheetHouse;
import com.qtd.realestate1012.custom.LocalInfoDialog;
import com.qtd.realestate1012.manager.MapManager;
import com.qtd.realestate1012.manager.PlaceManager;
import com.qtd.realestate1012.messageevent.MessageDataLocationNearBy;
import com.qtd.realestate1012.messageevent.MessageEventClickHouseMarker;
import com.qtd.realestate1012.model.Place;
import com.qtd.realestate1012.utils.ServiceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/30/2016.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback, SearchFragmentCallback {

    private static final String MAP_RESULT = "Bản đồ";
    private static final String LIST_RESULT = "Danh sách";

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.tvFilter)
    TextView tvFilter;

    @BindView(R.id.tvResult)
    TextView tvListResult;

    @BindView(R.id.fabLocation)
    FloatingActionButton fabLocation;

    @BindView(R.id.fabEnableMarker)
    FloatingActionButton fabEnableMarker;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.layoutLocalInfo)
    LinearLayout layoutLocalInfo;

    @BindView(R.id.tvInfoType)
    TextView tvInfoType;

    @BindView(R.id.toolbar_1)
    LinearLayout layoutSearch;

    private View locationButton;
    private SupportMapFragment supportMapFragment;
    private MapManager mapManager;
    private String jsonBoard;
    private ListHouseFragment listHouseFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            jsonBoard = arguments.getString(ApiConstant.LIST_BOARD);
        }
    }

    private void requestData() {
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString(ApiConstant.RESULT)) {
                        case ApiConstant.FAILED: {
                            Toast.makeText(getContext(), R.string.errorProcessing, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case ApiConstant.SUCCESS: {
                            handleResponseSuccess(response);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        HousieApplication.getInstance().addToRequestQueue(request);
    }

    private void handleResponseSuccess(JSONObject response) throws JSONException {
        mapManager.displayHousesMarker(response.getJSONArray(ApiConstant.LIST_HOUSE));
    }

    private void initView() {
        fabEnableMarker.getDrawable().setLevel(1);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(true);

        supportMapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.layoutSearch, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager = new MapManager(getContext(), googleMap, this);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

        //hide google map's location button
        if (supportMapFragment.getView() != null && supportMapFragment.getView().findViewById(Integer.parseInt("1")) != null) {
            locationButton = ((View) supportMapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            locationButton.setVisibility(View.INVISIBLE);
        }
        requestData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mapManager.setEnabledMyLocation();
        }
    }

    @OnClick({R.id.tvLocalInfo, R.id.tvSaveSearch, R.id.fabLocation, R.id.fabEnableMarker, R.id.imvClose
            , R.id.tvFilter, R.id.tvResult})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocalInfo: {
                showDialogLocalInfo();
                break;
            }
            case R.id.tvSaveSearch: {

                break;
            }
            case R.id.fabEnableMarker: {
                onClickFabEnableMarker();
                break;
            }
            case R.id.fabLocation: {
                onClickFabLocation();
                break;
            }
            case R.id.imvClose: {
                onClickImvCloseLayoutLocalInfo();
                break;
            }
            case R.id.tvFilter: {
                onClickTvFilter();
                break;
            }
            case R.id.tvResult: {
                onClickTvResult();
                break;
            }
        }
    }

    private void onClickTvResult() {
        switch (tvListResult.getText().toString()) {
            case LIST_RESULT: {
                listHouseFragment = new ListHouseFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ApiConstant.URL_WEB_SERVICE, ApiConstant.URL_WEB_SERVICE_GET_ALL_HOUSE);
                bundle.putString(ApiConstant.LIST_BOARD, jsonBoard);
                listHouseFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(supportMapFragment);
                transaction.add(R.id.layoutSearch, listHouseFragment);
                transaction.commit();

                fabLocation.setVisibility(View.GONE);
                fabEnableMarker.setVisibility(View.GONE);

                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) layoutSearch.getLayoutParams();
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                layoutSearch.requestLayout();

                tvListResult.setText(MAP_RESULT);
                break;
            }
            case MAP_RESULT: {
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) layoutSearch.getLayoutParams();
                params.setScrollFlags(0);
                layoutSearch.requestLayout();

                fabLocation.setVisibility(View.VISIBLE);
                fabEnableMarker.setVisibility(View.VISIBLE);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(listHouseFragment);
                listHouseFragment.onDestroyView();
                transaction.show(supportMapFragment);
                transaction.commit();

                tvListResult.setText(LIST_RESULT);
                break;
            }
        }
    }

    private void onClickTvFilter() {
        Intent intent = new Intent(getActivity(), FilterActivity.class);
        startActivityForResult(intent, AppConstant.REQUEST_CODE_FILTER_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.REQUEST_CODE_FILTER_ACTIVITY && resultCode == Activity.RESULT_OK && data != null) {
            //// TODO: 8/21/2016 filter
        }
    }

    private void onClickImvCloseLayoutLocalInfo() {
        layoutLocalInfo.setVisibility(View.INVISIBLE);
        mapManager.clearLocationNearByMarker();
    }

    private void onClickFabLocation() {
        if (!ServiceUtils.isLocationServiceEnabled(getContext())) {
            Toast.makeText(getContext(), R.string.pleaseEnableLocationService, Toast.LENGTH_SHORT).show();
            return;
        }

        if (locationButton != null) {
            locationButton.performClick();
        }
    }

    private void showDialogLocalInfo() {
        String placeType = layoutLocalInfo.getVisibility() == View.INVISIBLE ? "" : tvInfoType.getText().toString();
        LocalInfoDialog localInfoDialog = new LocalInfoDialog(getContext(), mapManager.getMapType(), placeType);
        localInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //change map type
                if (((LocalInfoDialog) dialog).getMapType() != mapManager.getMapType()) {
                    mapManager.setMapType(((LocalInfoDialog) dialog).getMapType());
                    return;
                }

                //show local info
                switch (((LocalInfoDialog) dialog).getRadioButtonIdChecked()) {
                    case R.id.radioSchools: {
                        mapManager.requestLocationNearbyPlace(ApiConstant.API_PLACE_TYPE_SCHOOL);
                        break;
                    }
                    case R.id.radioHospital: {
                        mapManager.requestLocationNearbyPlace(ApiConstant.API_PLACE_TYPE_HOSPITAL);
                        break;
                    }
                }
            }
        });
        localInfoDialog.show();

        //change size local dialog info
        int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.90);
        localInfoDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void onClickFabEnableMarker() {
        if (fabEnableMarker.getDrawable().getLevel() == 1) {
            fabEnableMarker.getDrawable().setLevel(2);
            mapManager.setVisibleHousesMarker(false);
            return;
        }

        fabEnableMarker.getDrawable().setLevel(1);
        mapManager.setVisibleHousesMarker(true);
    }

    @Override
    public void onEnableProgressBar() {
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void handleRequestLocationNearBy(MessageDataLocationNearBy message) {
        if (message.result != null) {
            switch (message.result) {
                case ApiConstant.SUCCESS: {
                    ArrayList<Place> arrayLocationNearby = PlaceManager.getLocationNearBy(message.data);
                    mapManager.showLocationNearByMarker(message.placeType, arrayLocationNearby);
                    tvInfoType.setText(message.placeType);
                    layoutLocalInfo.setVisibility(View.VISIBLE);
                    break;
                }
                case ApiConstant.FAILED: {
                    Toast.makeText(getContext(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    public void handleEventClickMarker(MessageEventClickHouseMarker message) {
        BottomSheetHouse dialog = new BottomSheetHouse();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant.HOUSE, message.houseInfo);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "dialog");
    }
}

