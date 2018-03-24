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
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.curbmap.android.R;
import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.RestrictionAccessor;
import com.curbmap.android.models.db.RestrictionContainer;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private boolean isUploaded;
    private List<RestrictionContainer> restrictionContainerList;

    public ImageAdapter(Context c, boolean isUploaded) {
        mContext = c;
        this.isUploaded = isUploaded;

        AppDatabase restrictionAppDatabase = AppDatabase.getRestrictionAppDatabase(this.mContext);
        if (isUploaded) {
            this.restrictionContainerList = RestrictionAccessor.
                    getAllUploadedRestriction(restrictionAppDatabase);
        } else {
            this.restrictionContainerList = RestrictionAccessor.
                    getAllUploadingRestriction(restrictionAppDatabase);
        }
    }

    private void populateThumbnails() {

    }

    public int getCount() {
        if (this.restrictionContainerList == null) {
            return 0;
        } else {
            return this.restrictionContainerList.size();
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        RestrictionContainer r = restrictionContainerList.get(position);
        if (r.isTextRestriction()) {
            //todo: set image for text restriction based on the type of text restriction
            imageView.setImageResource(R.drawable.type_no_parking);
        } else {
            imageView.setImageURI(Uri.parse(r.getThumbnailPath()));
        }
        return imageView;
    }


}