package com.qtd.realestate1012.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.LocalInfoDialog;
import com.qtd.realestate1012.manager.MapManager;
import com.qtd.realestate1012.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/30/2016.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {

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

    View locationButton;

    private SupportMapFragment supportMapFragment;
    private MapManager mapManager;

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
        mapManager = new MapManager(view.getContext(), googleMap);
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

    @OnClick({R.id.tvLocalInfo, R.id.tvSaveSearch, R.id.fabLocation, R.id.fabEnableMarker})
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
                if (!ServiceUtils.isLocationServiceEnabled(view.getContext())) {
                    Toast.makeText(view.getContext(), R.string.pleaseEnableLocationService, Toast.LENGTH_SHORT).show();
                    break;
                }

                if (locationButton != null) {
                    locationButton.performClick();
                }
                break;
            }
        }
    }

    private void showDialogLocalInfo() {
        LocalInfoDialog localInfoDialog = new LocalInfoDialog(view.getContext(), mapManager.getMapType());
        localInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (((LocalInfoDialog) dialog).getSatelliteModesStatus() && mapManager.getMapType() != GoogleMap.MAP_TYPE_SATELLITE) {
                    mapManager.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    mapManager.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });
        localInfoDialog.show();
    }

    private void onClickFabEnableMarker() {
        if (fabEnableMarker.getDrawable().getLevel() == 1) {
            fabEnableMarker.getDrawable().setLevel(2);
            mapManager.clearMarker();
            return;
        }

        fabEnableMarker.getDrawable().setLevel(1);
        mapManager.drawMarker();
    }
}

