package com.jw.iii.pocketjw.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by End on 2015/9/5.
 */
public class LocalPerference {

    public static void putValue(Context context, String key, int val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static void putValue(Context context, String key, boolean val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static void putValue(Context context, String key, String val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static int getValue(Context context, String key, int defVal) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getInt(key, defVal);
    }

    public static boolean getValue(Context context, String key, boolean defVal) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defVal);
    }

    public static String getValue(Context context, String key, String defVal) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getString(key, defVal);
    }

    private final static String SETTING = "Setting";
}
