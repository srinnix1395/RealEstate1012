package com.qtd.realestate1012.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

/**
 * Created by Dell on 7/31/2016.
 */
public class ServiceUtils {
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return (mConnectivityManager.getActiveNetworkInfo() != null
                    && mConnectivityManager.getActiveNetworkInfo().isAvailable()
                    && mConnectivityManager.getActiveNetworkInfo().isConnected());
        }
        return false;
    }

    public static boolean isLocationServiceEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false, network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            //do nothing...
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            //do nothing...
        }

        return gps_enabled || network_enabled;
    }
}
