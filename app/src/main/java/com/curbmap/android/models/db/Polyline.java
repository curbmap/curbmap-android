package com.curbmap.android.models.db;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * A polyline contains a list of coordinates
 * Each coordinates represents a point on the polyline
 */
public class Polyline {

    public List<LatLng> coordinatesList;

    public Polyline(List<LatLng> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public List<LatLng> getPolyline() {
        return coordinatesList;
    }

    public void setPolyline(List<LatLng> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

}
