package com.example.wordquest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
   public void changeHiddenWordWithChar(){
       String expectedHiddenWord = "_pp__";
       String word = "apple";
       String userInput = "p";
       String hiddenWord = "_____";

       String output = changeHiddenWordWithChar(word, userInput, hiddenWord);
       Assert.assertEquals(expectedHiddenWord, output);
   }

    @Test
    public void changeHiddenWordWithString() {
        String word = "apple";
        String userInput = "apple";
        String hiddenWord = "____";
        String expectedHiddenWord = "apple";
        int lives = 3;

        String output = changeHiddenWordWithString(word, userInput, hiddenWord, lives);
        Assert.assertEquals(output, expectedHiddenWord);
    }

    public String changeHiddenWordWithChar(String word, String userInput, String hiddenWord) {
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

    private String changeHiddenWordWithString (String word, String userInput, String hiddenWord, int lives) {
        boolean win = false;
        if (userInput.equals(word)) {
            // Word guessed correctly
            win = true;
            hiddenWord = word;
        } else {
            lives--;
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
}
