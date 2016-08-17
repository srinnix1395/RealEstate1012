package com.qtd.realestate1012.constant;

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
            R.drawable.ic_heart_white,
            R.drawable.ic_bell_white
    };

    public static int TITLE_TAB_FAVORITE[] = {
            R.string.boards,
            R.string.homes,
            R.string.searches
    };


    public static final int LAST_TAB = 4;
    public static final double LATITUDE_HANOI = 21.027784;
    public static final double LONGITUDE_HANOI = 105.834217;

}
