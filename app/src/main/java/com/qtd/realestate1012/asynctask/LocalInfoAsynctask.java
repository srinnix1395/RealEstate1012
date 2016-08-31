package com.qtd.realestate1012.asynctask;

import android.os.AsyncTask;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.manager.PlaceManager;
import com.qtd.realestate1012.messageevent.MessageDataLocationNearBy;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

/**
 * Created by Dell on 8/4/2016.
 */
public class LocalInfoAsyncTask extends AsyncTask<String, Void, JSONArray> {
    private String type;

    @Override
    protected JSONArray doInBackground(String... params) {
        type = params[0];
        String latitude = params[1];
        String longitude = params[2];
        JSONArray jsonArray = null;

        try {
            switch (type) {
                case ApiConstant.API_PLACE_TYPE_SCHOOL: {
                    jsonArray = PlaceManager.getPlace(ApiConstant.API_PLACE_TYPE_SCHOOL, latitude, longitude);
                    break;
                }
                case ApiConstant.API_PLACE_TYPE_HOSPITAL: {
                    jsonArray = PlaceManager.getPlace(ApiConstant.API_PLACE_TYPE_HOSPITAL, latitude, longitude);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        String result;

        if (jsonArray == null) {
            result = ApiConstant.FAILED;
        } else {
            result = ApiConstant.SUCCESS;
        }

        EventBus.getDefault().post(new MessageDataLocationNearBy(result, jsonArray, type));
    }
}
