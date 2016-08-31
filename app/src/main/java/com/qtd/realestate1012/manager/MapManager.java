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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.MainActivity;
import com.qtd.realestate1012.asynctask.LocalInfoAsyncTask;
import com.qtd.realestate1012.callback.SearchFragmentCallback;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.custom.MarkerInfoHouse;
import com.qtd.realestate1012.messageevent.MessageEventClickHouseMarker;
import com.qtd.realestate1012.model.Place;
import com.qtd.realestate1012.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dell on 7/31/2016.
 */
public class MapManager implements GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener {
    private Context context;
    private SearchFragmentCallback callback;
    private GoogleMap map;
    private boolean hasDisplayedLocalPlace;
    private String placeType;

    private ArrayList<Marker> arrayLocationNearByMarker;
    private ArrayList<Marker> arrayHousesMarker;

    public MapManager(Context context, GoogleMap map, SearchFragmentCallback callback) {
        this.context = context;
        this.map = map;
        this.callback = callback;
        arrayLocationNearByMarker = new ArrayList<>();
        arrayHousesMarker = new ArrayList<>();
        initMap();
    }

    private void initMap() {
        LatLng latLngHaNoi = new LatLng(AppConstant.LATITUDE_HANOI, AppConstant.LONGITUDE_HANOI);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngHaNoi, 14));

        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnCameraChangeListener(this);
        map.setOnMarkerClickListener(this);
        map.setTrafficEnabled(true);

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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (hasDisplayedLocalPlace) {
            clearLocationNearByMarker();
            requestLocationNearbyPlace(placeType);
        }
    }

    public void setEnabledMyLocation() {
        map.setMyLocationEnabled(true);
    }


    public void displayHousesMarker(JSONArray jsonArrayHouse) {
        for (int i = 0, size = jsonArrayHouse.length(); i < size; i++) {
            try {
                arrayHousesMarker.add(drawHouseMarker(jsonArrayHouse.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Marker drawHouseMarker(JSONObject house) throws JSONException {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(ApiConstant.HOUSE);
        markerOptions.snippet(house.toString());
        markerOptions.position(new LatLng(house.getDouble(ApiConstant.LATITUDE), house.getDouble(ApiConstant.LONGITUDE)));

        MarkerInfoHouse view = new MarkerInfoHouse(context, house.getInt(ApiConstant.PRICE));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(UiUtils.getBitmapFromView(view)));
        return map.addMarker(markerOptions);
    }

    public void setVisibleHousesMarker(boolean value) {
        for (Marker marker : arrayHousesMarker) {
            marker.setVisible(value);
        }
    }

    public void requestLocationNearbyPlace(String placeType) {
        hasDisplayedLocalPlace = true;
        this.placeType = placeType;
        callback.onEnableProgressBar();
        new LocalInfoAsyncTask().execute(placeType, String.valueOf(map.getCameraPosition().target.latitude),
                String.valueOf(map.getCameraPosition().target.longitude));
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
            arrayLocationNearByMarker.add(drawLocationNearByMarker(place, markerType));
        }
    }

    private Marker drawLocationNearByMarker(Place place, int icon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(place.getName());
        markerOptions.snippet(place.getAddress());
        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(UiUtils.getBitmapFromDrawable(context, icon)));
        return map.addMarker(markerOptions);
    }

    public void clearLocationNearByMarker() {
        for (Marker marker : arrayLocationNearByMarker) {
            marker.remove();
        }
        arrayLocationNearByMarker.clear();
        hasDisplayedLocalPlace = false;
    }

    public int getMapType() {
        return map.getMapType();
    }

    public void setMapType(int mapTypeSatellite) {
        map.setMapType(mapTypeSatellite);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals(ApiConstant.HOUSE)) {
            EventBus.getDefault().post(new MessageEventClickHouseMarker(marker.getSnippet()));
            return false;
        }
        return false;
    }
}
