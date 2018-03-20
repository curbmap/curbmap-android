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
}
