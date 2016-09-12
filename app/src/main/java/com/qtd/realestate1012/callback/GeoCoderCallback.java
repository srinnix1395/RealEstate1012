package com.qtd.realestate1012.callback;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DELL on 9/11/2016.
 */
public interface GeoCoderCallback {
    void onResult(LatLng latLng);
}
