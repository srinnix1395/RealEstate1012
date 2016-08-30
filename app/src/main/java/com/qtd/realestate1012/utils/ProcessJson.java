package com.qtd.realestate1012.utils;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
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
    public static ArrayList<Object> getArrayListHousesNew(ArrayList<Board> arrayListBoard, JSONArray jsonArray) {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("Nhà thường");
        BunchHouse bunchHouse = new BunchHouse();
        for (int i = 0, length = jsonArray.length(); i < length; i++) {
            try {
                JSONObject house = jsonArray.getJSONObject(i);

                String id = house.getString(ApiConstant._ID);
                int price = house.getInt(ApiConstant.PRICE);
                String firstImage = house.getJSONArray(ApiConstant.IMAGE).getString(0);
                String detailAddress = house.getString(ApiConstant.DETAIL_ADDRESS);
                String street = house.getString(ApiConstant.STREET);
                String ward = house.getString(ApiConstant.WARD);
                String district = house.getString(ApiConstant.DISTRICT);
                String city = house.getString(ApiConstant.CITY);

                bunchHouse.addHouse(new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage, false));

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
                    String image = jsonObject.has(ApiConstant.FIRST_IMAGE) ? jsonObject.getString(ApiConstant.FIRST_IMAGE) : "";

                    JSONArray listHouse = jsonObject.getJSONArray(ApiConstant.LIST_HOUSE);
                    ArrayList<String> list = new ArrayList<>();
                    for (int j = 0, length = listHouse.length(); j < length; j++) {
                        list.add(listHouse.getString(j));
                    }

                    arrayList.add(new Board(id, name, list, image));
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
                String detailAddress = house.getString(ApiConstant.DETAIL_ADDRESS);
                String street = house.getString(ApiConstant.STREET);
                String ward = house.getString(ApiConstant.WARD);
                String district = house.getString(ApiConstant.DISTRICT);
                String city = house.getString(ApiConstant.CITY);

                arrayList.add(new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage, false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static FullHouse getDetailInfoHouse(JSONObject response) {
        try {
            JSONObject house = response.getJSONObject(ApiConstant.HOUSE);
            String id = house.getString(ApiConstant._ID);
            String address = house.getString(ApiConstant.ADDRESS);
            String city = house.getString(ApiConstant.CITY);
            String description = house.getString(ApiConstant.DESCRIPTION);
            String district = house.getString(ApiConstant.DISTRICT);
            String lat = house.getString(ApiConstant.LATITUDE);
            String lng = house.getString(ApiConstant.LONGITUDE);
            int numberOfRoom = house.getInt(ApiConstant.NUMBER_OF_ROOMS);
            int price = house.getInt(ApiConstant.PRICE);
            String propertyType = house.getString(ApiConstant.PROPERTY_TYPE);
            String status = house.getString(ApiConstant.STATUS);
            String street = house.getString(ApiConstant.STREET);
            String type = house.getString(ApiConstant.TYPE);
            String ward = house.getString(ApiConstant.WARD);
            int area = house.getInt(ApiConstant.AREA);
            String detailAdd = house.getString(ApiConstant.DETAIL_ADDRESS);

            ArrayList<String> images = new ArrayList<>();
            JSONArray arrayImage = house.getJSONArray(ApiConstant.IMAGE);
            for (int i = 0, size = arrayImage.length(); i < size; i++) {
                images.add(arrayImage.getString(i));
            }

            return new FullHouse(id, price, detailAdd, street, ward, district, city, false, area, numberOfRoom, status, propertyType,
                    description, lat, lng, address, "hello", "0123", images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Board> getFavoriteBoardsHasHeart(String json, String idHouse) {
        ArrayList<Board> arrayList = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(json);
            if (response.getBoolean(ApiConstant.HAS_BOARD)) {
                JSONArray jsonArray = response.getJSONArray(ApiConstant.LIST_BOARD);
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id = jsonObject.getString(ApiConstant._ID);
                    String name = jsonObject.getString(ApiConstant.NAME);
                    String image = jsonObject.has(ApiConstant.FIRST_IMAGE) ? jsonObject.getString(ApiConstant.FIRST_IMAGE) : "";

                    JSONArray listHouse = jsonObject.getJSONArray(ApiConstant.LIST_HOUSE);
                    ArrayList<String> list = new ArrayList<>();
                    for (int j = 0, length = listHouse.length(); j < length; j++) {
                        list.add(listHouse.getString(j));
                    }

                    arrayList.add(new BoardHasHeart(id, name, list, image, list.contains(idHouse)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
