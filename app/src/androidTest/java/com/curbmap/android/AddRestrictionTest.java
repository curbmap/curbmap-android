/*
 * Copyright (c) 2018 curbmap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.curbmap.android;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Tests adding a restriction through manually typing in a form
 * it tests several difference scenarios, which results in a relatively complete
 * testing of the user cases of users adding restrictions through the form
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddRestrictionTest {
    static final String TAG = "AddRestrictionTest";

    private UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(MainActivity.class,
                    false,
                    false) {
            };

    @Before
    public void initActivity() {
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    //custom type: festival, weekends, head in
    public void custom() throws Exception {
        pauseTestFor(500);
        onView(withContentDescription("Google Map")).perform(click());
        for (int x = 300; x < 800; x += 300) {
            for (int y = 1000; y < 1500; y += 300) {
                uiDevice.click(x, y);
                pauseTestFor(300);
            }
        }
        onView(withId(R.id.addRestrictionButton)).perform(click());
        pauseTestFor(500);
        onView(withId(R.id.customTypeLabel)).perform(scrollTo(), click());
        onView(withId(R.id.customTypeText)).perform(scrollTo(), typeText("Festival"));
        closeSoftKeyboard();
        onView(withId(R.id.sunday)).perform(scrollTo(), click());
        onView(withId(R.id.saturday)).perform(scrollTo(), click());
        onView(withId(R.id.allDay)).perform(scrollTo(), click());
        onView(withId(R.id.parallel)).perform(scrollTo(), click());
        onView(withId(R.id.submitButton)).perform(click());
        pauseTestFor(500);
    }

    @Test
    //warning: the submit button is over the head in when scroll to head in
    //so it will be unable to select head in during the test and thus unable to submit
    //no parking, 20 minutes, monday, from 4 to 5, angle_angled
    public void addFromTo() throws Exception {
        pauseTestFor(500);
        onView(withContentDescription("Google Map"))
                .perform(click());
        for (int x = 300; x < 800; x += 300) {
            for (int y = 1000; y < 1500; y += 300) {
                uiDevice.click(x, y);
                pauseTestFor(300);
            }
        }
        onView(withId(R.id.addRestrictionButton)).perform(click());
        pauseTestFor(500);
        onView(withId(R.id.noParking)).perform(scrollTo(), click());
        onView(withId(R.id.timeLimitNumber)).perform(scrollTo(), typeText(String.valueOf("20")));
        closeSoftKeyboard();
        onView(withId(R.id.monday)).perform(scrollTo(), click());
        onView(withId(R.id.withinHoursOf)).perform(scrollTo(), click());
        onView(allOf(withId(R.id.fromTime))).perform(replaceText("4:0"));
        onView(allOf(withId(R.id.toTime))).perform(replaceText("5:0"));
        onView(withId(R.id.parallel)).perform(scrollTo(), click());
        onView(withId(R.id.submitButton)).perform(click());
        pauseTestFor(500);
    }

    @Test
    //time limit parking, 20 minutes, sunday, all day, angle_parallel
    public void addAllDay() throws Exception {
        pauseTestFor(500);
        onView(withContentDescription("Google Map")).perform(click());
        for (int x = 300; x < 800; x += 300) {
            for (int y = 1000; y < 1500; y += 300) {
                uiDevice.click(x, y);
                pauseTestFor(300);
            }
        }
        onView(withId(R.id.addRestrictionButton)).perform(click());
        pauseTestFor(500);
        onView(withId(R.id.timeLimitParking)).perform(scrollTo(), click());
        onView(withId(R.id.timeLimitNumber)).perform(scrollTo(), typeText(String.valueOf("20")));
        closeSoftKeyboard();
        onView(withId(R.id.sunday)).perform(scrollTo(), click());
        onView(withId(R.id.allDay)).perform(scrollTo(), click());
        onView(withId(R.id.parallel)).perform(scrollTo(), click());
        onView(withId(R.id.submitButton)).perform(click());
        pauseTestFor(500);
    }

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

