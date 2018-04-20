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

package com.curbmap.android.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.curbmap.android.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * The fragment for displaying the alarm and timer.
 */
public class AlarmFragment extends Fragment {
    private static final String TAG = "AlarmFragment";
    View myView;
    @BindView(R.id.menu_icon)
    ImageView menu_icon;
    @BindView(R.id.setAlarmBtn)
    Button setAlarmBtn;
    @BindView(R.id.timePicker)
    TimePicker timePicker;
    @BindView(R.id.setTimerBtn)
    Button setTimerBtn;
    @BindView(R.id.timerMagnitude)
    EditText timerMagnitude;
    @BindView(R.id.timerUnit)
    Spinner timerUnit;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_alarm, container, false);
        unbinder = ButterKnife.bind(this, myView);

        return myView;
    }


    @OnClick(R.id.menu_icon)
    public void openMenu(View view) {
        DrawerLayout drawer = (DrawerLayout)
                getActivity()
                        .getWindow()
                        .getDecorView()
                        .findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.setAlarmBtn)
    public void setSetAlarmBtn(View view) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        //rightNow snippet from https://stackoverflow.com/a/40167691
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        //end snippet
        int currentMinute = rightNow.get(Calendar.MINUTE);

        int minutesToTimer = (hour - currentHour) * 60 +
                (minute - currentMinute);
        //if timer is for next day, add a day's worth of minutes
        if (minutesToTimer < 0) minutesToTimer += 24 * 60;
        createTimer(getContext(), minutesToTimer);
    }

    @OnClick(R.id.setTimerBtn)
    public void setSetTimerBtn(View view) {
        if (timerMagnitude.getText() == null ||
                timerMagnitude.getText().toString().equals("")) {
            Toast.makeText(
                    getContext(),
                    "Please enter a magnitude for the timer",
                    Toast.LENGTH_LONG)
                    .show();
        } else {
            //the number of minutes to set a timer for, in the case of set timer
            //this does not support decimal timer magnitude
            // because we only allow integer input anyway
            // if we were to allow for entering decimal points
            // we would have to update this
            int timerMinutes = Integer.parseInt(
                    timerMagnitude.getText().toString());
            String timerUnitString = timerUnit.getSelectedItem().toString();
            if (timerUnitString.equals("hrs")) {
                timerMinutes *= 60;
            }
            createTimer(getContext(), timerMinutes);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Creates a timer that will execute a notification in given number of minutes
     *
     * @param context The application context
     * @param minutes The number of minutes to start the notification in
     */
    public void createTimer(final Context context, int minutes) {

        int MS_IN_A_MINUTE = 60 * 1000;
        int notificationDelay = minutes * MS_IN_A_MINUTE;
        Log.d(TAG, "creating timer for " + minutes + " minutes");


        int secondsLeft = notificationDelay / 1000;
        while (secondsLeft > 0) {
            //run the notification countdown
            Handler handler = new Handler();
            final int finalTimerMinutes = minutes;
            final int finalSecondsLeft = secondsLeft;
            handler.postDelayed(new Runnable() {
                public void run() {
                    Notification.Builder mBuilder =
                            new Notification.Builder(context)
                                    .setSmallIcon(R.drawable.curbmap_25x25)
                                    .setContentTitle("curbmap timer")
                                    .setContentText("Your timer for " +
                                            finalTimerMinutes +
                                            " minutes has " +
                                            finalSecondsLeft / 60 +
                                            ":" +
                                            String.format("%02d", finalSecondsLeft % 60) +
                                            " remaining.");

                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(
                                    Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                    Log.d(TAG, "Published timer notification");
                }
            }, notificationDelay - (secondsLeft * 1000));

            secondsLeft--;
        }

        //run the notification alarm
        Handler handler = new Handler();
        final int finalTimerMinutes = minutes;
        handler.postDelayed(new Runnable() {
            public void run() {
                Notification.Builder mBuilder =
                        new Notification.Builder(context)
                                .setSmallIcon(R.drawable.curbmap_25x25)
                                .setContentTitle("curbmap timer")
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                .setContentText("Your timer for " + finalTimerMinutes +
                                        " minutes has completed.");

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(
                                Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());
                Log.d(TAG, "Published timer notification");
            }
        }, notificationDelay);
    }

}
