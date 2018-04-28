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

package com.curbmap.android.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.curbmap.android.R;
import com.curbmap.android.models.db.Settings;
import com.curbmap.android.models.viewmodel.SettingsViewModel;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * The fragment for displaying all the settings options.
 */
public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    View myView;
    private Unbinder unbinder;


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    SettingsViewModel settingsViewModel;


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        settingsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SettingsViewModel.class);

        settingsViewModel.getSettings().observe(this, new Observer<Settings>() {
            @Override
            public void onChanged(@Nullable Settings settings) {
                if(settings != null) {
                    Log.d(TAG, "settings onChanged was called");
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, myView);

        return myView;
    }


    @OnClick(R.id.menu_icon)
    public void openMenu(View view) {
        DrawerLayout drawer = (DrawerLayout)
                getActivity()
                        .getWindow()
                        .getDecorView()
                        .findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    //when the uploadOverWifi switcher is changed,
    // we just log the event and not do anything yet
    @OnCheckedChanged(R.id.uploadOverWifi)
    public void switchUploadOverWifi(CompoundButton button,boolean uploadOverWifi) {
        //todo: implement updating value of uploadOverWifi in our Settings object in our database
        Log.d(TAG, "switchUploadOverWifi was called");
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
