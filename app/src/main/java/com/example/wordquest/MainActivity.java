package com.example.wordquest;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MyPreferences myPreferences;

    public String getAndroidID()
    {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public void createPlayerAndCheckIfExistsToDb() {
        // Check if the document already exists
        DocumentReference playerRef = db.collection("players").document(getAndroidID());
        playerRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Document already exists
                            // Handle the case where the document already exists, if needed
                            Log.d(TAG, "Player document already exists");
                        } else {
                            // Document doesn't exist, create a new one
                            // Create a new player document
                            Map<String, Object> playerData = new HashMap<>();
                            playerData.put("points", 0); // Initial points value

                            // Add the document to the "players" collection with the Android ID as the document identifier
                            db.collection("players").document(getAndroidID())
                                    .set(playerData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Document created successfully
                                        // Handle any additional logic here
                                        Log.d(TAG, "Player document created successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        // An error occurred while creating the document
                                        // Handle the error appropriately
                                        Log.e(TAG, "Error creating player document", e);
                                    });
                        }
                    } else {
                        // An error occurred while checking the document existence
                        // Handle the error appropriately
                        Log.e(TAG, "Error checking player document existence", task.getException());
                    }
                });
    }

    public void setPointsToMyPreferencesFromDb()
    {
        db.collection("players").document(getAndroidID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            myPreferences.savePoints(Objects.requireNonNull(documentSnapshot.get("points")).toString());
                        } else {
                            assert documentSnapshot != null;
                            myPreferences.savePoints(Objects.requireNonNull(documentSnapshot.get("points")).toString());
                            Log.d(TAG, "Document does not exist");
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPreferences = new MyPreferences(getApplicationContext());

        createPlayerAndCheckIfExistsToDb();
        setPointsToMyPreferencesFromDb();

        TextView textViewGold = (TextView) findViewById(R.id.textViewGold);
        textViewGold.setText(myPreferences.getPoints());

        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button btnHowToPlay = (Button) findViewById(R.id.btnHowToPlay);
        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, game_activity.class);
            startActivity(intent);
        });

        btnHowToPlay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HowToPlay.class);
            startActivity(intent);
        });

        btnExit.setOnClickListener(view -> {
            finish(); // Close the current activity and exit the application
        });
    }

}