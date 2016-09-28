package com.qtd.realestate1012.utils;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;
import com.qtd.realestate1012.model.BunchHouse;
import com.qtd.realestate1012.model.CompactHouse;
import com.qtd.realestate1012.model.FullHouse;
import com.qtd.realestate1012.model.ItemSavedSearch;
import com.qtd.realestate1012.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dell on 8/8/2016.
 */
public class ProcessJson {
    public static ArrayList<Object> getArrayListHousesNew(JSONObject jsonBoard, JSONArray jsonArray) {
        ArrayList<String> listFavoriteHouse = getListIdFavoriteHouse(jsonBoard);

        ArrayList<Object> arrayList = new ArrayList<>();

        for (int i = 0, size = jsonArray.length(); i < 1; i++) {
            try {
                JSONObject houseType = jsonArray.getJSONObject(i);

                String type = houseType.getString(ApiConstant.TYPE);
                arrayList.add(type);

                BunchHouse bunchHouse = new BunchHouse();
                bunchHouse.setType(type);

                JSONArray listHouse = houseType.getJSONArray(ApiConstant.LIST_HOUSE);
                for (int j = 0, length = listHouse.length(); j < length; j++) {
                    JSONObject house = listHouse.getJSONObject(j);

                    String id = house.getString(ApiConstant._ID);
                    int price = house.getInt(ApiConstant.PRICE);
                    String firstImage = house.getJSONArray(ApiConstant.IMAGE).getString(0);
                    String detailAddress = house.getString(ApiConstant.DETAIL_ADDRESS);
                    String street = house.getString(ApiConstant.STREET);
                    String ward = house.getString(ApiConstant.WARD);
                    String district = house.getString(ApiConstant.DISTRICT);
                    String city = house.getString(ApiConstant.CITY);
                    boolean isLiked = listFavoriteHouse.contains(id);

                    bunchHouse.addHouse(new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage, null, null, isLiked));
                }

                arrayList.add(bunchHouse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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

                    ArrayList<String> arrayList1 = new ArrayList<>();
                    for (int j = 0, length = listHouse.length(); j < length; j++) {
                        arrayList1.add(listHouse.getString(j));
                    }

                    arrayList.add(new Board(id, name, arrayList1, image));
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

    public static ArrayList<CompactHouse> getListCompactHouse(JSONObject response) {
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

                arrayList.add(new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage, null, null, false));
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
            String ward = house.getString(ApiConstant.WARD);
            int area = house.getInt(ApiConstant.AREA);
            String detailAdd = house.getString(ApiConstant.DETAIL_ADDRESS);

            ArrayList<String> images = new ArrayList<>();
            JSONArray arrayImage = house.getJSONArray(ApiConstant.IMAGE);
            for (int i = 0, size = arrayImage.length(); i < size; i++) {
                images.add(arrayImage.getString(i));
            }

            return new FullHouse(id, price, detailAdd, street, ward, district, city, false, area, numberOfRoom, status, propertyType,
                    description, lat, lng, address, images);
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

                    boolean hasHeart = false;

                    ArrayList<String> arrayList1 = new ArrayList<>();
                    for (int j = 0, length = listHouse.length(); j < length; j++) {
                        arrayList1.add(listHouse.getString(j));
                        if (listHouse.getString(j).equals(idHouse)) {
                            hasHeart = true;
                        }
                    }

                    arrayList.add(new BoardHasHeart(id, name, arrayList1, image, hasHeart));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static CompactHouse getInfoHouse(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String id = jsonObject.getString(ApiConstant._ID);
            String city = jsonObject.getString(ApiConstant.CITY);
            String detailAddress = jsonObject.getString(ApiConstant.DETAIL_ADDRESS);
            String district = jsonObject.getString(ApiConstant.DISTRICT);
            String lat = jsonObject.getString(ApiConstant.LATITUDE);
            String lng = jsonObject.getString(ApiConstant.LONGITUDE);
            int price = jsonObject.getInt(ApiConstant.PRICE);
            String street = jsonObject.getString(ApiConstant.STREET);
            String ward = jsonObject.getString(ApiConstant.WARD);
            String firstImage = jsonObject.getJSONArray(ApiConstant.IMAGE).getString(0);

            return new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage, lat, lng, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserInfo(JSONObject response) throws JSONException {
        JSONObject user = response.getJSONObject(ApiConstant.USER_INFO);

        String id = user.getString(ApiConstant._ID);
        String name = user.getString(ApiConstant.NAME);
        String image;
        if (user.getString(ApiConstant.AVATAR).contains("http")) {
            image = user.getString(ApiConstant.AVATAR);
        } else {
            image = ApiConstant.URL_WEB_SERVICE_GET_IMAGE_USER + user.getString(ApiConstant.AVATAR);
        }
        String email = user.getString(ApiConstant.EMAIL);
        String provider = user.getString(ApiConstant.PROVIDER);
        String phoneNumber = user.getString(ApiConstant.TELEPHONE);
        boolean hasReceiveNotification = user.getBoolean(ApiConstant.NOTIFICATION);

        return new User(id, name, image, email, provider, phoneNumber, hasReceiveNotification);
    }

    /***
     * trả về board khi vừa tạo thành công
     *
     * @param jsonObject chuỗi json board
     * @return board vừa được tạo
     * @throws JSONException
     */
    public static Board getBoard(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString(ApiConstant._ID);
        String name = jsonObject.getString(ApiConstant.NAME);
        return new Board(id, name, new ArrayList<String>(), "");
    }

    public static ArrayList<String> getListIdFavoriteHouse(JSONObject jsonBoard) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (jsonBoard.has(ApiConstant.LIST_BOARD)) {
            try {
                JSONArray jsonArray = jsonBoard.getJSONArray(ApiConstant.LIST_BOARD);
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONArray listHouse = jsonArray.getJSONObject(i).getJSONArray(ApiConstant.LIST_HOUSE);
                    if (listHouse.length() > 0) {
                        for (int j = 0, length = listHouse.length(); j < length; j++) {
                            arrayList.add(listHouse.getString(j));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static ArrayList<CompactHouse> getListCompactHouseHasHeart(JSONObject response, String jsonBoard) {
        ArrayList<CompactHouse> arrayList = getListCompactHouse(response);

        try {
            JSONObject jsonObject = new JSONObject(jsonBoard);
            ArrayList<String> arrayListID = getListIdFavoriteHouse(jsonObject);

            for (CompactHouse house : arrayList) {
                if (arrayListID.contains(house.getId())) {
                    house.setLiked(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static CompactHouse getCompactHouse(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.getString(ApiConstant._ID);
            int price = jsonObject.getInt(ApiConstant.PRICE);
            String detailAddress = jsonObject.getString(ApiConstant.DETAIL_ADDRESS);
            String street = jsonObject.getString(ApiConstant.STREET);
            String ward = jsonObject.getString(ApiConstant.WARD);
            String district = jsonObject.getString(ApiConstant.DISTRICT);
            String city = jsonObject.getString(ApiConstant.CITY);
            String firstImage = jsonObject.getJSONArray(ApiConstant.IMAGE).getString(0);

            return new CompactHouse(id, price, detailAddress, street, ward, district, city, firstImage, "0", "0", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ItemSavedSearch> getListItemSearch(JSONObject response) {
        ArrayList<ItemSavedSearch> arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = response.getJSONArray(ApiConstant.LIST_SEARCH);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString(ApiConstant._ID);
                String status = jsonObject.getString(ApiConstant.STATUS);
                int priceFrom = jsonObject.getInt(ApiConstant.PRICE_FROM);
                int priceTo = jsonObject.getInt(ApiConstant.PRICE_TO);
                int numberOfRooms = jsonObject.getInt(ApiConstant.NUMBER_OF_ROOMS);
                int areaFrom = jsonObject.getInt(ApiConstant.AREA_FROM);
                int areaTo = jsonObject.getInt(ApiConstant.AREA_TO);
                String property = jsonObject.getString(ApiConstant.PROPERTY_TYPE);
                String idOwner = jsonObject.getString(ApiConstant._ID_OWNER);

                arrayList.add(new ItemSavedSearch(id, status, priceFrom, priceTo, numberOfRooms, areaFrom, areaTo, property, idOwner));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
