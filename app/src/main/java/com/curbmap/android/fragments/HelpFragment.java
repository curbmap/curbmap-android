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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * The fragment for displaying help.
 */
public class HelpFragment extends Fragment {
    View myView;


    //the BindViews here are used for setLinks method
    // and not for onCreateView!
    //because setLinks has most of the findViewByIds...

    @BindView(R.id.tutorial)
    TextView tutorial;
    @BindView(R.id.releases)
    TextView releases;
    @BindView(R.id.terms)
    TextView terms;
    @BindView(R.id.privacy)
    TextView privacy;
    @BindView(R.id.open_source_licenses)
    TextView open_source_licenses;
    @BindView(R.id.credits)
    TextView credits;

    Unbinder unbinder;

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

        unbinder = ButterKnife.bind(this, view);

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_tutorial);
            }
        });

        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_credits);
            }
        });

        releases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_releases);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_terms);
            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(R.string.link_privacy);
            }
        });


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder should never be null because
        // we always call setLinks() whenever view is created
        // but we check for nullity anyway for safety...
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


}
