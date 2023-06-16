package com.example.wordquest;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity {
    //Making the alert for displaying msg when user input is empty
    AlertDialog.Builder builder;
    MyPreferences myPreferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> skinList = new ArrayList<>();
    Dictionary<String, String> button_skin = new Hashtable<>();

    public String getAndroidID()
    {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public void setPointsToMyPreferencesFromDb()
    {
        db.collection("players").document(getAndroidID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            myPreferences.savePoints(Objects.requireNonNull(documentSnapshot.get("points")).toString(), getAndroidID());
                        } else {
                            assert documentSnapshot != null;
                            myPreferences.savePoints(Objects.requireNonNull(documentSnapshot.get("points")).toString(), getAndroidID());
                            Log.d(TAG, "Document does not exist");
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void checkDbForBoats() {
        CollectionReference ref = db.collection("players").document(getAndroidID()).collection("boats");
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                if (documents != null && !documents.isEmpty()) {
                    skinList.clear(); // Clear the list before populating it
                    for (QueryDocumentSnapshot document : documents) {
                        skinList.add(Objects.requireNonNull(document.get("skin")).toString());
                    }
                    updateButtonStateLol(); // Call a method to update button visibility and behavior
                    updateButtonStateFisherman();
                } else {
                    Log.d(TAG, "Document boat doesn't exist");
                }
            } else {
                Log.e("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void updateButtonStateLol() {
        Button btnApplyLol = findViewById(R.id.btnApplyLol);
        Button btnBuyLol = findViewById(R.id.btnBuyLol);

        if (skinList.contains(button_skin.get("b"))) {
            // Skin has been bought, change button
            btnBuyLol.setVisibility(View.GONE);
            btnApplyLol.setVisibility(View.VISIBLE);
            btnApplyLol.setOnClickListener(v -> {
                myPreferences.setCurrentSkin(button_skin.get("b"));
                builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                builder.show();
            });
        } else {
            btnBuyLol.setVisibility(View.VISIBLE);
            btnApplyLol.setVisibility(View.GONE);
            btnBuyLol.setOnClickListener(v -> {
                int price = 500;
                String imageName = "lol";
                if (price <= Integer.parseInt(myPreferences.getPoints())) {
                    Log.d(TAG, "Player has enough gold");
                    int points = Integer.parseInt(myPreferences.getPoints());
                    points -= price;
                    myPreferences.savePoints(String.valueOf(points), getAndroidID());
                    myPreferences.saveSkinToDb(imageName, getAndroidID());
                    builder.setMessage("Your skin was bought successfully!").setTitle(getTitle());
                    TextView textViewGold = findViewById(R.id.textViewGold);
                    textViewGold.setText(myPreferences.getPoints());
                    checkDbForBoats();
                    builder.show();

                    // Update button visibility and behavior
                    btnBuyLol.setVisibility(View.GONE);
                    btnApplyLol.setVisibility(View.VISIBLE);
                    btnApplyLol.setOnClickListener(v2 -> {
                        myPreferences.setCurrentSkin(button_skin.get("b"));
                        builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                        builder.show();
                    });
                } else {
                    builder.setMessage("You don't have enough gold!").setTitle(getTitle());
                    builder.show();
                }
            });
        }
    }

    private void updateButtonStateFisherman() {

        Button btnApplyFisherman = findViewById(R.id.btnApplyFisherman);
        Button btnBuyFisherman = findViewById(R.id.btnBuyFisherman);

        if (skinList.contains(button_skin.get("c"))) {
            // Skin has been bought, change button
            btnBuyFisherman.setVisibility(View.GONE);
            btnApplyFisherman.setVisibility(View.VISIBLE);

            btnApplyFisherman.setOnClickListener(v -> {
                myPreferences.setCurrentSkin(button_skin.get("c"));
                builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                builder.show();
            });

        } else {
            btnBuyFisherman.setVisibility(View.VISIBLE);
            btnApplyFisherman.setVisibility(View.GONE);

            btnBuyFisherman.setOnClickListener(v -> {
                int price = 500;
                String imageName = "fisherman";
                if (price <= Integer.parseInt(myPreferences.getPoints())) {
                    Log.d(TAG, "Player has enough gold");
                    int points = Integer.parseInt(myPreferences.getPoints());
                    points -= price;

                    myPreferences.savePoints(String.valueOf(points), getAndroidID());
                    myPreferences.saveSkinToDb(imageName, getAndroidID());

                    builder.setMessage("Your skin was bought successfully!").setTitle(getTitle());

                    TextView textViewGold = findViewById(R.id.textViewGold);
                    textViewGold.setText(myPreferences.getPoints());

                    checkDbForBoats();

                    builder.show();

                    // Update button visibility and behavior
                    btnBuyFisherman.setVisibility(View.GONE);
                    btnApplyFisherman.setVisibility(View.VISIBLE);

                    btnApplyFisherman.setOnClickListener(v2 -> {
                        myPreferences.setCurrentSkin(button_skin.get("c"));
                        builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                        builder.show();
                    });
                } else {
                    builder.setMessage("You don't have enough gold!").setTitle(getTitle());
                    builder.show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button_skin.put("a", "default_ship");
        button_skin.put("b", "lol");
        button_skin.put("c", "fisherman");

        myPreferences = new MyPreferences(this);
        setContentView(R.layout.activity_shop);

        builder = new AlertDialog.Builder(this);

        setPointsToMyPreferencesFromDb();

        TextView textViewGold = findViewById(R.id.textViewGold);
        textViewGold.setText(myPreferences.getPoints());

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, MainActivity.class);
            startActivity(intent);
        });

        Button btnApplyDefaultShip = findViewById(R.id.a);
        btnApplyDefaultShip.setOnClickListener(v -> {
            myPreferences.setCurrentSkin("default_ship");
            builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
            builder.show();
        });

        checkDbForBoats();
    }
}