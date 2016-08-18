package com.qtd.realestate1012.utils;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.model.CompactHouse;

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
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString(ApiConstant._ID);
                String address = object.getString(ApiConstant.ADDRESS);
                int price = object.getInt(ApiConstant.PRICE);
                String image = object.getJSONArray(ApiConstant.IMAGE).getJSONObject(0).getString(ApiConstant.IMAGE_NO1);
                bunchHouse.addHouse(new CompactHouse(id, price, address, image));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        bunchHouse.setType("Nhà thường");
        return arrayList;
    }

    public static ArrayList<Board> getBoardFavorite(JSONObject response) {
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
}
