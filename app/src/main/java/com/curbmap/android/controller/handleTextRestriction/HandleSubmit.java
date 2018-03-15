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
import android.widget.RadioGroup;

import com.curbmap.android.R;
import com.curbmap.android.models.db.Restriction;

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
