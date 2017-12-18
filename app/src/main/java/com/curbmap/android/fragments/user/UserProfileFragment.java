package com.curbmap.android.fragments.user;

import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAppDatabase;
import com.curbmap.android.models.db.UserDao;

public class UserProfileFragment extends Fragment {
    View myView;

    //todo:handle logic for showing signinorup or user screen here

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_user_profile, container, false);

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


        UserAppDatabase db = Room.databaseBuilder(
                getContext(),
                UserAppDatabase.class,
                "user")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        final UserDao userDao = db.getUserDao();

        User user = userDao.getUser();
        EditText nameField = myView.findViewById(R.id.nameField);
        nameField.setText(user.getUsername());


        Button signOutButton = myView.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userDao.deleteUser();
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
}
