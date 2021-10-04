package com.example.readbarcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Save {

    private static final String DEFAULT_STRING = "";
    private static final int DEFAULT_INT = 0;
    private static final boolean DEFAULT_BOOLEAN = false;
    Context context;

    public Save(Context context){
        this.context = context;
    }




    //  String 값 저장하고 불러오는 메소드
    void setString(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value).apply();
    }
    String getString(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        String stringValue = sp.getString(key, DEFAULT_STRING);
        return stringValue;
    }

    //  Int 값 저장하고 불러오는 메소드
    void setInt(String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value).apply();
    }
    int getInt(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        int intValue = sp.getInt(key, DEFAULT_INT);
        return intValue;
    }

    //  Boolean 값 저장하고 불러오는 메소드
    void setBoolean(String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value).apply();
    }
    boolean getBoolean(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean booleanValue = sp.getBoolean(key, DEFAULT_BOOLEAN);
        return booleanValue;
    }
}
