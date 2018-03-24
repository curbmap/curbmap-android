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

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.controller.handleRestrictionText.HandleSubmit;
import com.curbmap.android.models.lib.SetTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * The fragment for adding a restriction using the
 * manual restriction description form
 * as opposed to simply taking a photo of the restriction
 */
public class AddRestrictionFragment extends Fragment {
    View myView;
    String polylineString;

    @BindView(R.id.submitButton)
    Button submitButton;
    @BindView(R.id.fromTimeEditText)
    EditText fromTime;
    @BindView(R.id.toTimeEditText)
    EditText toTime;

    /**
     * The unbinders are used to unbind butterknife on destruction
     * We have two unbinders because the second one might never be instantiated
     * unbinderAllDays is only instantiated if
     */
    private Unbinder unbinder;
    private String TAG = "AddRestrictionFragment";

    @OnClick(R.id.menu_icon)
    public void openMenu(View view) {
        Log.e(TAG, "menu was opened");
        new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout)
                        getActivity()
                                .getWindow()
                                .getDecorView()
                                .findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_add_restriction, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        unbinder = ButterKnife.bind(this, myView);

        this.polylineString = getArguments().getString("polylineString");

        //lets us use clock to set start and end time instead of just typing them in
        //these variables are not used but it instantiates the instance
        //so do not delete them
        SetTime startTime = new SetTime(fromTime);
        SetTime endTime = new SetTime(toTime);

        return myView;
    }

    @OnClick(R.id.submitButton)
    public void setSubmitButton(View view) {
        View parentView = (View) view.getParent();

        if (HandleSubmit.submitAddRestriction(
                parentView,
                polylineString)) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new HomeFragment())
                    .commit();

            Toast.makeText(view.getContext(),
                    getString(R.string.success_restriction_added),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}