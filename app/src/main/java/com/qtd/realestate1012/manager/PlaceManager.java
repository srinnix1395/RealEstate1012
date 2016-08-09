package com.qtd.realestate1012.manager;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dell on 8/3/2016.
 */
public class PlaceManager {
    public static String getPlace(String type, String latitude, String longitude) throws IOException {
        String jsonResult = null;

        StringBuilder googlePlaceURL = new StringBuilder(ApiConstant.API_PLACE_URL);
        googlePlaceURL.append("location=" + latitude + "," + longitude);
        googlePlaceURL.append("&radius=" + ApiConstant.DEFAULT_RADIUS);
        googlePlaceURL.append("&types=" + ApiConstant.API_PLACE_TYPE_SCHOOL);
        googlePlaceURL.append("&sensor=true");
        googlePlaceURL.append("&key=" + ApiConstant.API_KEY);

        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(googlePlaceURL.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            jsonResult = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return jsonResult;
    }

    public static ArrayList<Place> getLocationNearBy(String json) {
        ArrayList<Place> arrayList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            if (!object.getString(ApiConstant.API_PLACE_STATUS).equals(ApiConstant.API_PLACE_STATUS_SUCCESS)) {
                return null;
            }
            JSONArray array = object.getJSONArray(ApiConstant.API_PLACE_KEY_RESULTS);
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject place = array.getJSONObject(i);
                JSONObject location = place.getJSONObject(ApiConstant.API_PLACE_GEOMETRY).getJSONObject(ApiConstant.API_PLACE_LOCATION);
                double latitude = location.getDouble(ApiConstant.API_PLACE_LATITUDE);
                double longitude = location.getDouble(ApiConstant.API_PLACE_LONGITUDE);
                String placeID = place.getString(ApiConstant.API_PLACE_PLACE_ID);
                arrayList.add(new Place(placeID, latitude, longitude));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
