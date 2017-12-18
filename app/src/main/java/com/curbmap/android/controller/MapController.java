package com.curbmap.android.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MapController {
    private static final String TAG = "MapController";

    /**
     * Gets the coordinates of screen,
     * requests server for data in the screen area,
     * then asks drawMarkers to draw the markers on the map
     * @param map the Google Maps object
     * @param coordinatesList the list of coordinates
     */
    public static void getMarkers(final GoogleMap map, final List<LatLng> coordinatesList) {

        //add the markers to the map
        //warning: could not separate this function out into a new class
        //...because then we would have to declare map as final
        //...todo: figure out how to put this in a separate class
        /*
        (lat1,      NE
         lng1)+----+
              |    |
              +----+(lat2,
             SW      lng2)
         */
        LatLngBounds curScreen = map.getProjection()
                .getVisibleRegion().latLngBounds;
        double lat1 = curScreen.southwest.latitude;
        double lng1 = curScreen.northeast.longitude;
        double lat2 = curScreen.northeast.latitude;
        double lng2 = curScreen.southwest.longitude;

        //todo: Rest API must ONLY be called by Room!!!
        //we should NEVER EVER call the Rest API directly!!!
        //this way the interaction will be 100% offline-friendly
        final String BASE_URL = "https://curbmap.com:50003";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CurbmapRestService service = retrofit.create(CurbmapRestService.class);

        //the current map area displayed on user's phone
        Call<String> results = service.doAreaPolygon(
                "curbmaptest",
                lat1,
                lng1,
                lat2,
                lng2);

        results.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Succeeded in getting markers");
                    map.clear();
                    PolylineOptions line =
                            new PolylineOptions()
                                    .addAll(coordinatesList)
                                    .width(5)
                                    .color(Color.RED);
                    map.addPolyline(line);
                    for (LatLng x : coordinatesList) {
                        map.addMarker(new MarkerOptions().position(x));
                    }
                    MapController.updateMap(response, map);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failed to get markers.");
                t.printStackTrace();
            }
        });
    }

    /**
     * When receive a response from the server call GET /AreaPolygon
     * Updates the map with the responses by sending a call to
     * drawMarker to draw a marker for every single restriction
     * @param response the response from the server
     * @param map the Google Map object
     */
    private static void updateMap(Response<String> response, GoogleMap map) {
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



    /**
     * Draws a marker on the map to represent a type of restriction
     * this is called after sending a get request to the server
     *   asking the server for restriction points
     *
     * @param type    Type of restriction. Can be "sweep", "red", "hyd" or "ppd"
     * @param pLatLng The coordinates of the restriction
     */
    private static void drawMarkers(String type, LatLng pLatLng, GoogleMap map) {

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


    /**
     * Checks for location permission
     * If not granted, makes an alert requesting user to grant location permission.
     * @return true if location permission is granted, false otherwise
     */
    public static boolean checkLocationPermission(final Activity activity, final Context context) {


        final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle("Location permissions")
                        .setMessage(R.string.location_request)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
