package com.huaqin.mybill.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.huaqin.mybill.MyApplication;


public class SharedPreferenceUtils {
    private static SharedPreferences getSharedPreferences(Context context) {
        String pkgName = MyApplication.getContext().getPackageName();
        return context.getSharedPreferences(pkgName + "_preferenrces", Context.MODE_PRIVATE);
    }

    public static void savePref(String key, String value) {
        SharedPreferences.Editor edit = getSharedPreferences(MyApplication.getContext()).edit();
        edit.putString(key, value);
        edit.apply();//提交String
    }

    public static void savePref(String key, Integer value) {
        SharedPreferences.Editor edit = getSharedPreferences(MyApplication.getContext()).edit();
        edit.putInt(key, value);
        edit.apply();//提交Integer
    }

    public static void savePref(String key, Long value) {
        SharedPreferences.Editor edit = getSharedPreferences(MyApplication.getContext()).edit();
        edit.putLong(key, value);
        edit.apply();//提交Long
    }

    public static void savePref(String key, Boolean value) {
        SharedPreferences.Editor edit = getSharedPreferences(MyApplication.getContext()).edit();
        edit.putBoolean(key, value);
        edit.apply();//提交Boolean
    }

    public static String getPrefString(String key, String defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.getContext());
        return sharedPreferences.getString(key, defult);
    }

    public static Integer getPrefInteger(String key, Integer defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.getContext());
        return sharedPreferences.getInt(key, defult);
    }

    public static Long getPrefLong(String key, Long defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.getContext());
        return sharedPreferences.getLong(key, defult);
    }

    public static Boolean getPrefBoolean(String key, Boolean defult) {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.getContext());
        return sharedPreferences.getBoolean(key, defult);
    }

    public static void removeByKey(Context context, String key) {
        SharedPreferences.Editor edit = getSharedPreferences(MyApplication.getContext()).edit();
        edit.remove(key);
        edit.apply();
    }

    public static void clear() {
        SharedPreferences.Editor edit = getSharedPreferences(MyApplication.getContext()).edit();
        edit.clear();
    }
}
