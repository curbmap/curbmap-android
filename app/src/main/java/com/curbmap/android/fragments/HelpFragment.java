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

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.curbmap.android.R;

/**
 * The fragment for displaying help.
 */
public class HelpFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_help, container, false);

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


        setLinks(myView);

        return myView;
    }

    /**
     * Sets the links for some of the buttons
     *
     * @param view
     */
    private void setLinks(View view) {
        TextView tutorial = myView.findViewById(R.id.tutorial);
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_tutorial);
            }
        });

        TextView credits = myView.findViewById(R.id.credits);
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_credits);
            }
        });

        TextView releases = myView.findViewById(R.id.releases);
        releases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_releases);
            }
        });

        TextView terms = myView.findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_terms);
            }
        });

        TextView privacy = myView.findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_privacy);
            }
        });


        TextView open_source_licenses = myView.findViewById(R.id.open_source_licenses);
        open_source_licenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_open_source_licenses);
            }
        });
    }

    private void openLink(int uriId) {
        Uri uri = Uri.parse(getString(uriId)); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
