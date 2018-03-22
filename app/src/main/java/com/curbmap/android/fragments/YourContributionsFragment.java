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

package com.curbmap.android.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.controller.ImageAdapter;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.RestrictionAccessor;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * The fragment for displaying all of the contributions made by the user.
 * As of now, it only displays all contributions made using the form on the device
 * that the user is currently using.
 */
public class YourContributionsFragment extends Fragment {
    View myView;
    private String TAG = "YourContributions";
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_your_contributions, container, false);
        unbinder = ButterKnife.bind(this, myView);



        GridView uploadingGridView = (GridView) myView.findViewById(R.id.uploadingGridView);
        uploadingGridView.setAdapter(new ImageAdapter(getContext(), false));

        uploadingGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        GridView uploadedGridView = (GridView) myView.findViewById(R.id.uploadedGridView);
        uploadedGridView.setAdapter(new ImageAdapter(getContext(), true));

        uploadedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });




        return myView;
    }

    @OnClick(R.id.clearContributionsButton)
    public void clearAllContributions(View view) {
        new AlertDialog.Builder(myView.getContext())
                .setTitle(getString(R.string.clear_contributions))
                .setMessage(getString(R.string.confirm_clear_contributions))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RestrictionAccessor.deleteAllRestriction(
                                AppDatabase.getRestrictionAppDatabase(
                                        getContext()
                                )
                        );

                        Toast.makeText(myView.getContext(),
                                getString(R.string.success_clear_contributions),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @OnClick(R.id.emailContributionsButton)
    public void emailContributions(View view) {
        final String finalAllCards = "all cards";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{getString(R.string.developer_email)});
        i.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.email_subject));
        i.putExtra(Intent.EXTRA_TEXT, finalAllCards);
        try {
            startActivity(Intent.createChooser(i,
                    getString(R.string.prompt_send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(myView.getContext(),
                    getString(R.string.error_no_email_clients),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @OnClick(R.id.menu_icon)
    public void openMenu(View view) {
        DrawerLayout drawer = (DrawerLayout)
                getActivity()
                        .getWindow()
                        .getDecorView()
                        .findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
