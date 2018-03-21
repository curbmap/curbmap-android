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

package com.curbmap.android.controller.handleRestrictionText;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.curbmap.android.R;
import com.curbmap.android.models.db.Polyline;
import com.curbmap.android.models.db.RestrictionText;
import com.curbmap.android.models.db.RestrictionTextInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.curbmap.android.controller.handleRestrictionText.HandleSubmit.getViewString;


public class CreateRestrictionText {
    final static String TAG = "CreateRestrictionText";


    @BindView(R.id.typeOfRestrictionSpinner)
    Spinner typeOfRestrictionSpinner;
    @BindView(R.id.costEditText)
    EditText costEditText;
    @BindView(R.id.perEditText)
    EditText perEditText;
    @BindView(R.id.timeLimitNumberEditText)
    EditText timeLimitNumberEditText;
    @BindView(R.id.timeLimitSpinner)
    Spinner timeLimitSpinner;
    @BindView(R.id.customTypeText)
    EditText customTypeText;
    @BindView(R.id.permitText)
    EditText permitText;


    @BindView(R.id.lengthOfRestrictionRadioGroup)
    RadioGroup lengthOfRestrictionRadioGroup;
    @BindView(R.id.fromTimeEditText)
    EditText fromTimeEditText;
    @BindView(R.id.toTimeEditText)
    EditText toTimeEditText;
    @BindView(R.id.compassDirSpinner)
    Spinner compassDirSpinner;
    @BindView(R.id.vehicleTypeSpinner)
    Spinner vehicleTypeSpinner;
    @BindView(R.id.parkingAngleRadioGroup)
    RadioGroup parkingAngleRadioGroup;

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

    @BindView(R.id.week1)
    CheckBox week1;
    @BindView(R.id.week2)
    CheckBox week2;
    @BindView(R.id.week3)
    CheckBox week3;
    @BindView(R.id.week4)
    CheckBox week4;


    @BindView(R.id.month01)
    CheckBox month01;
    @BindView(R.id.month02)
    CheckBox month02;
    @BindView(R.id.month03)
    CheckBox month03;
    @BindView(R.id.month04)
    CheckBox month04;
    @BindView(R.id.month05)
    CheckBox month05;
    @BindView(R.id.month06)
    CheckBox month06;
    @BindView(R.id.month07)
    CheckBox month07;
    @BindView(R.id.month08)
    CheckBox month08;
    @BindView(R.id.month09)
    CheckBox month09;
    @BindView(R.id.month10)
    CheckBox month10;
    @BindView(R.id.month11)
    CheckBox month11;
    @BindView(R.id.month12)
    CheckBox month12;


