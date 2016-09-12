package com.qtd.realestate1012.asynctask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.qtd.realestate1012.callback.SearchFragmentCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by DELL on 9/2/2016.
 */
public class GeoCoderGetAddressAsyncTask extends AsyncTask<LatLng, Void, String> {
    private Context context;
    private SearchFragmentCallback callback;

    public GeoCoderGetAddressAsyncTask(Context context, SearchFragmentCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(LatLng... latLngs) {
        LatLng target = latLngs[0];
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(target.latitude, target.longitude, 1);
            return list.get(0).getAddressLine(2) + ", " + list.get(0).getAddressLine(3);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        callback.resetAddressEtSearch(s);
    }
}
