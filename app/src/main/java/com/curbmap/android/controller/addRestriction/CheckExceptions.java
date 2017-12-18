package com.curbmap.android.controller.addRestriction;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.models.db.RestrictionAppDatabase;
import com.curbmap.android.models.db.Restriction;
import com.curbmap.android.models.db.RestrictionDao;

public class CheckExceptions {
    static final String TAG = "CheckExceptions";


    static final int OKAY_RESTRICTIONS = -1;

    /**
     * Checks for exceptions
     * @param view
     * @param selectedTypeId the id of type selected radioButton
     * @param selectedLengthId the id of the length selected radioButton
     * @param selectedAngleId the id of the angle selected radioButton
     * @param length the length of restriction: "All Day" or "Within hours of:"
     * @param restriction the Restriction object containing restriction information
     * @return false if user needs to fill form properly, true otherwise
     */
    static boolean checkNoExceptions(
            View view,
            int selectedTypeId,
            int selectedLengthId,
            int selectedAngleId,
            String length,
            Restriction restriction) {
        //make sure the fragment_user entered all required values
        //try to display only one toast, instead of multiple toasts
        String parkingMeter = HandleSubmit.getViewString(view, R.string.parking_meter);
        String timeLimitParking = HandleSubmit.getViewString(view, R.string.time_limit_parking);
        String permitParkingDistricts = HandleSubmit.getViewString(view, R.string.permit_parking_districts);
        String withinHoursOf = HandleSubmit.getViewString(view, R.string.within_hours_of);

        int warningResult = checkWarning(
                parkingMeter,
                timeLimitParking,
                permitParkingDistricts,
                withinHoursOf,
                selectedTypeId,
                selectedLengthId,
                selectedAngleId,
                length,
                restriction);


        if (warningResult != OKAY_RESTRICTIONS) {
            //there was an error in the form for add restrictions

            Toast.makeText(view.getContext(),
                    HandleSubmit.getViewString(view, warningResult),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else {
            //user filled the form with all the needed information
            return true;
        }
    }


    /**
     * Given the variables that exist when user clicks 'Add Restriction'
     * Return the id of the string that should appear as a result
     * But if there is no exception (yay!) returns -5
     *
     * @return Toast string id, or -5 for success
     */
    public static int checkWarning(
            String parkingMeter,
            String timeLimitParking,
            String permitParkingDistricts,
            String withinHoursOf,
            int selectedTypeId,
            int selectedLengthId,
            int selectedAngleId,
            String length,
            Restriction restriction) {
        if (restriction == null) {
            return R.string.warn_null_restriction;
        } else if (restriction.polyline == null) {
            //actually this condition is not being handled properly
            //but it is okay because the user would never be able to reach here
            //unless they selected 2 or more points
            //so this check should be unnecessary
            return R.string.warn_select_restriction;
        } else if (selectedTypeId == -1) {
            return R.string.warning_no_type;
        } else if (restriction.type.equals("")) {
            return R.string.warning_no_type;
        } else if (restriction.type.equals(parkingMeter) && restriction.cost == -1) {
            return R.string.warning_no_cost;
        } else if (restriction.type.equals(parkingMeter) && restriction.per == -1) {
            return R.string.warning_no_per;
        } else if (restriction.type.equals(timeLimitParking) && restriction.time_limit == -1) {
            return R.string.warning_no_time_limit;
        } else if (restriction.type.equals("undefined")) {
            return R.string.warning_no_custom_type;
        } else if (restriction.type.equals(permitParkingDistricts)
                && restriction.permitDistrict.equals("undefined")) {
            return R.string.warning_no_permit_district;
        } else if (restriction.days.equals("0000000")) {
            return R.string.warning_no_days;
        } else if (selectedLengthId == -1) {
            return R.string.warning_no_length;
        } else if (length.equals(withinHoursOf) && restriction.start_time.equals("undefined")) {
            return R.string.warning_no_from;
        } else if (length.equals(withinHoursOf) && restriction.end_time.equals("undefined")) {
            return R.string.warning_no_to;
        } else if (selectedAngleId == -1) {
            return R.string.warning_no_angle;
        } else {
            //no errors and good to go
            return OKAY_RESTRICTIONS;
        }
    }

    /**
     * Adds restriction to Room database
     * Only when user has filled the form properly
     */
    public static void addToDatabase(
            Context context,
            Restriction restriction) {

        //todo: refactor db operations to run on a non-main thread
        //the name of the database is "restrictions"
        RestrictionAppDatabase db = Room.databaseBuilder(
                context,
                RestrictionAppDatabase.class,
                "restrictions")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        RestrictionDao restrictionDao = db.getRestrictionDao();
        restrictionDao.insertAll(restriction);
    }

}


