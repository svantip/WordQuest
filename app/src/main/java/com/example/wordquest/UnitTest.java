package com.example.wordquest;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class UnitTest {
   @Test
   public void makeHiddenWord() {
        String word = "test";
        word = makeHiddenWord(word);
        String expectedHiddenWord = "____";

        Assert.assertEquals(expectedHiddenWord, word);
   }
   @Test
   public void changeHiddenWord(){
       String expectedHiddenWord = "_pp__";
       String word = "apple";
       String userInput = "p";
       String hiddenWord = "_____";

       String output = changeHiddenWord(word, userInput, hiddenWord);
       Assert.assertEquals(expectedHiddenWord, output);
   }
    @Test
    public void testUpdateImagePositions() {
        // Create mock ImageView objects
        ImageView imageBoat = new ImageView(null);
        ImageView imageChest = new ImageView(null);

        // Create mock MarginLayoutParams objects
        ViewGroup.MarginLayoutParams layoutParamsBoat = new ViewGroup.MarginLayoutParams(0, 0);
        ViewGroup.MarginLayoutParams layoutParamsChest = new ViewGroup.MarginLayoutParams(0, 0);

        // Set initial margin start values
        int initialMarginBoat = 0;
        int initialMarginChest = 185;
        layoutParamsBoat.setMarginStart(initialMarginBoat);
        layoutParamsChest.setMarginStart(initialMarginChest);

        // Set initial layout params for the image views
        imageBoat.setLayoutParams(layoutParamsBoat);
        imageChest.setLayoutParams(layoutParamsChest);

        // Simulate guessed letters count
        int guessedLetters = 2;
        int wordLength = 7;

        // Get the mock resources object
        Resources resources = Mockito.mock(Resources.class);

        // Set the desired density for the mock resources object
        float density = 2.0f; // Set your desired density value
        Mockito.when(resources.getDisplayMetrics()).thenReturn(createDisplayMetrics(density));

        // Calculate the expected margin start values
        int expectedMarginBoat = convertDpToPx((185 / wordLength) * guessedLetters, resources);
        int expectedMarginChest = convertDpToPx(185 - (185 / wordLength) * guessedLetters, resources);

        // Update the image positions
        updateImagePositions(imageBoat, layoutParamsBoat, imageChest, layoutParamsChest, guessedLetters, wordLength, resources);

        // Get the updated margin start values
        int updatedMarginBoat = layoutParamsBoat.getMarginStart();
        int updatedMarginChest = layoutParamsChest.getMarginStart();

        // Assert that the updated margin start values match the expected values
        Assert.assertEquals(expectedMarginBoat, updatedMarginBoat);
        Assert.assertEquals(expectedMarginChest, updatedMarginChest);
    }

    private void updateImagePositions(ImageView imageBoat, ViewGroup.MarginLayoutParams layoutParamsBoat,
                                      ImageView imageChest, ViewGroup.MarginLayoutParams layoutParamsChest,
                                      int guessedLetters, int wordLength, Resources resources) {
        int convertedMarginBoat = convertDpToPx((185 / wordLength) * guessedLetters, resources);
        layoutParamsBoat.setMarginStart(convertedMarginBoat);
        imageBoat.setLayoutParams(layoutParamsBoat);

        int convertedMarginChest = convertDpToPx(185 - (185 / wordLength) * guessedLetters, resources);
        layoutParamsChest.setMarginStart(convertedMarginChest);
        imageChest.setLayoutParams(layoutParamsChest);
    }


    public String changeHiddenWord(String word, String userInput, String hiddenWord) {
       if (word.contains(userInput)) {
           //Char matches a char inside word
           for (int i = 0; i < word.length(); i++) {
               //Loop going threw word
               if (word.charAt(i) == userInput.charAt(0)) {
                   //Indexes are found inside the word
                   char[] hiddenWordArray = hiddenWord.toCharArray(); //Create array from string
                   hiddenWordArray[i] = userInput.charAt(0); //Switching the correct char for the userInput char
                   hiddenWord = String.valueOf(hiddenWordArray); //Switching it back to string
               }
           }
           //Setting txtView text to hiddenWord
           return hiddenWord;
       }
       return hiddenWord;
   }
   public String makeHiddenWord(String word)
   {
       String hiddenWord = "";
       for(int i = 0;i<word.length();i++)
       {
           hiddenWord += "_";
       }
       return hiddenWord;
   }
    public int convertDpToPx (int dp, Resources resources){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    private android.util.DisplayMetrics createDisplayMetrics(float density) {
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        metrics.density = density;
        return metrics;
    }

}
