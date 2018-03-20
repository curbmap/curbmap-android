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

package com.curbmap.android.controller.handleImageRestriction;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.models.lib.OpenLocationCode;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Houses the uploadOneImage function which
 * handles uploading an image to the server
 * given the imagePath to the image.
 */
public class UploadOneImage {

    static final String TAG = "UploadOneImage";

    /**
     * Handles uploading an image to the server
     * given the imagePath to the image
     * when a user takes a picture of a parking sign
     * and clicks upload image to upload it to the server.
     * <p>
     * Please note that the location and azimuth are stored separately
     * so whatever implementation you have will need to store the image
     * but also the location and azimuth, for example
     * by storing location and azimuth in separate text files for retrieval
     * in the case of uploading multiple images.
     *
     * @param context   The application context
     * @param imagePath The path to the image on the device
     * @param mLocation The location of the user
     * @param azimuth   The azimuth of the camera, measured as
     *                  degrees clockwise from True North
     */
    public static void uploadOneImage(
            final Context context,
            String imagePath,
            Location mLocation,
            float azimuth
    ) {
        String filePath = imagePath;
        File file = new File(filePath);

        Log.d(TAG, "created image file without exceptions");
        //bookmark

        String olcString = "";

        //12 is about the size of a parking spot
        final int OLC_LENGTH = 12;
        if (mLocation != null) {
            OpenLocationCode code = new OpenLocationCode(
                    mLocation.getLatitude(),
                    mLocation.getLongitude(),
                    OLC_LENGTH
            );
            olcString = code.getCode();
        } else {
            Log.e(TAG, "mLocation was null, so olc is blank");
        }


        //todo: figure out how to NOT log the image body
        //because it keeps spamming the logs...
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final String BASE_URL = "https://curbmap.com:50003";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CurbmapRestService service = retrofit.create(CurbmapRestService.class);


        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody olc = RequestBody.create(MediaType.parse("text/plain"), olcString);
        RequestBody bearing = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(azimuth));

        Log.d("olc is", olcString);



        /*
         * The default username and session
         * we use this temporarily before fixing the proper
         * user session token to always update properly
         */
        String username = "curbmaptest";
        String session = "x";

        //warning this code does not work right now
        //it must be refactored so that the session is updated
        //otherwise, we receive a 404 error because
        //the session is expired and invalid
        //only if user is signed in
        /*
        AppDatabase userAppDatabase = AppDatabase.getUserAppDatabase(context);
        User user = UserAccessor.getUser(userAppDatabase);
        AppDatabase.destroyInstance();

        if (user != null) {
            username = user.getUsername();
            session = user.getSession();
        }
        */

        Log.d("username", username);
        Log.d("session", session);

        //the current map area displayed on user's phone
        Call<String> results = service.doUploadImage(
                username,
                session,
                body,
                olc,
                bearing
        );

        results.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e(TAG, response.toString());
                if (response.body() == null) {
                    Log.e(TAG, "response was null!");
                } else {
                    Log.d(TAG, response.body());

                    if (response.isSuccessful()) {
                        Log.d(TAG, "Succeeded in uploading image.");
                        Toast.makeText(context,
                                "Succeeded in uploading image.",
                                Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Log.d(TAG, "Server rejected image upload.");
                        Toast.makeText(context,
                                "Server rejected image upload." +
                                        response.body(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failed to upload image.");
                t.printStackTrace();
                Toast.makeText(context,
                        "Failed to upload image.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}
