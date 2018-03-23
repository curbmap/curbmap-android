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
import android.widget.TextView;
import android.widget.Toast;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAccessor;
import com.curbmap.android.models.db.UserAuth;
import com.curbmap.android.models.db.UserAuthAccessor;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserSigninFragment extends Fragment {
    View myView;
    String TAG = "UserSigninFragment";
    AppDatabase userAppDatabase;

    //todo:handle logic for showing signinorup or user screen here

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_user_signin, container, false);
        userAppDatabase = AppDatabase.getUserAppDatabase(getContext());

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

        Button signInButton = myView.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        //sign in
                        View view = (View) buttonView.getParent();
                        EditText usernameEditText = view.findViewById(R.id.usernameField);
                        String username = usernameEditText.getText().toString();
                        EditText passwordEditText = view.findViewById(R.id.passwordField);
                        String password = passwordEditText.getText().toString();
                        signIn(username, password);
                    }
                });

        TextView signUpButton = myView.findViewById(R.id.signUpLink);
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame
                                        , new UserSignupFragment())
                                .commit();
                    }
                });

        return myView;
    }


    /**
     * posts the signin info onto server
     *
     * @param username the user's username
     * @param password the user's password
     */
    public void signIn(final String username, final String password) {
        final String BASE_URL = getString(R.string.BASE_URL_API);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        CurbmapRestService service = retrofit.create(CurbmapRestService.class);
        Call<User> results = service.doLoginPOST(
                username,
                password);

        results.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, getContext().getResources().getString(R.string.success_signin));
                    Toast.makeText(
                            getContext(),
                            "Signed in!",
                            Toast.LENGTH_LONG)
                            .show();
                    Log.d(TAG, response.body().makeString());
                    User user = response.body();

                    UserAccessor.insertUser(userAppDatabase, user);

                    //store the userAuth information since the user has finally
                    //  successfully logged in with the username and password
                    AppDatabase userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(getContext());

                    long timestamp =  Calendar.getInstance().getTimeInMillis();
                    UserAuth userAuth = new UserAuth(username, password, timestamp);
                    UserAuthAccessor.insertUserAuth(userAuthAppDatabase, userAuth);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new UserProfileFragment())
                            .commit();

                } else {
                    Log.d(TAG, getString(R.string.issue_signin));
                    Toast.makeText(
                            getContext(),
                            "Wrong username or password",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, getString(R.string.fail_signin));
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        //todo: check whether this works or not
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

}
