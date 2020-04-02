package com.example.funfindr.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.funfindr.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class GoogleMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    /* GLOBAL VARIABLES --start-- */
    GoogleMap mapApi;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15f;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    MarkerOptions markerOptions = new MarkerOptions();
    /* GLOBAL VARIABLES --end-- */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_google_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_gps_fixed_blue_24dp);
        fab.setImageDrawable(drawable);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationPermission();
                getDeviceLocation();
            }
        });

        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.mapType(mapApi.MAP_TYPE_NORMAL).compassEnabled(true).liteMode(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getView().setClickable(true);

        mapFragment.getMapAsync(this);

    }

    /**
     * Override of the method that triggers when the map is ready
     * @param googleMap GoogleMap Object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapApi = googleMap;
        LatLng douglasCollege = new LatLng(49.203771, -122.912636);


        mapApi.addMarker(markerOptions.position(douglasCollege).title("Douglas College"));
        mapApi.moveCamera(CameraUpdateFactory.newLatLng(douglasCollege));
        mapApi.moveCamera(CameraUpdateFactory.newLatLngZoom(douglasCollege, DEFAULT_ZOOM));


        // Allows the map to move to a location that is cliked and place the marker there
        mapApi.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mapApi.clear();

                // Animating to the touched position
                mapApi.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mapApi.addMarker(markerOptions);
            }
        });

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mapApi.setMyLocationEnabled(true);
            mapApi.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }

    /**
     * Gets the location of the device on the planet
     */
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            LatLng currLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mapApi.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
                            mapApi.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, DEFAULT_ZOOM));

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    /**
     * Displays information when the marker is clicked
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount += 1;
            marker.setTag(clickCount);
            return true;
        }

        return false;
    }



    /**
     * Gets the persmission from the user to access their location
     */
    public void getLocationPermission()
    {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionsGranted = true;
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),  permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Event handler for when the user has given permissoins back from the user
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }
}
