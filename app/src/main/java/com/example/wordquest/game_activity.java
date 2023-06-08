package com.example.wordquest;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;


public class game_activity extends AppCompatActivity {
    AlertDialog.Builder builder;
    String word;

    int convertDpToPx (int dp){
      int px = (int) TypedValue.applyDimension(
              TypedValue.COMPLEX_UNIT_DIP,
              dp,
              getResources().getDisplayMetrics()
      );
      return px;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Making the alk:''ert for displaying msg when user input is empty
        builder = new AlertDialog.Builder(this);

        //Array of all words
        String[] words = {"apple", "guitar", "jacket", "elephant", "carrot", "dragon", "house", "banana", "island", "flower"};

        //Getting a random word
        Random random = new Random();
        int index = random.nextInt(words.length);
        String word = words[index];

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
        //Setting a starting position for the boat and the chest
        ImageView imageBoat = findViewById(R.id.imageBoat);
        ViewGroup.MarginLayoutParams layoutParamsBoat = (ViewGroup.MarginLayoutParams) imageBoat.getLayoutParams();
        layoutParamsBoat.setMarginStart(convertDpToPx(0));
        imageBoat.setLayoutParams(layoutParamsBoat);
        ImageView imageChest = findViewById(R.id.imageChest);
        ViewGroup.MarginLayoutParams layoutParamsChest = (ViewGroup.MarginLayoutParams) imageChest.getLayoutParams();
        layoutParamsChest.setMarginStart(convertDpToPx(185));
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

                    //Moving the boat
                    int guessedLetters = 0;
                    for(int i=0;i<word.length();i++) if(hiddenWord.charAt(i) != '_') guessedLetters++;
                    if(guessedLetters > 0){
                        layoutParamsBoat.setMarginStart(convertDpToPx((185/word.length())*guessedLetters));
                        imageBoat.setLayoutParams(layoutParamsBoat);
                        layoutParamsChest.setMarginStart(convertDpToPx(185-(185/word.length())*guessedLetters));
                        imageChest.setLayoutParams(layoutParamsChest);
                    }

                    if(lives == 0){
                        //Loser screen
                        Intent intent = new Intent(game_activity.this, LosingScreen.class);
                        startActivity(intent);
                        finish();
                    } else if(win) {
                        //Wining screen
                        Intent intent = new Intent(game_activity.this, WiningScreen.class);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });

    }
}