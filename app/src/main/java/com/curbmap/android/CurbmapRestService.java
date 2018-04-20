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

package com.curbmap.android;


import com.curbmap.android.models.SignUpResponse;
import com.curbmap.android.models.db.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Interface for the functions used to send requests
 * to the curbmap server
 */
public interface CurbmapRestService {
    String TAG = "CurbmapRestService";

    //warning: watch out for 50003 in url
    // base urls are "curbmap.com:50003/" or just "curbmap.com/"

    /**
     * Sends a login request to the server
     * Url: https://curbmap.com/login
     *
     * @param username The username to attempt for login
     * @param password The password to attempt for login
     * @return The call to the server requesting the login
     */
    @POST("login")
    @FormUrlEncoded
    Call<User> doLoginPOST(
            @Field("username") String username,
            @Field("password") String password
    );

    /**
     * Sends a sign-up request to the server
     * Url: https://curbmap.com/signup
     *
     * @param username The username the user chose
     * @param password The password the user chose
     * @param email    The email the user chose
     * @return The call to the server requesting the sign-up
     */
    @POST("signup")
    @FormUrlEncoded
    Call<SignUpResponse> signup(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email
    );

    /**
     * Uploads an image to the server
     * Url: https://curbmap.com:50003/imageUpload
     *
     * @param bearerSpaceToken "Bearer " + token, where token is the session token received upon login
     * @param image            The image file to upload
     * @param olc              The Open Location Code describing the location the image was taken
     * @param bearing          The bearing the image was taken measured as
     *                         degrees clockwise from True North
     * @return The call to to upload the image to the server
     */
    @POST("imageUpload")
    @Multipart
    Call<String> doUploadImage(
            @Header("Authorization") String bearerSpaceToken,

            @Part MultipartBody.Part image,
            @Part("olc") RequestBody olc,
            @Part("bearing") RequestBody bearing
    );

    /**
     * Warning: "coordinates" in API docs must actually be "line" as used here.
     *
     * {
     * "line": [[-118.3997778, 33.8608611], [-118.3997414,33.8607043]],
     * "restrictions": [{
     * "type": 3,
     * "duration": 120,
     * "vehicle": -1,
     * "cost": 0.25,
     * "per": 12,
     * "side": 3,
     * "angle": 0,
     * "holiday": true,
     * "days": [1,1,1,1,1,1,1],
     * "weeks": [1,1,1,1],
     * "months": [1,1,1,1,1,1,1,1,1,1,1,1],
     * "start": 600,
     * "end": 1440
     * }]
     * }
     *
     * @return
     */
    @POST("addLine")
    Call<String> doUploadText(
            @Header("Authorization") String bearerSpaceToken,
            @Body RequestBody body
            );
}