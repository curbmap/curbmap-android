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

import android.location.Location;
import android.util.Log;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.models.db.RestrictionImage;
import com.curbmap.android.models.lib.OpenLocationCode;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
     * @param restrictionImage the RestrictionImage object to upload
     * @param token            the user's session token
     */
    public static void uploadOneImage(
            RestrictionImage restrictionImage,
            String token
    ) {
        String imagePath = restrictionImage.getImagePath();
        Location mLocation = restrictionImage.getmLocation();
        float azimuth = restrictionImage.getAzimuth();

        File file = new File(imagePath);

        Log.d(TAG, "created image file without exceptions");

        String olcString = "";

        //OLC of length 12 is about the size of a parking spot
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
        OkHttpClient client = new OkHttpClient.Builder()
                //we increase the timeout here because
                //the default timeout is about 10 seconds
                //and does not allow enough time for the image to upload
                .readTimeout(2,TimeUnit.MINUTES)
                .writeTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();

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

        String bearerSpaceToken = "Bearer " + token;

        //the current map area displayed on user's phone
        Call<String> results = service.doUploadImage(
                bearerSpaceToken,
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
                    } else {
                        Log.d(TAG, "Server rejected image upload.");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failed to upload image.");
                t.printStackTrace();
            }
        });
    }
}
