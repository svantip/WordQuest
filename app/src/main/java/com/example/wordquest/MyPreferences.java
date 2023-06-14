package com.example.wordquest;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    public static final String key_points = "points";

    private final SharedPreferences sharedPreferences;

    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
    }

    public void savePoints(String points) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_points, points);
        editor.apply();
    }

    public String getPoints() {
        return sharedPreferences.getString(key_points, "");
    }

    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
