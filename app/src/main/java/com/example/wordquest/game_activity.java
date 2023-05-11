package com.example.wordquest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class game_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Generate random word for testing -change later to display from database-
        String[] words = {"apple", "banana", "carrot", "dragon", "elephant", "flower", "guitar", "house", "island", "jacket"};
        Random random = new Random();
        int index = random.nextInt(words.length);
        //Word that user needs to guess
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
        EditText txtBoxGuess = (EditText) findViewById(R.id.txtBoxGuess);
        //When button send on keyboard is pressed
        txtBoxGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //Get the text from the EditText
                    String userInput = txtBoxGuess.getText().toString().toLowerCase();
                    String hiddenWord = wordToBeGuessed.getText().toString();
                    String alreadyGuessedLetters = alreadyGuessedLettersView.getText().toString();
                    //Do something with text
                    if(userInput.length() == 1) {
                        //Input is a char
                        alreadyGuessedLetters += userInput;
                        alreadyGuessedLettersView.setText(alreadyGuessedLetters);
                        if(word.contains(userInput)) {
                            //Char matches a char inside word
                            for(int i=0;i<word.length();i++) {
                                //Loop going threw word
                                if(word.charAt(i) == userInput.charAt(0)){
                                    //Indexes are found inside the word
                                    char[] hiddenWordArray = hiddenWord.toCharArray(); //Create array from string
                                    hiddenWordArray[i] = userInput.charAt(0); //Switching the correct char for the userInput char
                                    hiddenWord = String.valueOf(hiddenWordArray); //Switching it back to string
                                }
                            }
                            wordToBeGuessed.setText(hiddenWord);
                        }
                        else{
                            //Char doesn't match a char inside word

                        }
                    }else if(userInput.length() == 0){
                        //Input is empty

                    }else{
                        //Input is a string

                    }
                    return true;
                }
                return false;
            }
        });

    }


}