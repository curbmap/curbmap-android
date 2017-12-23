package com.curbmap.android.fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curbmap.android.R;
import com.curbmap.android.controller.MapController;
import com.curbmap.android.models.db.Polyline;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback {

    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1;
    int DEFAULT_ZOOM_LEVEL = 18;
    MapView mapView;
    View view;
    String TAG = "HomeFragment";
    GoogleMap map;
    Context mContext;// Define a listener that responds to location updates
    public List<LatLng> coordinatesList = new ArrayList<>();
    int numberOfMarkers = 0;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //commented out because we want user to manually choose their location
            Log.d("locationchanged","yay");
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
            if (location != null) {
                /*
                //commented out so it doesn't keep moving to user's location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
                map.animateCamera(cameraUpdate);
                locationManager.removeUpdates(this);
                MapController.getMarkers(map, coordinatesList);
                */

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

        ImageView menu_icon = (ImageView) view.findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(new View.OnClickListener() {
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



        Button addRestrictionButton = view.findViewById(R.id.addRestrictionButton);
        addRestrictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfMarkers < 2) {
                    Toast.makeText(view.getContext(),
                            R.string.warn_select_restriction,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    Polyline polyline = new Polyline(coordinatesList);
                    String polylineString = gson.toJson(polyline);
                    bundle.putString("polylineString", polylineString);
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


        //the search box uses Google Places Autocomplete API
        //...launching a fullscreen intent.
        //...whatever the user selects will be processed in onActivityResult()
        TextView searchBox = view.findViewById(R.id.searchBox);
        searchBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    //results only within us
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry("US")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    //bias within la
                                    .setBoundsBias(new LatLngBounds(
                                            new LatLng(33.604807, -118.718185),
                                            new LatLng(34.333501, -117.144204)))
                                    .setFilter(typeFilter)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                    Log.e(TAG, e.toString());
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    Log.e(TAG, e.toString());
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place: " + place.getName());
                LatLng latLng = place.getLatLng();;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
                map.animateCamera(cameraUpdate);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, getString(R.string.info_cancel_select_place));
            }
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

        if (MapController.checkLocationPermission(getActivity(), getContext())) {
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

        //the default coordinates, in case the fragment_user_profile does not enable location services
        //LatLng laLatLng = new LatLng(34.040011, -118.259419);
        LatLng laLatLng = new LatLng(34.0377002544831, -118.248260994197);
        map.moveCamera(CameraUpdateFactory.newLatLng(laLatLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_LEVEL));

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.
                if (map != null) {
                    //map.clear();
                }

                MapController.getMarkers(map, coordinatesList);
            }
        });

        final Button addResBtn = view.findViewById(R.id.addRestrictionButton);
        addResBtn.setVisibility(View.GONE);
        
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (numberOfMarkers == 0) {
                    map.addMarker(new MarkerOptions().position(point));
                    coordinatesList.add(point);
                    numberOfMarkers = 1;
                } else {
                    //we can draw a line
                    map.addMarker(new MarkerOptions().position(point));
                    coordinatesList.add(point);
                    numberOfMarkers++;

                    PolylineOptions line =
                            new PolylineOptions()
                                    .addAll(coordinatesList)
                                    .width(5)
                                    .color(Color.RED);
                    map.addPolyline(line);
                }

                if (numberOfMarkers == 2) {
                    addResBtn.setVisibility(View.VISIBLE);

                }

            }
        });
    }





}