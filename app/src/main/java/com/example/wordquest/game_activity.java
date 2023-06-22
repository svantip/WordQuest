package com.example.wordquest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class game_activity extends AppCompatActivity {
    AlertDialog.Builder builder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String word;
    public String hiddenWord = "";
    List<String> documents = new ArrayList<>();

    public String getAndroidID()
    {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public void getWordsFromDb() {
        CollectionReference ref = db.collection("words");
        ref.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    documents.add(Objects.requireNonNull(document.get("word")).toString());
                }
                startGameActivity();
            }
            else
            {
                Log.e("TAG","Error getting documents: ", task.getException());
            }
        });
    }

    int convertDpToPx (int dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }



    private void startGameActivity(){
        MyPreferences myPreferences = new MyPreferences(game_activity.this);
        //Making the alert for displaying msg when user input is empty
        builder = new AlertDialog.Builder(this);
        Random random = new Random();
        int index = random.nextInt(documents.size());
        word = documents.get(index);
        //Making the "_" signs instead of displaying the word
        for(int i = 0;i<word.length();i++)
        {
            hiddenWord += "_";
        }

        //Displaying the hidden word in text view
        TextView wordToBeGuessed = findViewById(R.id.txtBoxWordToBeGuessed);
        wordToBeGuessed.setLetterSpacing(0.2f);
        wordToBeGuessed.setText(hiddenWord);

        TextView alreadyGuessedLettersView = findViewById(R.id.alreadyGuessedLetters);
        alreadyGuessedLettersView.setLetterSpacing(0.2f);
        TextView alreadyGuessedWordsView = findViewById(R.id.alreadyGuessedWords);
        EditText txtBoxGuess = findViewById(R.id.txtBoxGuess);
        TextView txtBoxLives = findViewById(R.id.txtBoxLives);
        //Setting a starting position for the boat and the chest
        ImageView imageBoat = findViewById(R.id.imageBoat);

            String imageName = myPreferences.getCurrentSkin();
            if(getResources().getIdentifier(imageName, "drawable", getPackageName())!=0) {
                int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                imageBoat.setImageResource(resourceId);
            }

        ViewGroup.MarginLayoutParams layoutParamsBoat = (ViewGroup.MarginLayoutParams) imageBoat.getLayoutParams();
        layoutParamsBoat.setMarginStart(convertDpToPx(0));
        imageBoat.setLayoutParams(layoutParamsBoat);

        ImageView imageChest = findViewById(R.id.imageChest);
        ViewGroup.MarginLayoutParams layoutParamsChest = (ViewGroup.MarginLayoutParams) imageChest.getLayoutParams();
        layoutParamsChest.setMarginStart(convertDpToPx(185));
        imageChest.setLayoutParams(layoutParamsChest);

        //When button send on keyboard is pressed
        txtBoxGuess.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                //Get the text from the EditText
                String userInput = txtBoxGuess.getText().toString().toLowerCase();
                txtBoxGuess.setText("");
                //Get hiddenWord
                String hiddenWord1 = wordToBeGuessed.getText().toString();
                //Get alreadyGuessedWords and alreadyGuessedLetters
                String alreadyGuessedLetters = alreadyGuessedLettersView.getText().toString();
                String alreadyGuessedWords = alreadyGuessedWordsView.getText().toString();
                //Get lives
                int lives = Integer.parseInt(txtBoxLives.getText().toString()); //String to integer
                boolean win = false;
                //Do something with text
                if(userInput.length() == 1) {
                    //Input is a char
                    alreadyGuessedLetters += userInput; //Displaying already guessed letters
                    alreadyGuessedLettersView.setText(alreadyGuessedLetters);
                    if(word.contains(userInput)) {
                        win = true;
                        //Char matches a char inside word
                        for(int i=0;i<word.length();i++) {
                            //Loop going threw word
                            if(word.charAt(i) == userInput.charAt(0)){
                                //Indexes are found inside the word
                                char[] hiddenWordArray = hiddenWord1.toCharArray(); //Create array from string
                                hiddenWordArray[i] = userInput.charAt(0); //Switching the correct char for the userInput char
                                hiddenWord1 = String.valueOf(hiddenWordArray); //Switching it back to string
                            }
                            if(hiddenWord1.charAt(i) == '_') win = false;
                        }
                        //Setting txtView text to hiddenWord
                        wordToBeGuessed.setText(hiddenWord1);
                    }
                    else{
                        //Char doesn't match a char inside word
                        lives--; //Lives -1
                        String livesString = Integer.toString(lives);//Integer to string
                        txtBoxLives.setText(livesString);//Displaying lives in txtView
                    }
                }else if(userInput.length() == 0){
                    //Input is empty
                    builder.setMessage("You need to write something into input") .setTitle(getTitle());//Set alerts title and text
                    builder.show();//Show alert
                }else{
                    //Input is a string
                    hiddenWord1 = word;
                    wordToBeGuessed.setText(hiddenWord1);
                    if(userInput.equals(word)) win = true;
                    else{
                        alreadyGuessedWords+=userInput+" ";
                        alreadyGuessedWordsView.setText(alreadyGuessedWords);
                        lives--; //Lives -1
                        String livesString = Integer.toString(lives);//Integer to string
                        txtBoxLives.setText(livesString);//Displaying lives in txtView
                    }
                }

                // Moving the boat
                int boatStartPosition = layoutParamsBoat.getMarginStart();
                int guessedLetters = 0;
                for (int i = 0; i < word.length(); i++) {
                    if (hiddenWord1.charAt(i) != '_') {
                        guessedLetters++;
                    }
                }

                if (guessedLetters > 0 ) {
                    int targetPosition = convertDpToPx((185 / word.length()) * guessedLetters);

                    // Create an ObjectAnimator to animate the boat's position
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageBoat, "translationX", boatStartPosition, targetPosition);
                    animator.setDuration(500); // Set the duration of the animation in milliseconds
                    animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Apply an interpolator for smooth acceleration and deceleration
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layoutParamsBoat.setMarginStart(targetPosition);

                        }
                    });
                    // Start the animation
                    animator.start();
                }
                int finalLives = lives;
                boolean finalWin = win;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(finalLives == 0){
                            //Loser screen
                            Intent intent = new Intent(game_activity.this, LosingScreen.class);
                            startActivity(intent);
                            finish();
                        } else if(finalWin) {
                            //Wining screen
                            int points = Integer.parseInt(myPreferences.getPoints());
                            points += 100 * finalLives;
                            myPreferences.setPointsPlus(Integer.toString(100 * finalLives));
                            myPreferences.savePoints(points+"", getAndroidID());
                            Intent intent = new Intent(game_activity.this, WiningScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 500);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(game_activity.this, MainActivity.class);
            startActivity(intent);
        });
        getWordsFromDb();
    }
}