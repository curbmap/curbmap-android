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

package com.curbmap.android.controller.handleupload;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.RestrictionAccessor;
import com.curbmap.android.models.db.RestrictionContainer;
import com.curbmap.android.models.db.RestrictionImage;
import com.curbmap.android.models.db.RestrictionText;
import com.curbmap.android.models.db.Settings;
import com.curbmap.android.models.db.SettingsAccessor;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAccessor;
import com.google.gson.Gson;

import java.util.List;

public class UploadHandler {
    private static final String TAG = "UploadHandler";

    /**
     * Initiate this and it will handle the whole process of uploading for you
     *
     * @param context the application context
     */
    public static void initiateHandler(Context context) {
        if (isUserLoggedIn(context) && isNetworkReadyForUploading(context)) {
            initiateUploading(context);
        }
    }

    public static boolean isUserLoggedIn(Context context) {
        AppDatabase userAppDatabase = AppDatabase.getUserAppDatabase(context);
        User user = UserAccessor.getUser(userAppDatabase);
        return user != null;
    }

    public static boolean isNetworkReadyForUploading(Context context) {
        AppDatabase settingsAppDatabase = AppDatabase.getSettingsAppDatabase(
                context);
        Settings settings = SettingsAccessor.getSettings(settingsAppDatabase);

        if (settings == null || settings.isUploadOverWifi()) {
            if (settings == null) {
                //if settings entry does not exist we must create it
                Settings newSettings = new Settings();
                SettingsAccessor.insertSettings(settingsAppDatabase, newSettings);
                Log.d(TAG, "Created new settings entry");
            }
            ConnectivityManager connManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Log.d(TAG, "Settings is upload over wifi only");
            if (mWifi != null && mWifi.isConnected()) {
                // Do whatever
                Log.d(TAG, "Wifi only in settings AND Wifi is currently connected");
                return true;
            } else {
                return false;
            }

        } else {
            Log.d(TAG, "Settings is upload over anything");
            if (isNetworkAvailable(context)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static boolean
    isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //handles actually uploading restrictions to the server
    public static void initiateUploading(Context context) {
        AppDatabase userAppDatabase = AppDatabase.getUserAppDatabase(context);
        User user = UserAccessor.getUser(userAppDatabase);
        String token = user.getToken();

        AppDatabase restrictionAppDatabase = AppDatabase.getRestrictionAppDatabase(context);
        List<RestrictionContainer> uploadingRestrictionList =
                RestrictionAccessor.getAllUploadingRestriction(restrictionAppDatabase);
        if (uploadingRestrictionList.size() > 0) {
            //there is something left to upload
            //get first item on list
            RestrictionContainer restrictionContainer = uploadingRestrictionList.get(0);
            String object = restrictionContainer.getObject();
            Gson gson = new Gson();
            if (restrictionContainer.isImageRestriction()) {
                RestrictionImage restrictionImage = gson.fromJson(object, RestrictionImage.class);
                UploadOneImage.uploadOneImage(context, restrictionContainer, restrictionImage, token);
            } else {
                RestrictionText restrictionText = gson.fromJson(object, RestrictionText.class);
                UploadOneText.uploadOneText(context, restrictionContainer, restrictionText, token);
            }

        }
    }

    public static void handleSuccessfulUpload(Context context,
                                              RestrictionContainer restrictionContainer) {
        AppDatabase restrictionAppDatabase = AppDatabase.getRestrictionAppDatabase(context);
        RestrictionAccessor.setRestrictionAsUploaded(
                restrictionAppDatabase, restrictionContainer
        );
        UploadHandler.initiateUploading(context);
    }


}
