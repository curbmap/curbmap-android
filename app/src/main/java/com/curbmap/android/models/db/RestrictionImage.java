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

package com.curbmap.android.models.db;

import android.location.Location;

/**
 * Stores information related to an image of restriction
 */
public class RestrictionImage {
    private String imagePath;
    private Location mLocation;
    private float azimuth;

    public RestrictionImage(String imagePath, Location mLocation, float azimuth) {

        this.imagePath = imagePath;
        this.mLocation = mLocation;
        this.azimuth = azimuth;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getThumbnailPath() {
        return this.imagePath.replace(".jpg", "_tmb.jpg");
    }

    public Location getmLocation() {
        return mLocation;
    }

    public float getAzimuth() {
        return azimuth;
    }
}
