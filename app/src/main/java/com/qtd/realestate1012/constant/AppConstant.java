package com.qtd.realestate1012.constant;

import com.qtd.realestate1012.R;

/**
 * Created by Dell on 7/28/2016.
 */
public class AppConstant {
    public static final int REQUEST_CODE_SIGN_IN_GOOGLE = 1;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 2;
    public static final int REQUEST_CODE_CREATE_BOARD = 3;
    public static final int REQUEST_CODE_SIGN_IN = 4;
    public static final int REQUEST_CODE_FILTER_ACTIVITY = 5;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 6;
    public static final int REQUEST_CODE_IMAGES_PICKER = 7;
    public static final int NOTIFICATION_ID = 10121995;
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String ACTION_NOTIFICATION = "Housie.notification.newHouse";
    public static final String BOARD_DETAIL_ACTIVITY = "com.qtd.realestate1012.activity.BoardDetailActivity";
    public static final String RESULT_ACTIVITY = "com.qtd.realestate1012.activity.ResultActivity";
    public static final String HOUSIE_SHARED_PREFERENCES = "housie_shared_preferences";
    public static final String USER_LOGGED_IN = "user_logged_in";
    public static final String LAST_EMAIL_AT_LOGIN_ACTIVITY = "last_email";
    public static final int REQUEST_CODE_DETAIL_KIND = 8;

    public static int ICON_TAB_NORMAL[] = {
            R.drawable.ic_home_dark_green,
            R.drawable.ic_search_dark_green,
            R.drawable.ic_heart_dark_green,
            R.drawable.ic_bell_dark_green,
            R.drawable.ic_dot_vertical_dark_green
    };

    public static int ICON_TAB_SELECTED[] = {
            R.drawable.ic_home_white,
            R.drawable.ic_search_white,
            R.drawable.ic_heart_white_small,
            R.drawable.ic_bell_white
    };

    public static int TITLE_TAB_FAVORITE[] = {
            R.string.boards,
            R.string.homes,
            R.string.searches
    };

    public static final int SEARCH_FRAGMENT_TAB = 1;
    public static final int LAST_TAB = 4;

    public static final double LATITUDE_HANOI = 21.027784;
    public static final double LONGITUDE_HANOI = 105.834217;

    public static final int HEIGHT_LAYOUT_INFO_COLLAPSED = 564;
    public static int MAX_LINE_COLLAPSE = 3;
    public static int MAX_LINE_EXPAND = 20;
}
