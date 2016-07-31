package com.qtd.realestate1012.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.qtd.realestate1012.activity.MainActivity;
import com.qtd.realestate1012.constant.AppConstant;

/**
 * Created by Dell on 7/31/2016.
 */
public class MapManager implements GoogleMap.OnMyLocationChangeListener {
    private Context context;
    private GoogleMap map;

    public MapManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        initMap();
    }

    private void initMap() {
//        change location button's position to right bottom
//        if (mapView != null && mapView.findViewById(1) != null) {
//            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
//                    locationButton.getLayoutParams();
//            // position on right bottom
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            layoutParams.setMargins(0, 0, 30, 30);
//        }

        LatLng latLngHaNoi = new LatLng(AppConstant.LATITUDE_HANOI, AppConstant.LONGITUDE_HANOI);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngHaNoi, 13));

        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMyLocationChangeListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(((MainActivity) context), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstant.REQUEST_CODE_LOCATION_PERMISSION);
            } else {
                map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);
        }
    }

    public void setEnabledMyLocation() {
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    public void clearMarker() {

    }

    public void drawMarker() {

    }
}
