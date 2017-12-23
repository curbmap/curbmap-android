package com.curbmap.android.controller.addRestriction;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.curbmap.android.R;
import com.curbmap.android.models.db.Restriction;
import com.curbmap.android.models.db.RestrictionDao;

public class HandleSubmit {
    static final String TAG = "HandleSubmit";

    /**
     * Handles the event of user clicking submit button on Add Restriction fragment
     *
     * @param view           The view of the entire form, eg the parent of the button view.
     * @param polylineString the json string of the polyline object
     * @return the value of checkNoExceptions, false if warning had to be displayed, true otherwise
     */
    public static boolean submitAddRestriction(
            View view,
            String polylineString) {

        //although we restrictionDao as null, it should work properly.
        //...this is just an intricacy of Room.
        RestrictionDao restrictionDao = null;

        Restriction restriction = CreateRestriction.createRestriction(view, polylineString);

        RadioGroup typeRadioGroup = view.findViewById(R.id.typeOfRestrictionRadioGroup);
        int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();

        RadioGroup lengthRadioGroup = view.findViewById(R.id.lengthOfRestrictionRadioGroup);
        int selectedLengthId = lengthRadioGroup.getCheckedRadioButtonId();

        RadioGroup angleRadioGroup = view.findViewById(R.id.parkingAngleRadioGroup);
        int selectedAngleId = angleRadioGroup.getCheckedRadioButtonId();

        String length = "";

        boolean noExceptions = CheckExceptions.checkNoExceptions(
                view,
                selectedTypeId,
                selectedLengthId,
                selectedAngleId,
                length,
                restriction
        );

        if (noExceptions) {
            Context context = view.getContext();
            CheckExceptions.addToDatabase(
                    context,
                    restriction);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Gets the String resource from android
     *
     * @param view Any view in the app
     * @param id   the id of the string you want
     * @return the string you wanted
     */
    static String getViewString(View view, int id) {
        return view.getContext().getResources().getString(id);
    }


}
