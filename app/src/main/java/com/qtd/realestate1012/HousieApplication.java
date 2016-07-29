package com.qtd.realestate1012;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Dell on 7/28/2016.
 */
public class HousieApplication extends Application {

    private static final String TAG = "HousieApplication";

    private RequestQueue queue;
    private static HousieApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized HousieApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        return queue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request, Object tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public <T> void cancelPendingRequest(Object tag) {
        if (queue != null) {
            queue.cancelAll(tag);
        }
    }


}
