package com.qtd.realestate1012.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.custom.MarkerInfoHouse;
import com.qtd.realestate1012.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 8/25/2016.
 */
public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LatLng latLng;
    private Marker myPosition;
    private int price;
    private int mapType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();

        if (intent != null) {
            latLng = new LatLng(intent.getDoubleExtra(ApiConstant.LATITUDE, 0), intent.getDoubleExtra(ApiConstant.LONGITUDE, 0));
            price = intent.getIntExtra(ApiConstant.PRICE, 0);
            mapType = intent.getIntExtra(ApiConstant.MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.mapnNearBy);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
    }

    private void setupMap(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(mapType);
        if (latLng != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        View view = new MarkerInfoHouse(this, price);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(UiUtils.getBitmapFromView(view)));

        myPosition = map.addMarker(markerOptions);
    }


}
