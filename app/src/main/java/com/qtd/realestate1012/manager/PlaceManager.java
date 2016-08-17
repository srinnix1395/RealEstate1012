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
    public static JSONArray getPlace(String type, String latitude, String longitude) throws IOException {
        JSONArray jsonArray = new JSONArray();
        String jsonResult;

        StringBuilder googlePlaceURL = new StringBuilder(ApiConstant.API_PLACE_URL);
        googlePlaceURL.append("location=" + latitude + "," + longitude);
        googlePlaceURL.append("&radius=" + ApiConstant.DEFAULT_RADIUS);
        googlePlaceURL.append("&types=" + type);
        googlePlaceURL.append("&sensor=true");
        googlePlaceURL.append("&key=" + ApiConstant.API_KEY);

        InputStream inputStream;
        HttpURLConnection connection;

        try {
            URL url = new URL(googlePlaceURL.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            inputStream.close();
            connection.disconnect();

            jsonResult = builder.toString();

            JSONObject jsonObject = new JSONObject(jsonResult);
            jsonArray.put(jsonObject);

            while (jsonObject.has(ApiConstant.NEXT_PAGE_TOKEN)) {
                String urlNextPage = googlePlaceURL.toString() + "&pageToken=" + jsonObject.getString(ApiConstant.NEXT_PAGE_TOKEN);

                url = new URL(urlNextPage);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                inputStream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                inputStream.close();
                connection.disconnect();

                jsonResult = builder.toString();

                jsonObject = new JSONObject(jsonResult);
                jsonArray.put(jsonObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static ArrayList<Place> getLocationNearBy(String json) {
        ArrayList<Place> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int j = 0, length = jsonArray.length(); j < length; j++) {
                JSONArray arrayResult = jsonArray.getJSONObject(j).getJSONArray("results");
                for (int i = 0, size = arrayResult.length(); i < size; i++) {
                    JSONObject place = arrayResult.getJSONObject(i);
                    JSONObject location = place.getJSONObject(ApiConstant.API_PLACE_GEOMETRY).getJSONObject(ApiConstant.API_PLACE_LOCATION);

                    String placeID = place.getString(ApiConstant.API_PLACE_PLACE_ID);
                    String name = place.getString(ApiConstant.NAME);
                    String address = place.getString(ApiConstant.VICINITY);
                    double latitude = location.getDouble(ApiConstant.API_PLACE_LATITUDE);
                    double longitude = location.getDouble(ApiConstant.API_PLACE_LONGITUDE);
                    arrayList.add(new Place(placeID, name, address, latitude, longitude));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
