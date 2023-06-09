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
   public void changeHiddenWord(){
       String expectedHiddenWord = "_pp__";
       String word = "apple";
       String userInput = "p";
       String hiddenWord = "_____";

       String output = changeHiddenWord(word, userInput, hiddenWord);
       Assert.assertEquals(expectedHiddenWord, output);
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
}
