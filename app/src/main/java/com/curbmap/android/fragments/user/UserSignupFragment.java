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

package com.curbmap.android.fragments.user;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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
import android.widget.Toast;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.curbmap.android.models.SignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class UserSignupFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_user_signup, container, false);

        ImageView menu_icon = (ImageView) myView.findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DrawerLayout drawer = (DrawerLayout)
                                getActivity()
                                        .getWindow()
                                        .getDecorView()
                                        .findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
        );


        Button signUpButton = myView.findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //sign up
                        EditText usernameEditText = myView.findViewById(R.id.usernameField);
                        String username = usernameEditText.getText().toString();
                        EditText passwordEditText = myView.findViewById(R.id.passwordField);
                        String password = passwordEditText.getText().toString();
                        EditText emailEditText = myView.findViewById(R.id.emailField);
                        String email = emailEditText.getText().toString();
                        EditText confirmEmailEditText = myView.findViewById(R.id.confirmEmailField);
                        String confirmEmail = confirmEmailEditText.getText().toString();

                        signUp(username, password, email);

                    }
                });

        return myView;
    }

    public void signUp(String username, String password, String email) {
        final String BASE_URL = getString(R.string.BASE_URL_API);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurbmapRestService service = retrofit.create(CurbmapRestService.class);
        Call<SignUpResponse> results = service.signup(
                username,
                password,
                email);

        results.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "success signing up");
                    Toast.makeText(
                            getContext(),
                            response.body().getStatusMessage(),
                            Toast.LENGTH_LONG)
                            .show();

                    if (response.body().isSuccess()) {

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame
                                        , new UserSigninFragment())
                                .commit();
                    }
                } else {
                    Log.d(TAG, "failure signing up");
                    //triggers null pointer exception:
                    //Log.d(TAG, response.body().getStatusMessage());
                    Toast.makeText(
                            getContext(),
                            "Failed to sign up. Please check that your entries are valid.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d(TAG, getString(R.string.fail_signin));
                t.printStackTrace();
            }
        });

    }
}
