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
 * Source: https://gist.github.com/BrandonSmith/6679223
 * BrandonSmith's Quick example of how to schedule a notification in the future using AlarmManager
 * However the source code here was modified and adapted to curbmap.
 */

package com.curbmap.android.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.curbmap.android.R;

/**
 * Sets basic information needed to create notification for timer in curbmap
 */
public class NotificationSetter {
    private static final String TAG = "NotificationSetter";

    /**
     * Schedules a notification for curbmap timer
     *
     * @param context      The context of the application
     * @param notification The notification object to schedule
     * @param delay        The delay in milliseconds before broadcasting the notification
     */
    public static void scheduleNotification(Context context, Notification notification, int delay) {

        Log.d(TAG, context.toString());
        Log.d(TAG, notification.toString());
        Log.d(TAG, String.valueOf(delay));

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    /**
     * Creates a notification object to send to the scheduleNotification() function
     *
     * @param context The context of the application
     * @param content The text to display on the notification
     * @return The notification object which would display the text
     */
    public static Notification getNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("curbmap timer notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        return builder.build();
    }

}
