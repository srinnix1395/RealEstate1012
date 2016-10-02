package com.qtd.realestate1012.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.asynctask.UpdateFirebaseRegId;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.utils.ServiceUtils;

/**
 * Created by DELL on 9/29/2016.
 */
public class FcmHousieInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(ApiConstant.HOUSIE, "onTokenRefresh: " + token);

        HousieApplication.getInstance().getSharedPreUtils().putBoolean(AppConstant.DEVICE_TOKEN, false);

        boolean isUserLoggedIn = HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false);

        if (isUserLoggedIn && ServiceUtils.isNetworkAvailable(this)) {
            String id = HousieApplication.getInstance().getSharedPreUtils().getString(ApiConstant._ID, "");
            UpdateFirebaseRegId.updateRegId(id, token);
        }
    }
}
