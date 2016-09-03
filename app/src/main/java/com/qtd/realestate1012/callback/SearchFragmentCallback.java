package com.qtd.realestate1012.callback;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DELL on 8/17/2016.
 */
public interface SearchFragmentCallback {
    void onEnableProgressBar();

    void resetAddressEtSearch(String address);

    void requestData(LatLng target);
}
