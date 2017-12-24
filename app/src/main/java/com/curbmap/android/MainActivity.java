package com.curbmap.android;

import android.app.FragmentManager;
import android.arch.persistence.room.Room;
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
import com.curbmap.android.fragments.YourTimelineFragment;
import com.curbmap.android.fragments.user.UserProfileFragment;
import com.curbmap.android.fragments.user.UserSigninFragment;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAppDatabase;
import com.curbmap.android.models.db.UserDao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new HomeFragment())
                .commit();



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

        } else if (id == R.id.nav_your_timeline) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new YourTimelineFragment())
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
            UserAppDatabase db = Room.databaseBuilder(
                    getApplicationContext(),
                    UserAppDatabase.class,
                    "user")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            UserDao userDao = db.getUserDao();
            User user = userDao.getUser();
            if (user == null) {
                Log.d(TAG, "user is not logged in");

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new UserSigninFragment())
                        .commit();
            } else {
                Log.d(TAG, user.toString());

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


}
