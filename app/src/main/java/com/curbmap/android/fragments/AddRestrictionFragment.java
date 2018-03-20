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

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.controller.handleTextRestriction.HandleSubmit;
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
    @BindView(R.id.fromTime)
    EditText fromTime;
    @BindView(R.id.customTypeText)
    EditText customTypeText;
    @BindView(R.id.toTime)
    EditText toTime;
    @BindView(R.id.typeOfRestrictionRadioGroup)
    RadioGroup typeOfRestrictionRadioGroup;
    @BindView(R.id.allDay)
    RadioButton allDay;

    @BindView(R.id.sunday)
    CheckBox sunday;
    @BindView(R.id.monday)
    CheckBox monday;
    @BindView(R.id.tuesday)
    CheckBox tuesday;
    @BindView(R.id.wednesday)
    CheckBox wednesday;
    @BindView(R.id.thursday)
    CheckBox thursday;
    @BindView(R.id.friday)
    CheckBox friday;
    @BindView(R.id.saturday)
    CheckBox saturday;


    /**
     * The unbinders are used to unbind butterknife on destruction
     * We have two unbinders because the second one might never be instantiated
     * unbinderAllDays is only instantiated if
     */
    private Unbinder unbinder;
    private Unbinder unbinderAllDays;
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

    /**
     * Given a view with the CheckBoxes names sunday through saturday
     * Check all of the boxes in the view.
     * For example, when we select a fire hydrant, we call selectAllDays(View v)
     * so that all days are selected
     * We have to send in the parentview and not the
     *
     * @param view The view which contains the checkboxes
     */
    private void selectAllDays(View view) {

        unbinderAllDays = ButterKnife.bind(this, myView);
        sunday.setChecked(true);
        monday.setChecked(true);
        tuesday.setChecked(true);
        wednesday.setChecked(true);
        thursday.setChecked(true);
        friday.setChecked(true);
        saturday.setChecked(true);
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

        customTypeText.setVisibility(View.INVISIBLE);
        typeOfRestrictionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.fireHydrant:
                        selectAllDays(myView);
                        allDay.setChecked(true);
                        break;
                    case R.id.customTypeLabel:
                        customTypeText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
        if (unbinderAllDays != null) {
            unbinderAllDays.unbind();
        }
    }
}