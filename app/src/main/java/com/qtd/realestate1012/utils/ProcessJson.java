package com.qtd.realestate1012.utils;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.model.FullHouse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dell on 8/8/2016.
 */
public class ProcessJson {
    public static ArrayList<Object> getArrayListHousesNew(JSONArray jsonArray) {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("Nhà thường");
        BunchHouse bunchHouse = new BunchHouse();
        for (int i = 0, length = jsonArray.length(); i < length; i++) {
            try {
                JSONObject house = jsonArray.getJSONObject(i);

                String id = house.getString(ApiConstant._ID);
                int price = house.getInt(ApiConstant.PRICE);
                String firstImage = house.getJSONArray(ApiConstant.IMAGE).getString(0);
                String detailAddress= house.getString(ApiConstant.DETAIL_ADDRESS);
                String street = house.getString(ApiConstant.STREET);
                String ward = house.getString(ApiConstant.WARD);
                String district = house.getString(ApiConstant.DISTRICT);
                String city = house.getString(ApiConstant.CITY);

                bunchHouse.addHouse(new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        bunchHouse.setType("Nhà thường");
        arrayList.add(bunchHouse);
        arrayList.add("Nhà thường");
        arrayList.add(bunchHouse);
        return arrayList;
    }

    public static ArrayList<Board> getFavoriteBoards(JSONObject response) {
        ArrayList<Board> arrayList = new ArrayList<>();
        try {
            if (response.getBoolean(ApiConstant.HAS_BOARD)) {
                JSONArray jsonArray = response.getJSONArray(ApiConstant.LIST_BOARD);
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id = jsonObject.getString(ApiConstant._ID);
                    String name = jsonObject.getString(ApiConstant.NAME);
                    int numberHouse = jsonObject.getJSONArray(ApiConstant.LIST_HOUSE).length();
                    String image = jsonObject.has(ApiConstant.FIRST_IMAGE) ? jsonObject.getString(ApiConstant.FIRST_IMAGE) : "";

                    arrayList.add(new Board(id, name, numberHouse, image));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static JSONObject processDataToServer(JSONObject response) {
        JSONObject object = new JSONObject();
        try {
            object.put(ApiConstant._ID_SOCIAL, response.getInt("id"));
            object.put(ApiConstant.EMAIL, response.getString("email"));
            object.put(ApiConstant.NAME, response.getString("first_name") + " " + object.getString("last_name"));
            object.put(ApiConstant.AVATAR, response.getString("picture"));
            object.put(ApiConstant.PROVIDER, ApiConstant.FACEBOOK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static ArrayList<CompactHouse> getFavoriteHouses(JSONObject response) {
        ArrayList<CompactHouse> arrayList = new ArrayList<>();

        try {
            JSONArray array = response.getJSONArray(ApiConstant.LIST_HOUSE);

            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject house = array.getJSONObject(i);

                String id = house.getString(ApiConstant._ID);
                int price = house.getInt(ApiConstant.PRICE);
                String firstImage = house.getJSONArray(ApiConstant.IMAGE).getString(0);
                String detailAddress= house.getString(ApiConstant.DETAIL_ADDRESS);
                String street = house.getString(ApiConstant.STREET);
                String ward = house.getString(ApiConstant.WARD);
                String district = house.getString(ApiConstant.DISTRICT);
                String city = house.getString(ApiConstant.CITY);

                arrayList.add(new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static FullHouse getDetailInfoHouse(JSONObject response) {
        try {
            String id = response.getString(ApiConstant._ID);
            String address = response.getString(ApiConstant.ADDRESS);
            String city = response.getString(ApiConstant.CITY);
            String description = response.getString(ApiConstant.DESCRIPTION);
            String district = response.getString(ApiConstant.DISTRICT);
            String lat = response.getString(ApiConstant.LATITUDE);
            String lng = response.getString(ApiConstant.LONGITUDE);
            int numberOfRoom = response.getInt(ApiConstant.NUMBER_OF_ROOMS);
            int price = response.getInt(ApiConstant.PRICE);
            String propertyType = response.getString(ApiConstant.PROPERTY_TYPE);
            String status = response.getString(ApiConstant.STATUS);
            String street = response.getString(ApiConstant.STREET);
            String type = response.getString(ApiConstant.TYPE);
            String ward = response.getString(ApiConstant.WARD);
            int area = response.getInt(ApiConstant.AREA);
            String detailAdd = response.getString(ApiConstant.DETAIL_ADDRESS);

            ArrayList<String> images = new ArrayList<>();
            JSONArray arrayImage = response.getJSONArray(ApiConstant.IMAGE);
            for (int i = 0, size = arrayImage.length(); i < size; i++) {
                images.add(arrayImage.getString(i));
            }

            return new FullHouse(id, price, detailAdd, street, ward, district, city, area, numberOfRoom, status, propertyType,
                    description, lat, lng, address, "hello", "0123", images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
