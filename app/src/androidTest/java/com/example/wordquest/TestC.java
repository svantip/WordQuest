package com.example.wordquest;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestC {
    @Rule
    public ActivityScenarioRule<game_activity> activityScenarioRule = new ActivityScenarioRule<>(game_activity.class);

    @Test
    public void testPoints() {
        Intents.init();

        final MyPreferences[] myPreferences = new MyPreferences[1];
        final CountDownLatch latch = new CountDownLatch(1);

        activityScenarioRule.getScenario().onActivity(activity -> {
            myPreferences[0] = new MyPreferences(activity);
            int expPoints = Integer.parseInt(myPreferences[0].getPoints());

            Thread interactionThread = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    Espresso.onView(withId(R.id.txtBoxGuess))
                            .perform(ViewActions.typeText("x"));
                    Espresso.onView(withId(R.id.txtBoxGuess))
                            .perform(ViewActions.pressImeActionButton());
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int points = Integer.parseInt(myPreferences[0].getPoints());

                Assert.assertEquals(expPoints, points);

                latch.countDown();
            });

            interactionThread.start();
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intents.release();
    }
}
