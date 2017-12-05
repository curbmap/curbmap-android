package com.curbmap.android.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.curbmap.android.controller.MapController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 10;
    MapView mapView;
    View view;
    String TAG = "HomeFragment";
    GoogleMap map;
    Context mContext;// Define a listener that responds to location updates
    int numberOfMarkers = 0;
    private String mCoordinateOfRestriction;
    private String mEndCoordinate;
    private LatLng mCoordinateLatLng;
    private LatLng mEndCoordinateLatLng;
    private LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
                map.animateCamera(cameraUpdate);
                locationManager.removeUpdates(this);
                getMarkers();
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        this.mContext = this.getContext();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    locationListener);
        }

        Button updateButton = view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMarkers();
            }
        });

        Button addRestrictionButton = view.findViewById(R.id.addRestrictionButton);
        addRestrictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCoordinateOfRestriction == null) {
                    Toast.makeText(view.getContext(),
                            R.string.warn_select_restriction,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("coordinatesOfRestriction", mCoordinateOfRestriction);
                    bundle.putString("endCoordinates", mEndCoordinate);
                    AddRestrictionFragment addRestrictionFragment = new AddRestrictionFragment();
                    addRestrictionFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , addRestrictionFragment)
                            .commit();
                }
            }
        });

        return view;
    }

    /**
     * Checks for location permission
     * If not granted, makes an alert requesting user to grant location permission.
     * @return true if location permission is granted, false otherwise
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this.getContext())
                        .setTitle("Location permissions")
                        .setMessage(R.string.location_request)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        //locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME,
                        MIN_DISTANCE,
                        locationListener);
            }
        }

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);

        }

        //the default coordinates, in case the fragment_user does not enable location services
        //LatLng laLatLng = new LatLng(34.040011, -118.259419);
        LatLng laLatLng = new LatLng(34.0377002544831, -118.248260994197);
        map.moveCamera(CameraUpdateFactory.newLatLng(laLatLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (numberOfMarkers == 0 || numberOfMarkers == 2) {
                    map.clear();
                    numberOfMarkers = 0;

                    map.addMarker(new MarkerOptions().position(point));
                    mCoordinateOfRestriction = point.toString();
                    mCoordinateLatLng = point;
                    numberOfMarkers = 1;
                } else if (numberOfMarkers == 1) {
                    map.addMarker(new MarkerOptions().position(point));

                    mEndCoordinate = point.toString();
                    mEndCoordinateLatLng = point;
                    numberOfMarkers = 2;

                    PolylineOptions line =
                            new PolylineOptions().add(mCoordinateLatLng, mEndCoordinateLatLng)
                                    .width(5).color(Color.RED);
                    map.addPolyline(line);
                }

            }
        });
    }


    /**
     * Gets the coordinates of screen,
     * requests server for data in the screen area,
     * then asks drawMarkers to draw the markers on the map
     */
    public void getMarkers() {

        //add the markers to the map
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
        final String BASE_URL = getString(R.string.MAP_API_BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        CurbmapRestService service = retrofit.create(CurbmapRestService.class);
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
}