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
        Button btnApplyDefaultShip = findViewById(R.id.a);
        CollectionReference ref = db.collection("players").document(getAndroidID()).collection("boats");
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                if (documents != null && !documents.isEmpty()) {
                    skinList.clear(); // Clear the list before populating it
                    for (QueryDocumentSnapshot document : documents) {
                        skinList.add(Objects.requireNonNull(document.get("skin")).toString());
                    }
                    if (myPreferences.getCurrentSkin().equals(button_skin.get("a"))){
                        btnApplyDefaultShip.setText("APPLIED");
                        btnApplyDefaultShip.setEnabled(false);
                    }else{
                        btnApplyDefaultShip.setText("APPLY");
                        btnApplyDefaultShip.setEnabled(true);
                    }
                    updateButtonStateLol();
                    updateButtonStateFisherman();
                    updateButtonStatePC();
                    updateButtonStateKids();
                    updateButtonStateOP();
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
                checkDbForBoats();
            });

            if (myPreferences.getCurrentSkin().equals(button_skin.get("b"))){
                btnApplyLol.setText("APPLIED");
                btnApplyLol.setEnabled(false);
            }else{
                btnApplyLol.setText("APPLY");
                btnApplyLol.setEnabled(true);
            }

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
                checkDbForBoats();
            });

            if (myPreferences.getCurrentSkin().equals(button_skin.get("c"))){
                btnApplyFisherman.setText("APPLIED");
                btnApplyFisherman.setEnabled(false);
            }else{
                btnApplyFisherman.setText("APPLY");
                btnApplyFisherman.setEnabled(true);
            }
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

    private void updateButtonStatePC() {

        Button btnApplyPC = findViewById(R.id.btnApplyPC);
        Button btnBuyPC = findViewById(R.id.btnBuyPC);

        if (skinList.contains(button_skin.get("d"))) {
            // Skin has been bought, change button
            btnBuyPC.setVisibility(View.GONE);
            btnApplyPC.setVisibility(View.VISIBLE);

            btnApplyPC.setOnClickListener(v -> {
                myPreferences.setCurrentSkin(button_skin.get("d"));
                builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                builder.show();
                checkDbForBoats();
            });

            if (myPreferences.getCurrentSkin().equals(button_skin.get("d"))){
                btnApplyPC.setText("APPLIED");
                btnApplyPC.setEnabled(false);
            }else{
                btnApplyPC.setText("APPLY");
                btnApplyPC.setEnabled(true);
            }

        } else {
            btnBuyPC.setVisibility(View.VISIBLE);
            btnApplyPC.setVisibility(View.GONE);

            btnBuyPC.setOnClickListener(v -> {
                int price = 1000;
                String imageName = "pc";
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
                    btnBuyPC.setVisibility(View.GONE);
                    btnApplyPC.setVisibility(View.VISIBLE);

                    btnApplyPC.setOnClickListener(v2 -> {
                        myPreferences.setCurrentSkin(button_skin.get("d"));
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

    private void updateButtonStateKids() {

        Button btnApplyKids = findViewById(R.id.btnApplyKids);
        Button btnBuyKids = findViewById(R.id.btnBuyKids);

        if (skinList.contains(button_skin.get("e"))) {
            // Skin has been bought, change button
            btnBuyKids.setVisibility(View.GONE);
            btnApplyKids.setVisibility(View.VISIBLE);

            btnApplyKids.setOnClickListener(v -> {
                myPreferences.setCurrentSkin(button_skin.get("e"));
                builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                builder.show();
                checkDbForBoats();
            });

            if (myPreferences.getCurrentSkin().equals(button_skin.get("e"))){
                btnApplyKids.setText("APPLIED");
                btnApplyKids.setEnabled(false);
            }else{
                btnApplyKids.setText("APPLY");
                btnApplyKids.setEnabled(true);
            }

        } else {
            btnBuyKids.setVisibility(View.VISIBLE);
            btnApplyKids.setVisibility(View.GONE);

            btnBuyKids.setOnClickListener(v -> {
                int price = 1000;
                String imageName = "kids";
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
                    btnBuyKids.setVisibility(View.GONE);
                    btnApplyKids.setVisibility(View.VISIBLE);

                    btnApplyKids.setOnClickListener(v2 -> {
                        myPreferences.setCurrentSkin(button_skin.get("e"));
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

    private void updateButtonStateOP() {

        Button btnApplyOP = findViewById(R.id.btnApply1P);
        Button btnBuyOP = findViewById(R.id.btnBuy1P);

        if (skinList.contains(button_skin.get("f"))) {
            // Skin has been bought, change button
            btnBuyOP.setVisibility(View.GONE);
            btnApplyOP.setVisibility(View.VISIBLE);

            btnApplyOP.setOnClickListener(v -> {
                myPreferences.setCurrentSkin(button_skin.get("f"));
                builder.setMessage("Your skin was set successfully!").setTitle(getTitle());
                builder.show();
                checkDbForBoats();
            });

            if (myPreferences.getCurrentSkin().equals(button_skin.get("f"))){
                btnApplyOP.setText("APPLIED");
                btnApplyOP.setEnabled(false);
            }else{
                btnApplyOP.setText("APPLY");
                btnApplyOP.setEnabled(true);
            }

        } else {
            btnBuyOP.setVisibility(View.VISIBLE);
            btnApplyOP.setVisibility(View.GONE);

            btnBuyOP.setOnClickListener(v -> {
                int price = 1000000;
                String imageName = "op";
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
                    btnBuyOP.setVisibility(View.GONE);
                    btnApplyOP.setVisibility(View.VISIBLE);

                    btnApplyOP.setOnClickListener(v2 -> {
                        myPreferences.setCurrentSkin(button_skin.get("f"));
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
        button_skin.put("d", "pc");
        button_skin.put("e", "kids");
        button_skin.put("f", "op");

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
            checkDbForBoats();
        });

        checkDbForBoats();
    }
}