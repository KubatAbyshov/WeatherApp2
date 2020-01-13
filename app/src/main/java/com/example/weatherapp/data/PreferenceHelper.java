package com.example.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static SharedPreferences preferences;
    private static final String MY_PREFS = "MY_PREFS";
    private static final String IS_NOT_FIRST_LAUNCH = "IS_NOT_FIRST_LAUNCH";

    public PreferenceHelper(Context context) {
        preferences = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
    }

    public static void setIsFirstLaunch(){
        preferences.edit().putBoolean(IS_NOT_FIRST_LAUNCH, true).apply();
    }

    public static boolean getIsNotFirstLaunch(){
       return preferences.getBoolean(IS_NOT_FIRST_LAUNCH, false);
    }

    public void saveisShow(){
        preferences.edit().putBoolean(MY_PREFS , false);
    }

}
