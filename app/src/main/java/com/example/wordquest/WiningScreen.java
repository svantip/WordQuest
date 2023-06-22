package com.example.wordquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WiningScreen extends AppCompatActivity {

    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wining_screen);
        myPreferences = new MyPreferences(this);
        TextView textViewGoldPlus = findViewById(R.id.textViewGoldPlus);
        textViewGoldPlus.setText(myPreferences.getPointsPlus());
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(WiningScreen.this, game_activity.class);
            startActivity(intent);
            finish();
        });

        Button btnMainMenu = findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(v -> finish());
    }
}