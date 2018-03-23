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

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.curbmap.android.controller.HandShaker;
import com.curbmap.android.controller.handleupload.UploadHandler;
import com.curbmap.android.fragments.AlarmFragment;
import com.curbmap.android.fragments.HelpFragment;
import com.curbmap.android.fragments.HomeFragment;
import com.curbmap.android.fragments.SettingsFragment;
import com.curbmap.android.fragments.YourContributionsFragment;
import com.curbmap.android.fragments.YourPlacesFragment;
import com.curbmap.android.fragments.user.UserProfileFragment;
import com.curbmap.android.fragments.user.UserSigninFragment;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.UserAccessor;

/**
 * Handle the navigation view because it runs in all the other fragments.
 * So instead of putting it in its own fragment, we put all the logic in the
 * MainActivity. Ideally we would take out the logic for navigation view into a
 * separate class but it was hard to do, so when we finally figure out how to do that
 * we will take it out and put it in a separate file.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    AppDatabase userAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        userAppDatabase = AppDatabase.getUserAppDatabase(context);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new HomeFragment())
                .commit();

        HandShaker.handShake(context);
        UploadHandler.initiateHandler(context);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment nextFragment;

        if (id == R.id.nav_home) {
            nextFragment = new HomeFragment();
        } else if (id == R.id.nav_alarm) {
            nextFragment = new AlarmFragment();
        } else if (id == R.id.nav_your_places) {
            nextFragment = new YourPlacesFragment();
        } else if (id == R.id.nav_your_contributions) {
            nextFragment = new YourContributionsFragment();
        } else if (id == R.id.nav_settings) {
            nextFragment = new SettingsFragment();
        } else if (id == R.id.nav_help) {
            nextFragment = new HelpFragment();
        } else if (id == R.id.nav_user) {
            if (!UserAccessor.isLoggedIn(userAppDatabase)) {
                Log.d(TAG, "User is not logged in");
                nextFragment = new UserSigninFragment();
            } else {
                Log.d(TAG, "User is logged in");
                nextFragment = new UserProfileFragment();
            }
        } else {
            nextFragment = new HomeFragment();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , nextFragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        userAppDatabase.destroyInstance();
        super.onDestroy();
    }
}