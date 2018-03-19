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

import com.curbmap.android.fragments.SettingsFragment;

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
public class SettingsTest {
    static final String TAG = "SettingsTest";
    FragmentManager fragmentManager;
    private UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(MainActivity.class,
                    false,
                    false) {
            };

    /**
     * launches the Settings fragment
     */
    @Before
    public void initActivity() {
        Activity a = mActivityRule.launchActivity(new Intent());
        fragmentManager = a.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new SettingsFragment())
                .commit();
    }

    /**
     * Selects every single option in the Settings fragment one at at time
     * @throws Exception
     */
    @Test
    public void setTimer() throws Exception {
        onView(withId(R.id.allowPushNotifications)).perform(click(),click());
        onView(withId(R.id.allowSounds)).perform(click(),click());
        onView(withId(R.id.locationSharing)).perform(click(),click());
        onView(withId(R.id.voiceSearch)).perform(click(),click());
        onView(withId(R.id.offlineMaps)).perform(click(),click());
    }

}

