package com.qtd.realestate1012.asynctask;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 9/29/2016.
 */
public class UpdateFirebaseRegId {
    public static void updateRegId(String id, String token) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ApiConstant._ID, id);
            jsonRequest.put(ApiConstant.REG_ID, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.POST, ApiConstant.URL_WEB_SERVICE_UPDATE_REG_ID, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getString(ApiConstant.RESULT)) {
                                case ApiConstant.FAILED: {
                                    Log.i(ApiConstant.HOUSIE, "update firebase reg_id: Can't not update regId");
                                    break;
                                }
                                case ApiConstant.SUCCESS: {
                                    Log.i(ApiConstant.HOUSIE, "update firebase reg_id: update reg id succesfully");
                                    HousieApplication.getInstance().getSharedPreUtils().putBoolean(AppConstant.DEVICE_TOKEN, true);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        HousieApplication.getInstance().addToRequestQueue(request);
    }
}
