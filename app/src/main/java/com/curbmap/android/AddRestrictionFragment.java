package com.curbmap.android;

import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddRestrictionFragment extends Fragment {
    View myView;

    RestrictionDao restrictionDao;
    private String TAG = "AddRestrictionFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.add_restriction, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        Button submitButton = (Button) myView.findViewById(R.id.submitButton);
        Log.d(TAG, submitButton.toString());

        final String coordinatesOfRestriction = getArguments().getString("coordinatesOfRestriction");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View parentView = (View) view.getParent();
                RadioGroup typeRadioGroup = (RadioGroup) parentView.findViewById(R.id.typeOfRestrictionRadioGroup);
                Log.d(TAG, typeRadioGroup.toString());

                int selectedId = typeRadioGroup.getCheckedRadioButtonId();
                Log.d(TAG, Integer.toString(selectedId));

                String type;
                if (selectedId == -1) {
                    Toast.makeText(view.getContext(), "You must select a type of restriction.", Toast.LENGTH_SHORT).show();
                    type = "no type selected";
                } else {
                    RadioButton radioButton = (RadioButton) parentView.findViewById(selectedId);
                    type = radioButton.getText().toString();
                }

                EditText costObject = (EditText) parentView.findViewById(R.id.cost);
                Double cost;
                cost = Double.parseDouble(costObject.getText().toString());

                EditText perObject = (EditText) parentView.findViewById(R.id.per);
                int per;
                per = Integer.parseInt(perObject.getText().toString());

                EditText timeLimitObject = (EditText) parentView.findViewById(R.id.timeLimitNumber);
                int time_limit;
                time_limit = Integer.parseInt(timeLimitObject.getText().toString());

                Spinner timeLimitSpinnerObject = (Spinner) parentView.findViewById(R.id.timeLimitSpinner);
                String timeLimitSpinner = timeLimitSpinnerObject.getSelectedItem().toString();
                if (timeLimitSpinner == "hrs") {
                    time_limit *= 60;
                }

                CheckBox sunday = (CheckBox) parentView.findViewById(R.id.sunday);
                CheckBox monday = (CheckBox) parentView.findViewById(R.id.monday);
                CheckBox tuesday = (CheckBox) parentView.findViewById(R.id.tuesday);
                CheckBox wednesday = (CheckBox) parentView.findViewById(R.id.wednesday);
                CheckBox thursday = (CheckBox) parentView.findViewById(R.id.thursday);
                CheckBox friday = (CheckBox) parentView.findViewById(R.id.friday);
                CheckBox saturday = (CheckBox) parentView.findViewById(R.id.saturday);

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

                RadioGroup lengthRadioGroup = (RadioGroup) parentView.findViewById(R.id.lengthOfRestrictionRadioGroup);
                int selectedLengthId = lengthRadioGroup.getCheckedRadioButtonId();
                RadioButton lengthRadioButton = (RadioButton) parentView.findViewById(selectedLengthId);
                String length = lengthRadioButton.getText().toString();

                int start_time;
                int end_time;

                if (length == "All Day") {
                    start_time = 0;
                    end_time = 1440;
                } else {
                    EditText startTimeObject = (EditText) parentView.findViewById(R.id.fromTime);
                    start_time = Integer.parseInt(startTimeObject.getText().toString());

                    EditText endTimeObject = (EditText) parentView.findViewById(R.id.toTime);
                    end_time = Integer.parseInt(endTimeObject.getText().toString());
                }

                RadioGroup angleRadioGroup = (RadioGroup) parentView.findViewById(R.id.parkingAngleRadioGroup);
                int selectedAngleId = angleRadioGroup.getCheckedRadioButtonId();
                RadioButton angleRadioButton = (RadioButton) parentView.findViewById(selectedAngleId);
                String angleString = angleRadioButton.getText().toString();
                int angle;
                if (angleString == "parallel") {
                    angle = 0;
                } else if (angleString == "angled") {
                    angle = 45;
                } else {
                    angle = 90;
                }


                //todo: refactor so it does not run on main thread
                //the name of the database is "restrictions"
                AppDatabase db = Room.databaseBuilder(parentView.getContext(),
                        AppDatabase.class,
                        "restrictions").allowMainThreadQueries().build();

                restrictionDao = db.getRestrictionDao();
                //Restriction restriction = new Restriction("sweep","0010000",0,1440,45,60,0.0,0);
                Restriction restriction = new Restriction(
                        coordinatesOfRestriction,
                        type,
                        days,
                        start_time,
                        end_time,
                        angle,
                        time_limit,
                        cost,
                        per
                );

                restrictionDao.insertAll(restriction);
                Log.d(TAG, restrictionDao.getAll().toString());


                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new HomeFragment())
                        .commit();

                Toast.makeText(view.getContext(), "Your restriction was added!", Toast.LENGTH_SHORT).show();
            }
        });


        return myView;
    }
}
