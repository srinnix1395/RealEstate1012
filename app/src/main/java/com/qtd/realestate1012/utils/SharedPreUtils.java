package com.qtd.realestate1012.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
