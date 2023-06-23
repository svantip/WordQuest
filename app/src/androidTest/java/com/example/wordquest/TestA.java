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

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestA {
    @Rule
    public ActivityScenarioRule<game_activity> activityScenarioRule = new ActivityScenarioRule<>(game_activity.class);

    @Test
    public void testLose() throws InterruptedException {
        init();

        for (int i =0;i<5;i++){
            onView(withId(R.id.txtBoxGuess)).perform(typeText(String.valueOf("x")));
            onView(withId(R.id.txtBoxGuess)).perform(pressImeActionButton());
        }
        TimeUnit.SECONDS.sleep(1);
        intended(hasComponent(LosingScreen.class.getName()));
        release();
    }
}
