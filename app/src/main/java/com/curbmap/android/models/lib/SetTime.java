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

/*
 * Source:
 * The SetTime class is sourced from the StackOverflow answer for
 * "TimePicker Dialog from clicking EditText", answer by Sumoanand
 * https://stackoverflow.com/a/27647415/2333621
 * It is used to launch a TimePicker dialog which allows the user to pick a time
 * that a restriction starts and ends respectively
 */

package com.curbmap.android.models.lib;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class SetTime implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {
    static final String TAG = "SetTime";

    private EditText editText;
    private Calendar myCalendar;

    public SetTime(EditText editText) {
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.myCalendar = Calendar.getInstance();

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if (hasFocus) {
            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendar.get(Calendar.MINUTE);
            new TimePickerDialog(v.getContext(), this, hour, minute, true).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub
        this.editText.setText(hourOfDay + ":" + minute);
    }

}