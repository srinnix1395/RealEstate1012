package com.qtd.realestate1012.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;

/**
 * Created by Dell on 8/7/2016.
 */
public class SharedPreUtils {
    private SharedPreferences sharedPreferences;

    public SharedPreUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstant.HOUSIE_SHARED_PREFERENCES, Context.MODE_APPEND);
    }

    public void putInt(String key, int vaue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, vaue);
        editor.apply();
    }

    public int getInt(String key, int defVal) {
        return sharedPreferences.getInt(key, defVal);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defVal) {
        return sharedPreferences.getString(key, defVal);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void putUserData(boolean userLoggedIn, String id, String email, String provider, String jsonBoard) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(AppConstant.USER_LOGGED_IN, userLoggedIn);
        editor.putString(ApiConstant._ID, id);
        editor.putString(ApiConstant.EMAIL, email);
        editor.putString(ApiConstant.PROVIDER, provider);
        editor.putString(ApiConstant.LIST_BOARD, jsonBoard);

        editor.apply();
    }

    public void removeUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(AppConstant.USER_LOGGED_IN);
        editor.remove(ApiConstant._ID);
        editor.remove(ApiConstant.EMAIL);
        editor.remove(ApiConstant.PROVIDER);
        editor.remove(ApiConstant.LIST_BOARD);

        editor.apply();
    }
}
