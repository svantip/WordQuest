package com.example.wordquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EndToEndTest {

    @Rule
    public ActivityScenarioRule<game_activity> activityRule = new ActivityScenarioRule<>(game_activity.class);

    @Test
    public void testWinningCondition() {
        init();
        // Set up the initial state of the game with a specific word
        String word = game_activity.getWord(); // Change the word here

        // Simulate a series of correct character guesses until the word is completely revealed
        for (char c : word.toCharArray()) {
            onView(withId(R.id.txtBoxGuess)).perform(typeText(String.valueOf(c)));
            onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());
        }

        // Verify that the winning screen activity is launched after the game activity finishes
        intended(hasComponent(WiningScreen.class.getName()));
        release();
    }
}
