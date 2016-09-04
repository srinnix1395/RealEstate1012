package com.qtd.realestate1012.constant;

/**
 * Created by Dell on 7/29/2016.
 */
public class ApiConstant {
    //    database properties
    public static final String _ID = "_id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String DOB = "DOB";
    public static final String TELEPHONE = "telephone";
    public static final String GENDER = "gender";
    public static final String AVATAR = "avatar";
    public static final String TYPE = "type";
    public static final String _ID_SOCIAL = "_id_social";
    public static final String NOTIFICATION_ID = "regID";

    //webservice
    public static final String URL_WEB_SERVICE_LOGIN_SOCIAL = "http://protectedcedar-31067.rhcloud.com/user/loginNetworkSocial";
    public static final String URL_WEB_SERVICE_LOGIN = "http://protectedcedar-31067.rhcloud.com/user/login";
    public static final String URL_WEB_SERVICE_REGISTER = "http://protectedcedar-31067.rhcloud.com/user/register";
    public static final String URL_WEB_SERVICE_IS_EMAIL_EXISTED = "http://protectedcedar-31067.rhcloud.com/user/isEmailExisted";

    public static final String URL_WEB_SERVICE_GET_BOARDS = "http://protectedcedar-31067.rhcloud.com/board/getBoards";
    public static final String URL_WEB_SERVICE_CREATE_BOARD = "http://protectedcedar-31067.rhcloud.com/board/createBoard";
    public static final String URL_WEB_SERVICE_DELETE_BOARD = "http://protectedcedar-31067.rhcloud.com/board/deleteBoard";

    public static final String URL_WEB_SERVICE_GET_ALL_HOUSE = "http://protectedcedar-31067.rhcloud.com/house/getAllHouses?lat=%s&lng=%s";
    public static final String URL_WEB_SERVICE_GET_ALL_HOUSE_OF_KIND = "http://protectedcedar-31067.rhcloud.com/house/getAllHousesOfKind";
    public static final String URL_WEB_SERVICE_GET_FAVORITE_HOUSES = "http://protectedcedar-31067.rhcloud.com/house/getFavoriteHouses";
    public static final String URL_WEB_SERVICE_HANDLE_FAVORITE_HOUSES = "http://protectedcedar-31067.rhcloud.com/house/handleFavoriteHouse";
    public static final String URL_WEB_SERVICE_DETAIL_HOUSE = "http://protectedcedar-31067.rhcloud.com/house/detailInfo";
    public static final String URL_WEB_SERVICE_GET_NEWS = "http://protectedcedar-31067.rhcloud.com/house/getNews";

    public static final String URL_WEB_SERVICE_GET_IMAGE = "http://protectedcedar-31067.rhcloud.com/image/";


    public static final String RESULT = "result";
    public static final String FAILED = "failed";
    public static final String LIST_HOUSE = "list_house";
    public static final String SUCCESS = "success";
    public static final String FACEBOOK = "facebook";
    public static final String PROVIDER = "provider";
    public static final String GOOGLE = "google";
    public static final String HAS_BOARD = "has_board";
    public static final String LIST_BOARD = "list_board";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_REGISTER = "register";

    public static final String IMAGE = "image";
    public static final String ADDRESS = "address";
    public static final String PRICE = "price";
    public static final String FIRST_IMAGE = "first_image";
    public static final String STREET = "street";
    public static final String WARD = "ward";
    public static final String DISTRICT = "district";
    public static final String CITY = "city";
    public static final String STATUS = "status";
    public static final String PROPERTY_TYPE = "property_type";
    public static final String DESCRIPTION = "description";
    public static final String NUMBER_OF_ROOMS = "number_of_rooms";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String AREA = "area";
    public static final String DETAIL_ADDRESS = "detail_address";
    public static final String HOUSE = "house";
    public static final String ACTION = "action";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_ADD = "add";
    public static final String BOARD = "board";
    public static final String URL_WEB_SERVICE = "URL";
    public static final String JSON_REQUEST = "json_request";
    public static String _ID_BOARD = "_id_board";
    public static String _ID_HOUSE = "_id_house";

    //    api place
    public static final String API_PLACE_NEAR_BY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String API_DIRECTION_URL = "http://maps.googleapis.com/maps/api/directions/xml?";
    public static final String API_PLACE_GET_LAT_LONG = "http://maps.google.com/maps/api/geocode/json?address=%s&sensor=false";
    public static final String API_MAP_GEO_CODE = "https://maps.googleapis.com/maps/api/geocode/json?lat&key=";

    public static final String API_KEY = "AIzaSyAXKdlSDdIttYHwoyC2Ss_BqQa1HETpbBs";
    public static final String API_PLACE_TYPE_SCHOOL = "school";
    public static final int DEFAULT_RADIUS = 1750;
    public static final String API_PLACE_STATUS_SUCCESS = "OK";
    public static final String API_PLACE_STATUS_FAILED = "REQUEST_DENIED";
    public static final String API_PLACE_STATUS = "status";
    public static final String API_PLACE_KEY_RESULTS = "results";
    public static final String API_PLACE_GEOMETRY = "geometry";
    public static final String API_PLACE_LOCATION = "location";
    public static final String API_PLACE_LATITUDE = "lat";
    public static final String API_PLACE_LONGITUDE = "lng";
    public static final String API_PLACE_PLACE_ID = "place_id";
    public static final String API_PLACE_DATA = "data";
    public static final String API_PLACE_KEY_TYPE = "type";
    public static final String API_PLACE_TYPE_HOSPITAL = "hospital";
    public static final String API_PLACES_TYPES = "types";

    public static final String NEXT_PAGE_TOKEN = "next_page_token";
    public static final String VICINITY = "vicinity";
    public static final String MAP_TYPE = "map_type";
}
