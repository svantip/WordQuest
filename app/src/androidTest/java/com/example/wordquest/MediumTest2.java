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
public class MediumTest2 {

    @Rule
    public ActivityScenarioRule<game_activity> gameActivityActivityScenarioRule = new ActivityScenarioRule<>(game_activity.class);

    @Test
    public void testLosingCondition() {
        init();
        // Exhaust all lives without guessing the word correctly
        onView(withId(R.id.txtBoxGuess)).perform(typeText("x")); // Make a wrong guess
        onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());
        onView(withId(R.id.txtBoxGuess)).perform(typeText("x")); // Make a wrong guess
        onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());
        onView(withId(R.id.txtBoxGuess)).perform(typeText("x")); // Make a wrong guess
        onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());
        onView(withId(R.id.txtBoxGuess)).perform(typeText("x")); // Make a wrong guess
        onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());
        onView(withId(R.id.txtBoxGuess)).perform(typeText("x")); // Make a wrong guess
        onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());


        // Verify that the losing screen activity is launched after the game activity finishes
        intended(hasComponent(LosingScreen.class.getName()));
        release();
    }
}
