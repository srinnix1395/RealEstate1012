package com.qtd.realestate1012.asynctask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.qtd.realestate1012.callback.GeoCoderCallback;

import java.io.IOException;
import java.util.List;

/**
 * Created by DELL on 9/11/2016.
 */
public class GeoCoderGetLatLngAsyncTask extends AsyncTask<String, Void, LatLng> {
    private Context context;
    private GeoCoderCallback callback;

    public GeoCoderGetLatLngAsyncTask(Context context, GeoCoderCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected LatLng doInBackground(String... strings) {
        String address = strings[0];
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if (addressList.size() == 0) {
                return null;
            }
            Address address1 = addressList.get(0);
            return new LatLng(address1.getLatitude(), address1.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        callback.onResult(latLng);
    }
}
