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

import com.curbmap.android.fragments.user.UserSignupFragment;

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
 * Tests whether signing up the user works
 * with fake username and password, make sure it does not crash the app
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpTest {
    static final String TAG = "SignUpTest";
    FragmentManager fragmentManager;
    private UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(MainActivity.class,
                    false,
                    false) {
            };

    /**
     * launches the Sign Up fragment
     */
    @Before
    public void initActivity() {
        Activity a = mActivityRule.launchActivity(new Intent());
        fragmentManager = a.getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new UserSignupFragment())
                .commit();
    }

    /**
     * Tests the sign up functionality
     * should show error of password does not meet criteria
     */
    @Test
    public void signUpUserTestPasswordComplexity() throws Exception {
        onView(withId(R.id.usernameField)).perform(typeText(String.valueOf("username")));
        closeSoftKeyboard();
        onView(withId(R.id.passwordField)).perform(typeText(String.valueOf("password")));
        closeSoftKeyboard();
        onView(withId(R.id.emailField)).perform(typeText(String.valueOf("email@example.com")));
        closeSoftKeyboard();
        onView(withId(R.id.confirmEmailField)).perform(
                typeText(String.valueOf("email@example.com"))
        );
        closeSoftKeyboard();

        onView(withId(R.id.signUpButton)).perform(click());
        Thread.sleep(5000);
    }

    /**
     * Tests the sign up functionality
     * should work
     */
    @Test
    public void signUpUserTest() throws Exception {
        onView(withId(R.id.usernameField)).perform(typeText(String.valueOf("username")));
        closeSoftKeyboard();
        onView(withId(R.id.passwordField)).perform(typeText(String.valueOf("Password123!")));
        closeSoftKeyboard();
        onView(withId(R.id.emailField)).perform(typeText(String.valueOf("email@example.com")));
        closeSoftKeyboard();
        onView(withId(R.id.confirmEmailField)).perform(
                typeText(String.valueOf("email@example.com"))
        );
        closeSoftKeyboard();

        onView(withId(R.id.signUpButton)).perform(click());
        Thread.sleep(5000);
    }

}

