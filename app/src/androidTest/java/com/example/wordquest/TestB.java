package com.example.wordquest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class TestB {
    @Rule
    public ActivityScenarioRule<ShopActivity> activityScenarioRule = new ActivityScenarioRule<>(ShopActivity.class);

    @Test
    public void testSkinApply() {
        init();
        final MyPreferences[] myPreferences = new MyPreferences[1];
        onView(withId(R.id.a)).perform(click());

        activityScenarioRule.getScenario().onActivity(activity -> {
            myPreferences[0] = new MyPreferences(activity);
            String skin = "default_ship";
            Assert.assertEquals(skin, myPreferences[0].getCurrentSkin());
        });

        release();
    }
}
