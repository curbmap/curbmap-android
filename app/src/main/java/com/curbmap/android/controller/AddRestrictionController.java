package com.curbmap.android.controller;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.curbmap.android.R;
import com.curbmap.android.models.db.RestrictionDao;

public class AddRestrictionController {

    public static boolean submitAddRestriction(
            View view,
            View parentView,
            String coordinatesOfRestriction,
            String endCoordinates) {

        //although we restrictionDao as null, it should work properly.
        //...this is just an intricacy of Room.
        RestrictionDao restrictionDao = null;

        String TAG = "AddRestrictionCon";
        final EditText startTimeObject = parentView.findViewById(R.id.fromTime);
        final EditText endTimeObject = parentView.findViewById(R.id.toTime);
        RadioGroup typeRadioGroup = parentView.findViewById(R.id.typeOfRestrictionRadioGroup);

        int selectedId = typeRadioGroup.getCheckedRadioButtonId();


        String type = "";
        String permitDistrict = "";
        if (selectedId != -1) {
            RadioButton radioButton = parentView.findViewById(selectedId);
            type = radioButton.getText().toString();

            if (type.equals(getViewString(parentView, R.string.custom_type))) {
                EditText customTypeText = parentView.findViewById(R.id.customTypeText);
                type = customTypeText.getText().toString();
                if (type.length() == 0) {
                    type = "undefined";
                }
            }

            if (type.equals(getViewString(parentView, R.string.permit_parking_districts))) {
                EditText permitText = parentView.findViewById(R.id.permitText);
                permitDistrict = permitText.getText().toString();
                if (permitDistrict.length() == 0) {
                    permitDistrict = "undefined";
                }
            }
        }

        EditText costObject = parentView.findViewById(R.id.cost);
        Double cost;
        if (costObject.getText().toString().equals("")) {
            cost = null;
        } else {
            cost = Double.parseDouble(costObject.getText().toString());
        }

        EditText perObject = parentView.findViewById(R.id.per);
        int per;
        if (perObject.getText().toString().equals("")) {
            per = -1;
        } else {
            per = Integer.parseInt(perObject.getText().toString());
        }

        EditText timeLimitObject = parentView.findViewById(R.id.timeLimitNumber);
        int time_limit;
        if (timeLimitObject.getText().toString().equals("")) {
            time_limit = -1;
        } else {
            time_limit = Integer.parseInt(timeLimitObject.getText().toString());
        }

        Spinner timeLimitSpinnerObject = parentView.findViewById(R.id.timeLimitSpinner);
        String timeLimitSpinner = timeLimitSpinnerObject.getSelectedItem().toString();
        if (time_limit != -1 && timeLimitSpinner.equals(getViewString(parentView, R.string.hrs))) {
            time_limit *= 60;
        }

        CheckBox sunday = parentView.findViewById(R.id.sunday);
        CheckBox monday = parentView.findViewById(R.id.monday);
        CheckBox tuesday = parentView.findViewById(R.id.tuesday);
        CheckBox wednesday = parentView.findViewById(R.id.wednesday);
        CheckBox thursday = parentView.findViewById(R.id.thursday);
        CheckBox friday = parentView.findViewById(R.id.friday);
        CheckBox saturday = parentView.findViewById(R.id.saturday);

        String days = "";

        if (sunday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        if (monday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        if (tuesday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        if (wednesday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        if (thursday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        if (friday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        if (saturday.isChecked()) {
            days += "1";
        } else {
            days += "0";
        }

        String length = "";
        RadioGroup lengthRadioGroup = parentView.findViewById(R.id.lengthOfRestrictionRadioGroup);
        int selectedLengthId = lengthRadioGroup.getCheckedRadioButtonId();
        if (selectedLengthId != -1) {
            RadioButton lengthRadioButton = parentView.findViewById(selectedLengthId);
            length = lengthRadioButton.getText().toString();
        }

        String start_time;
        String end_time;


        Log.d(TAG, type);
        if (length.equals(getViewString(parentView, R.string.all_day))) {
            start_time = "00:00";
            end_time = "24:00";
        } else {


            if (startTimeObject.getText().toString().equals("")) {
                start_time = "undefined";
            } else {
                start_time = startTimeObject.getText().toString();
            }

            if (endTimeObject.getText().toString().equals("")) {
                end_time = "undefined";
            } else {
                end_time = endTimeObject.getText().toString();
            }
        }


        int angle;
        String angleString = "";
        RadioGroup angleRadioGroup = parentView.findViewById(R.id.parkingAngleRadioGroup);
        int selectedAngleId = angleRadioGroup.getCheckedRadioButtonId();
        if (selectedAngleId != -1) {
            RadioButton angleRadioButton = parentView.findViewById(selectedAngleId);
            angleString = angleRadioButton.getText().toString();
        }
        if (angleString.equals(getViewString(parentView, R.string.parallel))) {
            angle = 0;
        } else if (angleString.equals(getViewString(parentView, R.string.angled))) {
            angle = 45;
        } else if (angleString.equals(getViewString(parentView, R.string.head_in))) {
            angle = 90;
        } else {
            angle = -1;
        }

        return AddRestrictionCheckExceptions.checkExceptions(
                coordinatesOfRestriction,
                view,
                parentView,
                selectedId,
                type,
                cost,
                angle,
                permitDistrict,
                per,
                days,
                start_time,
                end_time,
                restrictionDao,
                time_limit,
                endCoordinates,
                selectedLengthId,
                selectedAngleId,
                length
        );
    }

    public static String getViewString(View view, int id) {
        return view.getContext().getResources().getString(id);
    }

}
