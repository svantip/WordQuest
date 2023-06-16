package com.example.wordquest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyPreferences {
    public static final String key_points = "points";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String key_current_skin = "current_skin";
    private final SharedPreferences sharedPreferences;

    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
    }

    public String getCurrentSkin() {
        return sharedPreferences.getString(key_current_skin, "");
    }

    public void setCurrentSkin(String skin)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_current_skin, skin);
        editor.apply();
    }

    public void saveSkinToDb(String skin, String androidID)
    {
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("skin", skin);
        db.collection("players").document(androidID).collection("boats").document()
                .set(playerData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Player skin was set successfully to document"))
                .addOnFailureListener(e -> Log.e(TAG, "Error setting skin to document", e));
    }

    public void savePoints(String points, String androidID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_points, points);
        editor.apply();

        Map<String, Object> playerData = new HashMap<>();
        playerData.put("points", points);
        db.collection("players").document(androidID)
                .set(playerData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Player points set successfully to document"))
                .addOnFailureListener(e -> Log.e(TAG, "Error setting points to document", e));
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
