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

package com.curbmap.android.controller;

import android.content.Context;
import android.util.Log;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAccessor;
import com.curbmap.android.models.db.UserAuth;
import com.curbmap.android.models.db.UserAuthAccessor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HandShaker {
    private static final String TAG = "HandShaker";

    /**
     * Every time the function is called
     * check whether its time to shake hands
     * if it is, then run handshake
     */
    public static void handShake(final Context context) {

        AppDatabase userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(
                context);
        UserAuth userAuth = UserAuthAccessor.getUserAuth(userAuthAppDatabase);
        if(userAuth != null) {
            if (userAuth.shouldWeHandShakeRightNow()) {
                //perform handshake
                //send login request to server
                final String BASE_URL = context.getString(R.string.BASE_URL_API);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                String username = userAuth.getUsername();
                String password = userAuth.getPassword();

                CurbmapRestService service = retrofit.create(CurbmapRestService.class);
                Call<User> results = service.doLoginPOST(
                        username,
                        password);

                results.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "successfully signed in through handshake to renew session token");

                            AppDatabase userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(
                                    context);

                            UserAuthAccessor.updateUserAuth(userAuthAppDatabase);

                            AppDatabase userAppDatabase = AppDatabase.getUserAppDatabase(
                                    context);
                            UserAccessor.deleteUser(userAppDatabase);

                            //update user object
                            //so not only the session token is renewed
                            //but also any other updates from the server will be received
                            User user = response.body();
                            UserAccessor.insertUser(userAppDatabase, user);


                        } else {
                            Log.e(TAG, "failed to sign in for handshake to renew session token");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, context.getString(R.string.fail_signin));
                        t.printStackTrace();
                    }
                });
            } else {
                Log.d(TAG, "We do not need to handshake.");
            }
        } else {
            Log.e(TAG, "userAuth was null so could not perform handshake");
        }

    }
}
