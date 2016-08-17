package com.qtd.realestate1012.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.manager.PlaceManager;

import org.json.JSONArray;

/**
 * Created by Dell on 8/4/2016.
 */
public class LocalInfoAsyncTask extends AsyncTask<String, Void, JSONArray> {
    private Handler handler;
    private String type;

    public LocalInfoAsyncTask(Handler handler) {
        this.handler = handler;
    }

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
    protected void onPostExecute(JSONArray result) {
        Message message = new Message();
        message.what = AppConstant.WHAT_LOCAL_INFO_ASYNC_TASK;
        Bundle data = new Bundle();

        if (result == null) {
            data.putString(ApiConstant.RESULT, ApiConstant.FAILED);
            return;
        } else {
            data.putString(ApiConstant.RESULT, ApiConstant.SUCCESS);
            data.putString(ApiConstant.API_PLACE_DATA, result.toString());
            data.putString(ApiConstant.API_PLACE_KEY_TYPE, type);
        }

        message.setData(data);
        handler.sendMessage(message);
    }
}
