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

package com.curbmap.android.controller.handleRestrictionText;

import android.util.Log;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.models.db.RestrictionText;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UploadOneText {

    static final String TAG = "UploadOneText";

    public static void uploadOneText(
            RestrictionText restrictionText,
            String token
    ) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                //we increase the timeout here because
                //the default timeout is about 10 seconds
                //and does not allow enough time for the image to upload
                .readTimeout(2, TimeUnit.MINUTES)
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


        String bearerSpaceToken = "Bearer " + token;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        String json = gson.toJson(restrictionText);
        RequestBody body = RequestBody.create(JSON, json);


        //the current map area displayed on user's phone
        Call<String> results = service.doUploadText(
                bearerSpaceToken,
                body
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
                        Log.d(TAG, "Succeeded in uploading restriction text.");
                    } else {
                        Log.d(TAG, "Server rejected restriction text upload.");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failed to upload restriction text.");
                t.printStackTrace();
            }
        });
    }
}
