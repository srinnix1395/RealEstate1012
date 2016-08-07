package com.qtd.realestate1012.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.MainActivity;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.Place;
import com.qtd.realestate1012.utils.ImageUtils;

import java.util.ArrayList;

/**
 * Created by Dell on 7/31/2016.
 */
public class MapManager {
    private Context context;
    private GoogleMap map;
    private ArrayList<Marker> arrayMarkerPlace;

    public MapManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        arrayMarkerPlace = new ArrayList<>();
        initMap();
    }

    private void initMap() {
        LatLng latLngHaNoi = new LatLng(AppConstant.LATITUDE_HANOI, AppConstant.LONGITUDE_HANOI);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngHaNoi, 13));

        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public void clearMarker() {

    }

    public void drawRealEstateMarker() {

    }

    public void showLocationNearByMarker(String type, ArrayList<Place> arrayLocationNearby) {
        int markerType = 0;
        switch (type) {
            case ApiConstant.API_PLACE_TYPE_SCHOOL: {
                markerType = R.drawable.drawable_marker_school;
                break;
            }
            case ApiConstant.API_PLACE_TYPE_HOSPITAL: {

                break;
            }
        }
        for (Place place : arrayLocationNearby) {
            arrayMarkerPlace.add(drawPlaceMarker(place.getLatitude(), place.getLongitude(), markerType));
        }
    }

    private Marker drawPlaceMarker(double lat, double lng, int icon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("school");
        markerOptions.snippet("school");
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromDrawable(context, icon)));
        return map.addMarker(markerOptions);
    }

    public void clearMarkerPlace() {
        for (Marker marker : arrayMarkerPlace) {
            marker.remove();
        }
        arrayMarkerPlace.clear();
    }

    public int getMapType() {
        return map.getMapType();
    }

    public void setMapType(int mapTypeSatellite) {
        map.setMapType(mapTypeSatellite);
    }

    public LatLng getLatLng() {
        return map.getCameraPosition().target;
    }
}
