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

import com.curbmap.android.fragments.user.UserSigninFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests whether signing in the user works
 * with fake username and password, make sure it does not crash the app
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignInTest {
    static final String TAG = "SignInTest";
    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(MainActivity.class,
                    false,
                    false) {
            };
    FragmentManager fragmentManager;
    private UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

    /**
     * launches the Sign In fragment
     */
    @Before
    public void initActivity() {
        Activity a = mActivityRule.launchActivity(new Intent());
        fragmentManager = a.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new UserSigninFragment())
                .commit();
    }

    /**
     * Tests the log in functionality
     */
    @Test
    public void signInUserTest() throws Exception {
        onView(withId(R.id.usernameField)).perform(typeText(String.valueOf("username")));
        closeSoftKeyboard();
        onView(withId(R.id.passwordField)).perform(typeText(String.valueOf("password")));
        closeSoftKeyboard();
        onView(withId(R.id.signInButton)).perform(click());
        Thread.sleep(5000);
    }

}

