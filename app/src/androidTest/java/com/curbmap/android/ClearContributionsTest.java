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

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.curbmap.android.fragments.YourContributionsFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests clearing contributions by clicking the clear contributions button
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClearContributionsTest {
    static final String TAG = "ClearContributionsTest";
    FragmentManager fragmentManager;
    private UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(MainActivity.class,
                    false,
                    false) {
            };

    /**
     * initiates the activity
     * now we have the activity stored in a global variable
     * so we can do all sorts of things with the activity variable,
     * most importantly, being able to call the fragmentmanager
     * and manipulate the fragment which is being displayed
     * because this allows us to test the various fragments
     *
     * Launches the contribution fragment
     */
    @Before
    public void initActivity() {
        Activity a = mActivityRule.launchActivity(new Intent());
        fragmentManager = a.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new YourContributionsFragment())
                .commit();
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearContributions() throws Exception {
        onView(withId(R.id.clearContributionsButton)).perform(click());
        UiObject okButton = uiDevice.findObject(
                new UiSelector().text("OK")
        );
        if (okButton.exists() && okButton.isEnabled()) {
            okButton.click();
        }

    }




}

