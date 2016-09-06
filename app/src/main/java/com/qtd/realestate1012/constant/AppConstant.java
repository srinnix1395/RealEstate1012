package com.qtd.realestate1012.constant;

import android.widget.LinearLayout;

import com.qtd.realestate1012.R;

/**
 * Created by Dell on 7/28/2016.
 */
public class AppConstant {
    public static final int TURN_ON_SATELLITE_MODE = 1;
    public static final int TURN_OFF_SATELLITE_MODE = 0;
    public static final int WHAT_LOCAL_INFO_ASYNC_TASK = 2;
    public static final int REQUEST_CODE_SIGN_IN_GOOGLE = 3;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 4;
    public static final String HOUSIE_SHARED_PREFERENCES = "housie_shared_preferences";
    public static final String USER_LOGGED_IN = "user_logged_in";
    public static final String LAST_EMAIL_AT_LOGIN_ACTIVITY = "last_email";
    public static final int REQUEST_CODE_CREATE_BOARD = 5;
    public static final int REQUEST_CODE_SIGN_IN = 6;
    public static final int REQUEST_CODE_FILTER_ACTIVITY = 7;
    public static final int REQUEST_CODE_CALL_PHONE_PERMISSION_AGENT = 8;
    public static final int REQUEST_CODE_CALL_PHONE_PERMISSION_OWNER = 9;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
    public static final int REQUEST_CODE_IMAGES_PICKER = 11;
    public static final int TYPE_PICKER_AREA = 12;
    public static final int TYPE_PICKER_NUMBER_OF_ROOM = 13;

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
    public static final int HEIGHT_LAYOUT_INFO_EXPANDED = LinearLayout.LayoutParams.WRAP_CONTENT;
    public static int MAX_LINE_COLLAPSE = 3;
    public static int MAX_LINE_EXPAND = 20;
}