    /**
     * Creates a RestrictionText object from the values in the Add Restriction view
     *
     * @param view the add restriction view
     * @return RestrictionText object containing information from the view
     */
    RestrictionText createRestrictionText(
            View view,
            String polylineString
    ) {
        Unbinder unbinder = ButterKnife.bind(this, view);

        int type = typeOfRestrictionSpinner.getSelectedItemPosition();

        int angle;
        String angleString = "";
        RadioGroup angleRadioGroup = view.findViewById(R.id.parkingAngleRadioGroup);
        int selectedAngleId = angleRadioGroup.getCheckedRadioButtonId();
        if (selectedAngleId != -1) {
            RadioButton angleRadioButton = view.findViewById(selectedAngleId);
            angleString = angleRadioButton.getText().toString();
        }
        if (angleString.equals(getViewString(view, R.string.parallel))) {
            angle = 0;
        } else if (angleString.equals(getViewString(view, R.string.angled))) {
            angle = 3;
        } else if (angleString.equals(getViewString(view, R.string.head_in))) {
            angle = 1;
        } else {
            Log.e(TAG, "No angle selected");
            angle = -1;
        }

        String start_time;
        String end_time;

        String length = "";
        int selectedLengthId = lengthOfRestrictionRadioGroup.getCheckedRadioButtonId();
        if (selectedLengthId != -1) {
            //finds which button was selected: "all day" or "within hours of"
            RadioButton lengthRadioButton = view.findViewById(selectedLengthId);
            length = lengthRadioButton.getText().toString();
        }
        if (length.equals(getViewString(view, R.string.all_day))) {
            start_time = "00:00";
            end_time = "24:00";
        } else if (length.equals(getViewString(view, R.string.within_hours_of))) {
            start_time = fromTimeEditText.getText().toString();
            end_time = toTimeEditText.getText().toString();
        } else {
            start_time = "00:00";
            end_time = "24:00";
            Log.e(TAG, "undefined length of restriction");
        }

        int start = timeToMinutes(start_time);
        int end = timeToMinutes(end_time);


        boolean[] days = this.viewToDays(view);
        boolean[] weeks = this.viewToWeeks(view);
        boolean[] months = this.viewToMonths(view);


        int limit;
        if (timeLimitNumberEditText.getText().toString().equals("")) {
            limit = -1;
            Log.e(TAG, "Time limit was not selected");
        } else {
            limit = Integer.parseInt(timeLimitNumberEditText.getText().toString());
        }

        //if hours is chosen for time limit units
        // then multiply our time limit number by 60 to achieve minutes magnitude
        String timeLimitSpinnerString = timeLimitSpinner.getSelectedItem().toString();
        if (limit != -1 && timeLimitSpinnerString.equals(
                getViewString(view, R.string.hrs))) {
            limit *= 60;
        }

        String permit = permitText.getText().toString();

        float cost;
        float DEFAULT_COST = 0;
        if (costEditText.getText().toString().equals("")) {
            cost = DEFAULT_COST;
        } else {
            cost = Float.parseFloat(costEditText.getText().toString());
        }

        int per;
        if (perEditText.getText().toString().equals("")) {
            per = -1;
        } else {
            per = Integer.parseInt(perEditText.getText().toString());
        }

        //subtract one because vehicle type is indexed starting from -1
        int vehicle = vehicleTypeSpinner.getSelectedItemPosition() - 1;

        int side = compassDirSpinner.getSelectedItemPosition();

        Gson gson = new Gson();
        Polyline polyline = gson.fromJson(polylineString, Polyline.class);
        ArrayList<double[]> coordinates = polyline.getAsCoordinates();

        RestrictionTextInfo restrictionTextInfo = new RestrictionTextInfo(
                type,
                angle,
                start,
                end,
                days,
                weeks,
                months,
                limit,
                permit,
                cost,
                per,
                vehicle,
                side
        );

        RestrictionText restrictionText = new RestrictionText(
                coordinates,
                restrictionTextInfo
        );

        unbinder.unbind();
        return restrictionText;


    }

    //end of createRestrictionText()


    /**
     * Given a view containing the add restriction form
     * Return the array of days where the restriction is effective
     *
     * @param view the add restriction form view
     * @return array of days where true indicates the restriction is effective
     * days start on Monday
     */
    private boolean[] viewToDays(View view) {
        Unbinder unbinder = ButterKnife.bind(this, view);
        boolean[] days = {
                monday.isChecked(),
                tuesday.isChecked(),
                wednesday.isChecked(),
                thursday.isChecked(),
                friday.isChecked(),
                saturday.isChecked(),
                sunday.isChecked()
        };
        unbinder.unbind();
        return days;
    }


    /**
     * Given a view returns array of weeks when
     * the restriction is in effect
     *
     * @param view the add restriction form view
     * @return the array of weeks of month starting on first week of the month
     */
    private boolean[] viewToWeeks(View view) {
        Unbinder unbinder = ButterKnife.bind(this, view);
        boolean[] weeks = {
                week1.isChecked(),
                week2.isChecked(),
                week3.isChecked(),
                week4.isChecked()
        };
        unbinder.unbind();
        return weeks;
    }


    /**
     * Given a filled add restriction view returns array of months when the restriction is in effect
     *
     * @param view the filled add restriction view
     * @return array of the months when the restriction is in effect
     */
    private boolean[] viewToMonths(View view) {
        Unbinder unbinder = ButterKnife.bind(this, view);
        boolean[] months = {
                month01.isChecked(),
                month02.isChecked(),
                month03.isChecked(),
                month04.isChecked(),
                month05.isChecked(),
                month06.isChecked(),
                month07.isChecked(),
                month08.isChecked(),
                month09.isChecked(),
                month10.isChecked(),
                month11.isChecked(),
                month12.isChecked(),
        };
        unbinder.unbind();
        return months;
    }

    /**
     * Given a time in the format of a string in 24 hour time
     * For example, "04:23"
     * We return the number of minutes since midnight that the time
     * represents
     * the result will range in [0,1439] as there are 1440 minutes in a day
     *
     * @param time 24 hour time in the format HH:MM
     * @return time in the format of integer minutes since midnight
     */
    private int timeToMinutes(String time) {
        String[] timeElements = time.split(":");
        int hour = Integer.parseInt(timeElements[0]);
        int minutes = Integer.parseInt(timeElements[1]);
        minutes += hour * 60;
        return minutes;
    }

}


