package com.qtd.realestate1012.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.callback.SearchFragmentCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.LocalInfoDialog;
import com.qtd.realestate1012.manager.MapManager;
import com.qtd.realestate1012.manager.PlaceManager;
import com.qtd.realestate1012.model.Place;
import com.qtd.realestate1012.utils.ServiceUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/30/2016.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback, SearchFragmentCallback {

    private View view;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.tvFilter)
    TextView tvFilter;

    @BindView(R.id.tvListResult)
    TextView tvListResult;

    @BindView(R.id.fabEnableMarker)
    FloatingActionButton fabEnableMarker;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.layoutLocalInfo)
    LinearLayout layoutLocalInfo;

    @BindView(R.id.tvInfoType)
    TextView tvInfoType;

    private View locationButton;

    private SupportMapFragment supportMapFragment;
    private MapManager mapManager;
    private Handler handlerLocalInfo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == AppConstant.WHAT_LOCAL_INFO_ASYNC_TASK) {
//
//            }

            Bundle bundle = msg.getData();
            switch (bundle.getString(ApiConstant.RESULT)) {
                case ApiConstant.SUCCESS: {
                    ArrayList<Place> arrayLocationNearby = PlaceManager.getLocationNearBy(bundle.getString(ApiConstant.API_PLACE_DATA));
                    mapManager.showLocationNearByMarker(bundle.getString(ApiConstant.API_PLACE_KEY_TYPE), arrayLocationNearby);
                    tvInfoType.setText(bundle.getString(ApiConstant.API_PLACE_KEY_TYPE));
                    layoutLocalInfo.setVisibility(View.VISIBLE);
                    break;
                }
                case ApiConstant.FAILED: {
                    Toast.makeText(view.getContext(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        fabEnableMarker.getDrawable().setLevel(1);

        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        progressBar.setEnabled(true);

        supportMapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.layoutSearch, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager = new MapManager(view.getContext(), googleMap, handlerLocalInfo, this);
        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

        //hide google map's location button
        if (supportMapFragment.getView() != null && supportMapFragment.getView().findViewById(Integer.parseInt("1")) != null) {
            locationButton = ((View) supportMapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            locationButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mapManager.setEnabledMyLocation();
        }
    }

    @OnClick({R.id.tvLocalInfo, R.id.tvSaveSearch, R.id.fabLocation, R.id.fabEnableMarker, R.id.imvClose})
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
        }
    }

    private void onClickImvCloseLayoutLocalInfo() {
        layoutLocalInfo.setVisibility(View.INVISIBLE);
        mapManager.clearMarkerPlace();
    }

    private void onClickFabLocation() {
        if (!ServiceUtils.isLocationServiceEnabled(view.getContext())) {
            Toast.makeText(view.getContext(), R.string.pleaseEnableLocationService, Toast.LENGTH_SHORT).show();
            return;
        }

        if (locationButton != null) {
            locationButton.performClick();
        }
    }

    private void showDialogLocalInfo() {
        String placeType = layoutLocalInfo.getVisibility() == View.INVISIBLE ? "" : tvInfoType.getText().toString();
        LocalInfoDialog localInfoDialog = new LocalInfoDialog(view.getContext(), mapManager.getMapType(), placeType);
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
                        mapManager.showMarkerPlace(ApiConstant.API_PLACE_TYPE_SCHOOL);
                        break;
                    }
                    case R.id.radioHospital: {
                        mapManager.showMarkerPlace(ApiConstant.API_PLACE_TYPE_HOSPITAL);
                        break;
                    }
                }
            }
        });
        localInfoDialog.show();

        //change size local dialog info
        int width = (int) (view.getContext().getResources().getDisplayMetrics().widthPixels * 0.90);
        localInfoDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void onClickFabEnableMarker() {
        if (fabEnableMarker.getDrawable().getLevel() == 1) {
            fabEnableMarker.getDrawable().setLevel(2);
            mapManager.clearMarker();
            return;
        }

        fabEnableMarker.getDrawable().setLevel(1);
        mapManager.drawRealEstateMarker();
    }

    @Override
    public void onEnableProgressBar() {
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
    }
}

