package com.example.wordquest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LosingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing_screen);
        Intent intent1 = new Intent(LosingScreen.this, game_activity.class);
        startActivity(intent1);
        Intent intent2 = new Intent(LosingScreen.this, MainActivity.class);
        startActivity(intent2);
    }
}