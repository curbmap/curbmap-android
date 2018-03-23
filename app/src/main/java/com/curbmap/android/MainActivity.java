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

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.curbmap.android.fragments.AlarmFragment;
import com.curbmap.android.fragments.HelpFragment;
import com.curbmap.android.fragments.HomeFragment;
import com.curbmap.android.fragments.SettingsFragment;
import com.curbmap.android.fragments.YourContributionsFragment;
import com.curbmap.android.fragments.YourPlacesFragment;
import com.curbmap.android.fragments.user.UserProfileFragment;
import com.curbmap.android.fragments.user.UserSigninFragment;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAccessor;
import com.curbmap.android.models.db.UserAuth;
import com.curbmap.android.models.db.UserAuthAccessor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    //we have to initialize the userAuthAppDatabase
    // on create, because initializing the database
    // is an asynchronous operation that takes time and runs off the main thread
    // so the only way to use the appdatabase is by launching it first
    // but ... this is only a temporary solution until we find out
    // how to run handshake() only after appdatabase is initialized...
    AppDatabase userAuthAppDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        userAppDatabase = AppDatabase.getUserAppDatabase(context);
        userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(context);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new HomeFragment())
                .commit();


        handShake(context);


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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new HomeFragment())
                    .commit();
        } else if (id == R.id.nav_alarm) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new AlarmFragment())
                    .commit();

        } else if (id == R.id.nav_your_places) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new YourPlacesFragment())
                    .commit();

        } else if (id == R.id.nav_your_contributions) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new YourContributionsFragment())
                    .commit();

        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new SettingsFragment())
                    .commit();

        } else if (id == R.id.nav_help) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new HelpFragment())
                    .commit();

        } else if (id == R.id.nav_user) {
            if (!UserAccessor.isLoggedIn(userAppDatabase)) {
                Log.d(TAG, "User is not logged in");

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new UserSigninFragment())
                        .commit();
            } else {
                Log.d(TAG, "User is logged in");
                // Log.d(TAG, UserAccessor.getString(userAppDatabase));
                //  Log.d("user-session", UserAccessor.getUser(userAppDatabase).getSession());

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new UserProfileFragment())
                        .commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //todo: check whether user database will be destroyed properly
    //... currently displays error message which says
    // static member is accessed via instance reference...
    @Override
    protected void onDestroy() {
        userAppDatabase.destroyInstance();
        super.onDestroy();
    }

    /**
     * Every time the function is called
     * check whether its time to shake hands
     * if it is, then run handshake
     */
    private void handShake(Context context) {

        AppDatabase userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(
                context);
        UserAuth userAuth = UserAuthAccessor.getUserAuth(userAuthAppDatabase);
        if(userAuth != null) {
            if (userAuth.shouldWeHandShakeRightNow()) {
                //perform handshake
                //send login request to server
                final String BASE_URL = getString(R.string.BASE_URL_API);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                String username = userAuth.getUsername();
                String password = userAuth.getPassword();

                CurbmapRestService service = retrofit.create(CurbmapRestService.class);
                Call<User> results = service.doLoginPOST(
                        username,
                        password);

                results.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "successfully signed in through handshake to renew session token");

                            AppDatabase userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(
                                    getApplicationContext());

                            UserAuthAccessor.updateUserAuth(userAuthAppDatabase);

                            AppDatabase userAppDatabase = AppDatabase.getUserAppDatabase(
                                    getApplicationContext());
                            UserAccessor.deleteUser(userAppDatabase);

                            //update user object
                            //so not only the session token is renewed
                            //but also any other updates from the server will be received
                            User user = response.body();
                            UserAccessor.insertUser(userAppDatabase, user);


                        } else {
                            Log.e(TAG, "failed to sign in for handshake to renew session token");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, getString(R.string.fail_signin));
                        t.printStackTrace();
                    }
                });
            } else {
                Log.d(TAG, "We do not need to handshake.");
            }
        } else {
            Log.e(TAG, "userAuth was null so could not perform handshake");
        }

    }

}
