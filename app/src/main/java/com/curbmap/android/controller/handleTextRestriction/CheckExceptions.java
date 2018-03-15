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

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.Restriction;
import com.curbmap.android.models.db.RestrictionAccessor;

public class CheckExceptions {
    static final String TAG = "CheckExceptions";


    static final int OKAY_RESTRICTIONS = -1;

    /**
     * Checks for exceptions
     *
     * @param view
     * @param selectedTypeId   the id of type selected radioButton
     * @param selectedLengthId the id of the length selected radioButton
     * @param selectedAngleId  the id of the angle selected radioButton
     * @param length           the length of restriction: "All Day" or "Within hours of:"
     * @param restriction      the Restriction object containing restriction information
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
        //display only one toast, instead of multiple toasts
        int warningResult = checkWarning(
                selectedTypeId != -1,
                selectedLengthId != -1,
                selectedAngleId != -1,
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
     * @param typeIsSelected   true if type is selected, false otherwise
     * @param angleIsSelected  true if angle is selected, false otherwise
     * @param lengthIsSelected true if length is selected, false otherwise
     * @param length           either "All Day" or "Within hours of:"
     *                         to represent the length of parking
     *                         restriction
     * @param restriction      the restriction object containing a lot of data
     * @return Toast string id for warning toast,
     * or OKAY_RESTRICTIONS === -1 for success
     */
    public static int checkWarning(
            boolean typeIsSelected,
            boolean lengthIsSelected,
            boolean angleIsSelected,
            String length,
            Restriction restriction) {

        String parkingMeter = "Parking Meter";
        String timeLimitParking = "Time Limit Parking";
        String permitParkingDistricts = "Permit Parking Districts";
        String withinHoursOf = "Within hours of:";

        /**
         * These toasts are sequenced to be intuitive by being based on the
         * sequence of answers that the user types into the form.
         * The first questions have the first warnings and so forth.
         */
        if (restriction == null) {
            //the null restriction test is meant for unit testing
            return R.string.warn_null_restriction;
        } else if (restriction.polyline == null) {
            //actually this condition is not being handled properly
            //but it is okay because the user would never be able to reach here
            //unless they selected 2 or more points
            //so this check should be unnecessary
            return R.string.warn_select_restriction;
        } else if (!typeIsSelected) {
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
        } else if (!lengthIsSelected) {
            return R.string.warning_no_length;
        } else if (length.equals(withinHoursOf) && restriction.start_time.equals("undefined")) {
            return R.string.warning_no_from;
        } else if (length.equals(withinHoursOf) && restriction.end_time.equals("undefined")) {
            return R.string.warning_no_to;
        } else if (!angleIsSelected) {
            return R.string.warning_no_angle;
        } else {
            //no errors and good to go
            return OKAY_RESTRICTIONS;
        }
    }

    /**
     * Adds restriction to Room database
     * Should only be called after the user has filled the
     * restriction description form properly
     */
    public static void addToDatabase(
            Context context,
            Restriction restriction) {

        //todo: refactor db operations to run on a non-main thread
        AppDatabase restrictionAppDatabase = AppDatabase.getRestrictionAppDatabase(context);
        RestrictionAccessor.insertRestriction(restrictionAppDatabase, restriction);
    }
}



