package com.qtd.realestate1012.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.manager.PlaceManager;

/**
 * Created by Dell on 8/4/2016.
 */
public class LocalInfoAsyncTask extends AsyncTask<String, Void, String> {
    private Handler handler;
    private String type;

    public LocalInfoAsyncTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String latitude = params[1];
        String longitude = params[2];
        String jsonResult = null;

        try {
            switch (type) {
                case ApiConstant.API_PLACE_TYPE_SCHOOL: {
                    jsonResult = PlaceManager.getPlace(ApiConstant.API_PLACE_TYPE_SCHOOL, latitude, longitude);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {

            return;
        }

        Message message = new Message();
        message.what = AppConstant.WHAT_LOCAL_INFO_ASYNC_TASK;

        Bundle data = new Bundle();
        data.putString(ApiConstant.API_PLACE_DATA, result);
        data.putString(ApiConstant.API_PLACE_KEY_TYPE, type);

        message.setData(data);
        handler.sendMessage(message);
    }
}
