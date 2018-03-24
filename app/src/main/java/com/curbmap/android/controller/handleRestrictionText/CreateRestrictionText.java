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

import static com.curbmap.android.controller.handleRestrictionText.HandleSubmit.getViewString;


public class CreateRestrictionText {
    final static String TAG = "CreateRestrictionText";

    /**
     * Creates a RestrictionText object from the values in the Add Restriction view
     *
     * @param view the add restriction view
     * @return RestrictionText object containing information from the view
     */
    static RestrictionText createRestrictionText(
            View view,
            String polylineString
    ) {

        Spinner typeOfRestrictionSpinner = view.findViewById(R.id.typeOfRestrictionSpinner);
        EditText costEditText = view.findViewById(R.id.costEditText);
        EditText perEditText = view.findViewById(R.id.perEditText);
        EditText timeLimitNumberEditText = view.findViewById(R.id.timeLimitNumberEditText);
        Spinner timeLimitSpinner = view.findViewById(R.id.timeLimitSpinner);
        EditText customTypeText = view.findViewById(R.id.customTypeText);
        EditText permitText = view.findViewById(R.id.permitText);
        RadioGroup lengthOfRestrictionRadioGroup = view.findViewById(R.id.lengthOfRestrictionRadioGroup);
        EditText fromTimeEditText = view.findViewById(R.id.fromTimeEditText);
        EditText toTimeEditText = view.findViewById(R.id.toTimeEditText);
        Spinner compassDirSpinner = view.findViewById(R.id.compassDirSpinner);
        Spinner vehicleTypeSpinner = view.findViewById(R.id.vehicleTypeSpinner);
        RadioGroup parkingAngleRadioGroup = view.findViewById(R.id.parkingAngleRadioGroup);
        RadioGroup holidayRadioGroup = view.findViewById(R.id.holidayRadioGroup);

        int type = typeOfRestrictionSpinner.getSelectedItemPosition();

        int angle;
        String angleString = "";
        int selectedAngleId = parkingAngleRadioGroup.getCheckedRadioButtonId();
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


        boolean holiday = false;
        String holidayString = "";
        int selectedHolidayId = holidayRadioGroup.getCheckedRadioButtonId();
        if (selectedHolidayId != -1) {
            RadioButton holidayButton = view.findViewById(selectedHolidayId);
            holidayString = holidayButton.getText().toString();
        }
        if (holidayString.equals(getViewString(view, R.string.is_a_holiday_related_restriction))) {
            holiday = true;
        } else if (holidayString.equals(getViewString(view, R.string.not_holiday_description))) {
            holiday = false;
        } else {
            //this should never be called because 'not a holiday' is selected by default
            //and since this is a radio group it should be impossible to have no selections
            //given that we start off with a default selection
            Log.e(TAG, "No holiday selected");
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


        boolean[] days = viewToDays(view);
        boolean[] weeks = viewToWeeks(view);
        boolean[] months = viewToMonths(view);


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
        ArrayList<double[]> tempcoordinates = polyline.getAsCoordinates();
        //switch lat,long to long,lat because server is using long,lat
        ArrayList<double[]> coordinates = new ArrayList<>();
        for (double[] c : tempcoordinates) {
            double[] current = new double[]{c[1],c[0]};
            coordinates.add(current);
        }

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
                side,
                holiday
        );

        RestrictionText restrictionText = new RestrictionText(
                coordinates,
                restrictionTextInfo
        );

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
    private static boolean[] viewToDays(View view) {
        CheckBox sunday = view.findViewById(R.id.sunday);
        CheckBox monday = view.findViewById(R.id.monday);
        CheckBox tuesday = view.findViewById(R.id.tuesday);
        CheckBox wednesday = view.findViewById(R.id.wednesday);
        CheckBox thursday = view.findViewById(R.id.thursday);
        CheckBox friday = view.findViewById(R.id.friday);
        CheckBox saturday = view.findViewById(R.id.saturday);

        boolean[] days = {
                monday.isChecked(),
                tuesday.isChecked(),
                wednesday.isChecked(),
                thursday.isChecked(),
                friday.isChecked(),
                saturday.isChecked(),
                sunday.isChecked()
        };
        return days;
    }


    /**
     * Given a view returns array of weeks when
     * the restriction is in effect
     *
     * @param view the add restriction form view
     * @return the array of weeks of month starting on first week of the month
     */
    private static boolean[] viewToWeeks(View view) {
        CheckBox week1 = view.findViewById(R.id.week1);
        CheckBox week2 = view.findViewById(R.id.week2);
        CheckBox week3 = view.findViewById(R.id.week3);
        CheckBox week4 = view.findViewById(R.id.week4);

        boolean[] weeks = {
                week1.isChecked(),
                week2.isChecked(),
                week3.isChecked(),
                week4.isChecked()
        };
        return weeks;
    }


    /**
     * Given a filled add restriction view returns array of months when the restriction is in effect
     *
     * @param view the filled add restriction view
     * @return array of the months when the restriction is in effect
     */
    private static boolean[] viewToMonths(View view) {
        CheckBox month01 = view.findViewById(R.id.month01);
        CheckBox month02 = view.findViewById(R.id.month02);
        CheckBox month03 = view.findViewById(R.id.month03);
        CheckBox month04 = view.findViewById(R.id.month04);
        CheckBox month05 = view.findViewById(R.id.month05);
        CheckBox month06 = view.findViewById(R.id.month06);
        CheckBox month07 = view.findViewById(R.id.month07);
        CheckBox month08 = view.findViewById(R.id.month08);
        CheckBox month09 = view.findViewById(R.id.month09);
        CheckBox month10 = view.findViewById(R.id.month10);
        CheckBox month11 = view.findViewById(R.id.month11);
        CheckBox month12 = view.findViewById(R.id.month12);

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
    private static int timeToMinutes(String time) {
        String[] timeElements = time.split(":");
        int hour = Integer.parseInt(timeElements[0]);
        int minutes = Integer.parseInt(timeElements[1]);
        minutes += hour * 60;
        return minutes;
    }

}


