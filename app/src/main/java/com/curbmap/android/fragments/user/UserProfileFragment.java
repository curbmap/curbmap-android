package com.curbmap.android.fragments.user;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.curbmap.android.R;

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

        return myView;
    }
}