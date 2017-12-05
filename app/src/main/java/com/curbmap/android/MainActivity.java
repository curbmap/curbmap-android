package com.curbmap.android;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.curbmap.android.fragments.AlarmFragment;
import com.curbmap.android.fragments.HelpFragment;
import com.curbmap.android.fragments.HomeFragment;
import com.curbmap.android.fragments.SettingsFragment;
import com.curbmap.android.fragments.UserFragment;
import com.curbmap.android.fragments.YourContributionsFragment;
import com.curbmap.android.fragments.YourPlacesFragment;
import com.curbmap.android.fragments.YourTimelineFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new UserFragment())
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
