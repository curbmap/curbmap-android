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

package com.curbmap.android.controller.handleTextRestriction;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.models.db.Restriction;

import static com.curbmap.android.controller.handleTextRestriction.HandleSubmit.getViewString;

public class CreateRestriction {
    final static String TAG = "CreateRestriction";

    /**
     * Creates a restriction object from the values in Add Restriction view
     *
     * @param view the add restriction view
     * @return Restriction object containing information from the view
     */
    static Restriction createRestriction(
            View view,
            String polylineString
    ) {
        final EditText startTimeObject = view.findViewById(R.id.fromTime);
        final EditText endTimeObject = view.findViewById(R.id.toTime);
        RadioGroup typeRadioGroup = view.findViewById(R.id.typeOfRestrictionRadioGroup);

        int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
        String type = "";
        String permitDistrict = "";
        if (selectedTypeId != -1) {
            RadioButton radioButton = typeRadioGroup.findViewById(selectedTypeId);
            type = radioButton.getText().toString();

            if (type.equals(getViewString(typeRadioGroup, R.string.custom_type))) {
                EditText customTypeText = view.findViewById(R.id.customTypeText);
                type = customTypeText.getText().toString();
                if (type.length() == 0) {
                    type = "undefined";
                }
            }

            if (type.equals(getViewString(view, R.string.permit_parking_districts))) {
                EditText permitText = view.findViewById(R.id.permitText);
                permitDistrict = permitText.getText().toString();
                if (permitDistrict.length() == 0) {
                    permitDistrict = "undefined";
                }
            }
        }

        EditText costObject = view.findViewById(R.id.cost);
        Double cost;
        if (costObject.getText().toString().equals("")) {
            cost = null;
        } else {
            cost = Double.parseDouble(costObject.getText().toString());
        }

        EditText perObject = view.findViewById(R.id.per);
        int per;
        if (perObject.getText().toString().equals("")) {
            per = -1;
        } else {
            per = Integer.parseInt(perObject.getText().toString());
        }

        EditText timeLimitObject = view.findViewById(R.id.timeLimitNumber);
        int time_limit;
        if (timeLimitObject.getText().toString().equals("")) {
            time_limit = -1;
        } else {
            time_limit = Integer.parseInt(timeLimitObject.getText().toString());
        }

        Spinner timeLimitSpinnerObject = view.findViewById(R.id.timeLimitSpinner);
        String timeLimitSpinner = timeLimitSpinnerObject.getSelectedItem().toString();
        if (time_limit != -1 && timeLimitSpinner.equals(getViewString(view, R.string.hrs))) {
            time_limit *= 60;
        }


        String length = "";
        RadioGroup lengthRadioGroup = view.findViewById(R.id.lengthOfRestrictionRadioGroup);
        int selectedLengthId = lengthRadioGroup.getCheckedRadioButtonId();
        if (selectedLengthId != -1) {
            RadioButton lengthRadioButton = view.findViewById(selectedLengthId);
            length = lengthRadioButton.getText().toString();
        }

        String start_time;
        String end_time;

        if (length.equals(getViewString(view, R.string.all_day))) {
            start_time = "00:00";
            end_time = "24:00";
        } else if (length.equals(getViewString(view, R.string.within_hours_of))) {


            if (startTimeObject.getText().toString().equals("")) {
                start_time = "undefined";
                Toast.makeText(view.getContext(),
                        HandleSubmit.getViewString(view, R.string.warning_no_from),
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                start_time = startTimeObject.getText().toString();
            }

            if (endTimeObject.getText().toString().equals("")) {
                end_time = "undefined";
                Toast.makeText(view.getContext(),
                        HandleSubmit.getViewString(view, R.string.warning_no_to),
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                end_time = endTimeObject.getText().toString();
            }
        } else {
            //todo: refactor this... the code is repetitive
            start_time = "error";
            end_time = "error";
        }

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
            angle = 45;
        } else if (angleString.equals(getViewString(view, R.string.head_in))) {
            angle = 90;
        } else {
            angle = -1;
        }

        String days = viewToDays(view);

        Double DEFAULT_COST = 0.0;
        if (cost == null) {
            cost = DEFAULT_COST;
        }

        int DEFAULT_TIME_LIMIT = 0;
        if (time_limit == -1) {
            time_limit = DEFAULT_TIME_LIMIT;
        }

        Restriction restriction = new Restriction(
                polylineString,
                type,
                days,
                start_time,
                end_time,
                angle,
                time_limit,
                cost,
                per,
                permitDistrict
        );

        return restriction;
    }
    //end of createRestriction()


    /**
     * Given the form for adding restrictions, give the string of days
     *
     * @param view The form for adding restrictions
     * @return 7 character string of days, 1 is checked day.
     * eg "1000000" is Sunday only checked
     */
    private static String viewToDays(View view) {

        CheckBox sunday = view.findViewById(R.id.sunday);
        CheckBox monday = view.findViewById(R.id.monday);
        CheckBox tuesday = view.findViewById(R.id.tuesday);
        CheckBox wednesday = view.findViewById(R.id.wednesday);
        CheckBox thursday = view.findViewById(R.id.thursday);
        CheckBox friday = view.findViewById(R.id.friday);
        CheckBox saturday = view.findViewById(R.id.saturday);

        StringBuilder days = new StringBuilder();
        boolean[] isCheckboxChecked = {sunday.isChecked(),
                monday.isChecked(),
                tuesday.isChecked(),
                wednesday.isChecked(),
                thursday.isChecked(),
                friday.isChecked(),
                saturday.isChecked()
        };

        for (boolean anIsCheckboxChecked : isCheckboxChecked) {
            if (anIsCheckboxChecked) days.append("1");
            else days.append("0");
        }

        return days.toString();
    }
}
