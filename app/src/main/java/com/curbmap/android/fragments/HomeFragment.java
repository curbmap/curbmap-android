package com.curbmap.android.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.curbmap.android.controller.MapController;
import com.curbmap.android.models.Compass;
import com.curbmap.android.models.OpenLocationCode;
import com.curbmap.android.models.db.Polyline;
import com.curbmap.android.models.db.User;
import com.curbmap.android.models.db.UserAppDatabase;
import com.curbmap.android.models.db.UserDao;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback {
    private static final int REQUEST_IMAGE_CAPTURE = 111;

    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    //minimum time in miliseconds before update location
    private static final long MIN_TIME = 200;

    //minimum distance user moved in meters before update location
    private static final float MIN_DISTANCE = 0.5f;
    int DEFAULT_ZOOM_LEVEL = 18;
    MapView mapView;
    View view;
    String polylineString;
    String TAG = "HomeFragment";
    GoogleMap map;
    Context mContext;// Define a listener that responds to location updates
    public List<LatLng> coordinatesList = new ArrayList<>();
    int numberOfMarkers = 0;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;
    Location mLocation;
    String imagePath;
    Compass compass;
    float azimuth;

    Button addResBtn;
    Button clearBtn;
    Button writeResBtn;

    //global var because onlocationchanged
    //seemed unable to use neither getContext() nor pass in string username
    //so this was the only option to pass username in : eg through global
    //variable
    String username = "curbmaptest";

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            mLocation = location;
            //commented out because we want user to manually choose their location
            Log.d("locationchanged", "yay");
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
            if (location != null) {
                //this part keeps moving to users location
                //what if user does not want this behavior?
                //answer: user would have to disable location permissions.
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
                map.animateCamera(cameraUpdate);
                locationManager.removeUpdates(this);

                //MapController.getMarkers(map, coordinatesList, username);

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

        //initialize the compass..
        //please note the compass takes some time to initialize
        //right now we do not have to do any async because we assume that the compass will
        //initialize by the time the user presses the snap restriction button
        //since that is when we record the azimuth
        compass = new Compass(this.getContext());
        compass.start();

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


        //the search box uses Google Places Autocomplete API
        //...launching a fullscreen intent.
        //...whatever the user selects will be processed in onActivityResult()
        TextView searchBox = view.findViewById(R.id.searchBox);
        searchBox.setOnClickListener(new View.OnClickListener() {
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
                LatLng latLng = place.getLatLng();
                ;
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            String filePath = imagePath;
            File file = new File(filePath);

            Log.d(TAG, "reached here ");
            //bookmark

            String olcString = "";

            //12 is about the size of a parking spot
            final int OLC_LENGTH = 12;
            if (mLocation != null) {
                OpenLocationCode code = new OpenLocationCode(
                        mLocation.getLatitude(),
                        mLocation.getLongitude(),
                        OLC_LENGTH
                );
                olcString = code.getCode();
            }


            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            final String BASE_URL = "https://curbmap.com:50003/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            CurbmapRestService service = retrofit.create(CurbmapRestService.class);


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            RequestBody olc = RequestBody.create(MediaType.parse("text/plain"), olcString);
            RequestBody bearing = RequestBody.create(
                    MediaType.parse("text/plain"),
                    String.valueOf(azimuth));

            Log.d("olc is", olcString);


            UserAppDatabase db = Room.databaseBuilder(
                    getContext(),
                    UserAppDatabase.class,
                    "user")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            final UserDao userDao = db.getUserDao();

            username = "curbmaptest";
            String session = "x";

            User user = userDao.getUser();
            //only if user is signed in
            if (user != null) {
                username = user.getUsername();
                session = user.getSession();
            }

            Log.d("username", username);
            Log.d("session", session);

            //the current map area displayed on user's phone
            Call<String> results = service.doUploadImage(
                    username,
                    session,
                    body,
                    olc,
                    bearing
            );

            results.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG,response.body());
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Succeeded in uploading image.");
                        Toast.makeText(view.getContext(),
                                "Succeeded in uploading image.",
                                Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Log.d(TAG, "Server rejected image upload.");
                        Toast.makeText(view.getContext(),
                                "Server rejected image upload." +
                                response.body(),
                                Toast.LENGTH_LONG)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "Failed to upload image.");
                    t.printStackTrace();
                    Toast.makeText(view.getContext(),
                            "Failed to upload image.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }


    /**
     * Encodes an image so that we can upload it to server
     *
     * @param bitmap Bitmap image
     * @return Base64 encoded string
     */
    public String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
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
                UserAppDatabase db = Room.databaseBuilder(
                        getContext(),
                        UserAppDatabase.class,
                        "user")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
                final UserDao userDao = db.getUserDao();

                String username = "curbmaptest";

                User user = userDao.getUser();
                //only if user is signed in
                if (user != null) {
                    username = user.getUsername();
                }

                Log.d("username", username);

                //MapController.getMarkers(map, coordinatesList, username);
            }
        });

        addResBtn = view.findViewById(R.id.addRestrictionButton);
        clearBtn = view.findViewById(R.id.clearButton);
        writeResBtn = view.findViewById(R.id.addRestrictionButtonForm);
        writeResBtn.setVisibility(View.INVISIBLE);


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
                    //make the buttons visible
                    writeResBtn.setVisibility(View.VISIBLE);

                }

            }
        });

        writeResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Polyline polyline = new Polyline(coordinatesList);
                polylineString = gson.toJson(polyline);
                Bundle bundle = new Bundle();
                bundle.putString("polylineString", polylineString);
                AddRestrictionFragment addRestrictionFragment = new AddRestrictionFragment();
                addRestrictionFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , addRestrictionFragment)
                        .commit();
            }
        });


        addResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MapController.checkCameraPermission(getActivity(), getContext())) {
                    if (MapController.checkWritePermission(getActivity(), getContext())) {

                        String storageState = Environment.getExternalStorageState();
                        if (storageState.equals(Environment.MEDIA_MOUNTED)) {

                            String path = Environment.getExternalStorageDirectory() + "/Android/data/" + getContext().getPackageName() + "/files/curbmap.jpg";
                            imagePath = path;

                            File _photoFile = new File(path);
                            Log.d("the path is ", path);
                            try {
                                if (_photoFile.exists() == false) {
                                    _photoFile.getParentFile().mkdirs();
                                    _photoFile.createNewFile();
                                    Log.d(TAG, "created new file");
                                } else {
                                    _photoFile.delete();
                                    _photoFile.getParentFile().mkdirs();
                                    _photoFile.createNewFile();
                                    Log.d(TAG, "replaced old file");
                                }

                            } catch (IOException e) {
                                Log.e(TAG, "Could not create file.", e);
                            }
                            Log.i(TAG, path);

                            Uri _fileUri = Uri.fromFile(_photoFile);


                            Log.d("compass azimuth", String.valueOf(compass.getAzimuth()));
                            azimuth = compass.getAzimuth();

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, _fileUri);
                            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                            }
                        } else {
                            new AlertDialog.Builder(getContext())
                                    .setMessage("External Storage (SD Card) is required.\n\nCurrent state: " + storageState)
                                    .setCancelable(true).create().show();
                        }
                    }
                }
            }
        });


        //clear the markers that the user drew whenever click 'clear' button
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove all markers including restrictions and user created markers from the map
                map.clear();

                //clear the old user created markers so that we do not redraw them
                coordinatesList.clear();

                //clear the buttons since we won't need them now
                writeResBtn.setVisibility(View.INVISIBLE);

                //reset numberOfMarkers so that it can go to 2 can display the buttons in future
                numberOfMarkers = 0;
            }
        });


    }


}