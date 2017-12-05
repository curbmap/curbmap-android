package com.curbmap.android.controller;

import android.arch.persistence.room.Room;
import android.view.View;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.Restriction;
import com.curbmap.android.models.db.RestrictionDao;

import static com.curbmap.android.controller.AddRestrictionController.getViewString;

public class AddRestrictionCheckExceptions {

    public static boolean checkExceptions(
            String coordinatesOfRestriction,
            View view,
            View parentView,
            int selectedId,
            String type,
            Double cost,
            int angle,
            String permitDistrict,
            int per,
            String days,
            String start_time,
            String end_time,
            RestrictionDao restrictionDao,
            int time_limit,
            String endCoordinates,
            int selectedLengthId,
            int selectedAngleId,
            String length) {
        //make sure the fragment_user entered all required values
        //try to display only one toast, instead of multiple toasts
        if (coordinatesOfRestriction == null) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_location),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (selectedId == -1) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_type),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (type.equals("")) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_type),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (type.equals(getViewString(parentView, R.string.parking_meter)) && cost == null) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_cost),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (type.equals(getViewString(parentView, R.string.parking_meter)) && per == -1) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_per),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (type.equals(getViewString(parentView, R.string.time_limit_parking)) && time_limit == -1) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_time_limit),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (type.equals("undefined")) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_custom_type),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (type.equals(getViewString(parentView, R.string.permit_parking_districts))
                && permitDistrict.equals("undefined")) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_permit_district),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (days.equals("0000000")) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_days),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (selectedLengthId == -1) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_length),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (length.equals(getViewString(parentView, R.string.within_hours_of)) && start_time.equals("undefined")) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_from),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (length.equals(getViewString(parentView, R.string.within_hours_of)) && end_time.equals("undefined")) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_to),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (selectedAngleId == -1) {
            Toast.makeText(view.getContext(),
                    getViewString(parentView, R.string.warning_no_angle),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }


        //fragment_user added all the needed information successfully
        else {
            Double DEFAULT_COST = 0.0;
            if (cost == null) {
                cost = DEFAULT_COST;
            }

            int DEFAULT_TIME_LIMIT = 0;
            if (time_limit == -1) {
                time_limit = DEFAULT_TIME_LIMIT;
            }

            //todo: refactor so it does not run on main thread
            //the name of the database is "restrictions"
            AppDatabase db = Room.databaseBuilder(parentView.getContext(),
                    AppDatabase.class,
                    "restrictions").allowMainThreadQueries().build();

            restrictionDao = db.getRestrictionDao();
            Restriction restriction = new Restriction(
                    coordinatesOfRestriction,
                    endCoordinates,
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
            restrictionDao.insertAll(restriction);

            return true;
        }
    }
}
