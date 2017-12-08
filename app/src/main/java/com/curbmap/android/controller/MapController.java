package com.curbmap.android.controller;

import com.curbmap.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Response;

public class MapController {
    /**
     * Draws a marker on the map
     *
     * @param type    Type of restriction. Can be "sweep", "red", "hyd" or "ppd"
     * @param pLatLng The coordinates of the restriction
     */
    public static void drawMarkers(String type, LatLng pLatLng, GoogleMap map) {

        //GoogleMap map = this.map;

        //add the icon for each restriction type
        switch (type) {
            case "sweep":
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.type_sweep))
                        .position(pLatLng));
                break;
            case "red":
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.type_no_parking))
                        .position(pLatLng));
                break;
            case "hyd":
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.type_hydrant))
                        .position(pLatLng));
                break;
            case "ppd":
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.type_permit))
                        .position(pLatLng));
                break;
        }
    }


    public static void updateMap(Response<String> response, GoogleMap map) {
        String CurbmapPolylineObjectList = response.body();
        JsonParser parser = new JsonParser();
        JsonArray pointsArray = parser.parse(CurbmapPolylineObjectList).getAsJsonArray();
        for (JsonElement point : pointsArray) {
            JsonObject p = point.getAsJsonObject();
            JsonObject multiPointProperties = p.getAsJsonObject("multiPointProperties");
            int numberOfPoints = multiPointProperties.getAsJsonArray("points").size();

            if (numberOfPoints > 0) {
                JsonArray restrictions = (JsonArray) multiPointProperties
                        .getAsJsonArray("restrs")
                        .get(0);
                if (restrictions.size() > 0) {
                    restrictions = (JsonArray) restrictions.get(0);
                    JsonArray coordinates = multiPointProperties.getAsJsonArray("points");
                    for (int j = 0; j < numberOfPoints; j++) {
                        JsonElement restriction = restrictions.get(1);
                        JsonArray coordinate = (JsonArray) coordinates.get(numberOfPoints - 1);
                        JsonElement latitude = coordinate.get(0);
                        double lat = latitude.getAsDouble();
                        JsonElement longitude = coordinate.get(1);
                        double lng = longitude.getAsDouble();
                        LatLng pLatLng = new LatLng(lng, lat);
                        String type = restriction.toString();
                        type = type.replaceAll("^\"|\"$", "");
                        MapController.drawMarkers(type, pLatLng, map);
                    }
                }
            }
            //loop n: json[0].multiPointProperties.points[0].length
            //restriction: json[0].multiPointProperties.restrs[0][0][n]
            //latitude: json[0].multiPointProperties.points[n][0]
            //longitude: json[0].multiPointProperties.points[n][1]
        }

    }
}
