package com.example.wordquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.NullValue;

import java.util.List;
import java.util.Random;

public class game_activity extends AppCompatActivity {
    AlertDialog.Builder builder;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String word;
    String[] words = {};

    int convertDpToPx (int dp){
      int px = (int) TypedValue.applyDimension(
              TypedValue.COMPLEX_UNIT_DIP,
              dp,
              getResources().getDisplayMetrics()
      );
      return px;
    };

    /*List<DocumentSnapshot> documents = querySnapshot.getDocuments();
    int randomIndex = new Random().nextInt(documents.size());
    DocumentSnapshot randomDocument = documents.get(randomIndex);
    Object randomValue = randomDocument.get("word");
                            if (randomValue != null) {
        word = randomValue.toString();
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();

        //Making the alk:''ert for displaying msg when user input is empty
        builder = new AlertDialog.Builder(this);
        //Generate random word for testing -change later to display from database-
        //String[] words = {"apple", "banana", "carrot", "dragon", "elephant", "flower", "guitar", "house", "island", "jacket"};

        firestore.collection("words")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int brc = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                words[brc] = document.get("word").toString();
                                brc++;
                            }
                        } else {
                            Log.w("Error","Error getting documents.", task.getException());
                        }
                    }
                });

        if(words == null)words[0] = "prazno";
        Random random = new Random();
        int index = random.nextInt(words.length);
        //Word that user needs to guess
        word = words[index];

        //Making the "_" signs instead of displaying the word
        String hiddenWord = "";
        for(int i = 0;i<word.length();i++)
        {
            hiddenWord += "_";
        }
        //Displaying the hidden word in text view
        TextView wordToBeGuessed = (TextView) findViewById(R.id.txtBoxWordToBeGuessed);
        wordToBeGuessed.setLetterSpacing(0.2f);
        wordToBeGuessed.setText(hiddenWord);

        TextView alreadyGuessedLettersView = (TextView) findViewById(R.id.alreadyGuessedLetters);
        alreadyGuessedLettersView.setLetterSpacing(0.2f);
        TextView alreadyGuessedWordsView = (TextView) findViewById(R.id.alreadyGuessedWords);
        EditText txtBoxGuess = (EditText) findViewById(R.id.txtBoxGuess);
        TextView txtBoxLives = findViewById(R.id.txtBoxLives);
        ImageView imageBoat = findViewById(R.id.imageBoat);
        ViewGroup.MarginLayoutParams layoutParamsBoat = (ViewGroup.MarginLayoutParams) imageBoat.getLayoutParams();
        layoutParamsBoat.setMarginStart(convertDpToPx(0));  // Set the desired margin in pixels
        imageBoat.setLayoutParams(layoutParamsBoat);
        ImageView imageChest = findViewById(R.id.imageChest);
        ViewGroup.MarginLayoutParams layoutParamsChest = (ViewGroup.MarginLayoutParams) imageChest.getLayoutParams();
        layoutParamsChest.setMarginStart(convertDpToPx(185));  // Set the desired margin in pixels
        imageChest.setLayoutParams(layoutParamsChest);



        //When button send on keyboard is pressed
        txtBoxGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //Get the text from the EditText
                    String userInput = txtBoxGuess.getText().toString().toLowerCase();
                    txtBoxGuess.setText("");
                    //Get hiddenWord
                    String hiddenWord = wordToBeGuessed.getText().toString();
                    //Get alreadyGuessedWords and alreadyGuessedLetters
                    String alreadyGuessedLetters = alreadyGuessedLettersView.getText().toString();
                    String alreadyGuessedWords = alreadyGuessedWordsView.getText().toString();
                    //Get lives
                    int lives = Integer.valueOf(txtBoxLives.getText().toString()); //String to integer
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
                                    char[] hiddenWordArray = hiddenWord.toCharArray(); //Create array from string
                                    hiddenWordArray[i] = userInput.charAt(0); //Switching the correct char for the userInput char
                                    hiddenWord = String.valueOf(hiddenWordArray); //Switching it back to string
                                }
                                if(hiddenWord.charAt(i) == '_') win = false;
                            }
                            //Setting txtView text to hiddenWord
                            wordToBeGuessed.setText(hiddenWord);
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
                        if(userInput.equals(word)) win = true;
                        else{
                            alreadyGuessedWords+=userInput+" ";
                            alreadyGuessedWordsView.setText(alreadyGuessedWords);
                            lives--; //Lives -1
                            String livesString = Integer.toString(lives);//Integer to string
                            txtBoxLives.setText(livesString);//Displaying lives in txtView
                        }
                    }

                    int guessedLetters = 0;
                    for(int i=0;i<word.length();i++) if(hiddenWord.charAt(i) != '_') guessedLetters++;
                    if(guessedLetters > 0){
                        layoutParamsBoat.setMarginStart(convertDpToPx((185/word.length())*guessedLetters));  // Set the desired margin in pixels
                        imageBoat.setLayoutParams(layoutParamsBoat);
                        layoutParamsChest.setMarginStart(convertDpToPx(185-(185/word.length())*guessedLetters));  // Set the desired margin in pixels
                        imageChest.setLayoutParams(layoutParamsChest);
                    }

                    if(lives == 0){
                        //Loser screen
                        Intent intent = new Intent(game_activity.this, LosingScreen.class);
                        startActivity(intent);
                    } else if(win) {
                        Intent intent = new Intent(game_activity.this, WiningScreen.class);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });

    }


}