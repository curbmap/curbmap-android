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
import com.curbmap.android.models.db.RestrictionTextInfo;
import com.curbmap.android.models.db.User;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
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
     * @param username The username of the logged in user
     * @param session  The session token string to authenticate the user
     * @param image    The image file to upload
     * @param olc      The Open Location Code describing the location the image was taken
     * @param bearing  The bearing the image was taken measured as
     *                 degrees clockwise from True North
     * @return The call to to upload the image to the server
     */
    @POST("imageUpload")
    @Multipart
    Call<String> doUploadImage(
            @Header("username") String username,
            @Header("session") String session,

            @Part MultipartBody.Part image,
            @Part("olc") RequestBody olc,
            @Part("bearing") RequestBody bearing
    );

    /**
     * {
     * "coordinates": [[0]],
     * "restrictions": [
     * {"type": 0,"angle": 0,"start": 0,"end": 0,
     * "days": [false,false,false,false,false,false,false],
     * "weeks": [false,false,false,false],
     * "months": [false,false,false,false,
     * false,false,false,false,false,false,
     * false,false],
     * "limit": 60,"permit": "111","cost": 1.25,
     * "per": 60,"vehicle": 0,"side": 0}
     * ]
     * }
     *
     * @return
     */
    @POST("addLine")
    Call<String> doUploadText(
            @Header("username") String username,
            @Header("session") String session,

            //the following are the contents of a single RestrictionText object:
            //  coordinates and restrictionTextInfo
            @Field("coordinates") ArrayList<Map<Integer,Integer>> coordinates,
            @Field("restrictions") RestrictionTextInfo restrictionTextInfo
    );
}