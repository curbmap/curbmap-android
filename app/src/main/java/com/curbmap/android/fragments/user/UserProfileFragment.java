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

import com.curbmap.android.R;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAccessor;
import com.curbmap.android.models.db.UserAuthAccessor;

public class UserProfileFragment extends Fragment {
    View myView;
    AppDatabase userAppDatabase;

    //todo:handle logic for showing signinorup or user screen here

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_user_profile, container, false);
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


        final User user = UserAccessor.getUser(userAppDatabase);
        Log.e("session-is", user.getSession());
        EditText nameField = myView.findViewById(R.id.nameField);
        nameField.setText(user.getUsername());


        Button signOutButton = myView.findViewById(R.id.signOutButton);
        /**
         * The logic for what happens when the user signs out
         */
        signOutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserAccessor.deleteUser(userAppDatabase);

                        //delete UserAuth since user logs out...
                        AppDatabase userAuthAppDatabase = AppDatabase.getUserAuthAppDatabase(getContext());
                        UserAuthAccessor.deleteUserAuth(userAuthAppDatabase);

                        Toast.makeText(
                                getContext(),
                                "You have signed out.",
                                Toast.LENGTH_LONG)
                                .show();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, new UserSigninFragment())
                                .commit();
                    }
                }
        );


        return myView;
    }

    @Override
    public void onDestroy() {
        userAppDatabase.destroyInstance();
        super.onDestroy();
    }


}
