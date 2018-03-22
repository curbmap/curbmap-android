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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * A polyline contains a list of coordinates
 * Each coordinates represents a point on the polyline
 */
public class Polyline {

    public List<LatLng> coordinatesList;

    //meant to help us do unit testing
    //this should be removed once unit testing is better...
    public Polyline() {

    }

    /**
     * @param coordinatesList The list of coordinates describing the polyline
     *                        For example, if we have five sequential coordinates in the
     *                        coordinatesList,
     *                        this means we connect straight lines between every subsequent pair of
     *                        coordinates to form the polyline.
     */
    public Polyline(List<LatLng> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public List<LatLng> getPolyline() {
        return coordinatesList;
    }

    public void setPolyline(List<LatLng> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }


    /**
     * Converts a Polyline object into the coordinates object
     * which is needed to create a RestrictionText object
     * each array of doubles in the result has size two,
     * the value of its elements correspond to latitude and longitude
     * @return An ArrayList of arrays of two doubles corrresponding to coordinates
     */
    public ArrayList<double[]> getAsCoordinates () {
        ArrayList<double[]> coordinates = new ArrayList<>();
        for (LatLng latLng : this.coordinatesList) {
            double[] array = {latLng.latitude, latLng.longitude};
            coordinates.add(array);
        }
        return coordinates;
    }

}
