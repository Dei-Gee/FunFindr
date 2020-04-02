package com.example.funfindr.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.funfindr.R;
import com.example.funfindr.database.models.Favorite;
import com.example.funfindr.utilites.handlers.DatabaseHandler;
import com.example.funfindr.utilites.handlers.SharedPreferencesManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GoogleMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    /* GLOBAL VARIABLES --start-- */

    GoogleMap gMap;
    private final String MYPREFERENCES= "MyPrefs";
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> autocompletePredictionList;
    private Location lastKnownLocation;
    private LocationCallback locationCallback;
    Bundle mapFavoriteBundle = new Bundle();



    MarkerOptions markerOptions = new MarkerOptions();
    Marker mapMarker;

    // views
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button buttonFind;
    private FloatingActionButton fab;
    private RippleBackground rippleBg;
    private LatLng currentMarkerLocation;


    /* GLOBAL VARIABLES --end-- */


    /* CONSTANTS --start-- */
    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 17f;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    /* CONSTANTS --turtorial-- */



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_google_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SQLiteDatabase database = DatabaseHandler.getWritable(getActivity());

        materialSearchBar = getActivity().findViewById(R.id.searchBarMap);
        buttonFind = getActivity().findViewById(R.id.buttonMapButton);
        rippleBg = getActivity().findViewById(R.id.ripple_bg);

        final SharedPreferences sharedPreferences = SharedPreferencesManager.newPreferences(MYPREFERENCES, getContext());



        final GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.mapType(gMap.MAP_TYPE_NORMAL).compassEnabled(true).liteMode(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();

        mapFragment.getMapAsync(this);

        /* EVENT LISTENERS */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        Places.initialize(getContext(), getString(R.string.GOOGLE_API_KEY));
        placesClient = Places.createClient(getContext());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // MATERIAL SEARCH BAR EVENT LISTENERS
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
//                getActivity().startSearch(text.toString(), true, null, true);
                getLocationFromSearchbar(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.closeSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                autocompletePredictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < autocompletePredictionList.size(); i++) {
                                    AutocompletePrediction prediction = autocompletePredictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= autocompletePredictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = autocompletePredictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG); // to get place coordinates
                List<Place.Field> placeFieldsTypes = Arrays.asList(Place.Field.TYPES); // to get place types

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                FetchPlaceRequest fetchPlaceRequestTypes = FetchPlaceRequest.builder(placeId, placeFieldsTypes).build();

                // fetching the latitude and longitud of the place(s) fethced
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag", "Place found: " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();
                        if (latLngOfPlace != null) {
                            currentMarkerLocation = latLngOfPlace;
                            markerOptions.position(currentMarkerLocation);
                            markerOptions.title(currentMarkerLocation.latitude + " " + currentMarkerLocation.longitude);
                            gMap.clear();
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarkerLocation, DEFAULT_ZOOM));
                            mapMarker = gMap.addMarker(markerOptions);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("mytag", "place not found: " + e.getMessage());
                            Log.i("mytag", "status code: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });


        // FIND BUTTON LISTENER
        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMarkerLocation = gMap.getCameraPosition().target;
                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();
//                        startActivity(new Intent(MapActivity.this, MainActivity.class));
//                        finish();
                        // If the bundle is not empty
                        if(mapFavoriteBundle != null)
                        {
                            String userId = SharedPreferencesManager.getString(sharedPreferences, "_id");
                            Favorite newFavorite = new Favorite();
                            newFavorite.setUserId(userId);
                            newFavorite.setAddress(mapFavoriteBundle.getString("address"));
                            newFavorite.setLocality(mapFavoriteBundle.getString("locality"));
                            newFavorite.setPostalCode(mapFavoriteBundle.getString("postal_code"));
                            newFavorite.setAdmin(mapFavoriteBundle.getString("admin"));
                            newFavorite.setSubAdmin(mapFavoriteBundle.getString("sub_admin"));
                            newFavorite.setCountryName(mapFavoriteBundle.getString("country_name"));

                            if(DatabaseHandler.addFavorite(database, newFavorite))
                            {
                                Toast.makeText(getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to add to favorites!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarkerLocation, DEFAULT_ZOOM));
                        gMap.setTrafficEnabled(true);
                        gMap.setMapType(2);
                        materialSearchBar.closeSearch();
                        materialSearchBar.clearSuggestions();
                    }
                }, 3000);

            }
        });

    }

    /**
     * Override of the method that triggers when the map is ready
     * @param googleMap GoogleMap Object
     */
    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        gMap = googleMap;
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        currentMarkerLocation  = gMap.getCameraPosition().target;

        // check if mapView is null and then modify the location button
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 220);

            locationButton.setBackground(getResources().getDrawable(R.drawable.round_circle));
            locationButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_gps_fixed_blue_24dp);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });



        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(getActivity(), 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible())
                    materialSearchBar.clearSuggestions();
                if (materialSearchBar.isSearchOpened())
                    materialSearchBar.closeSearch();
                return false;
            }
        });

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                currentMarkerLocation = latLng;
                markerOptions.position(currentMarkerLocation);
                gMap.clear();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarkerLocation, DEFAULT_ZOOM));
                mapMarker = gMap.addMarker(markerOptions);
                getMarkerDetails(mapMarker);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == Activity.RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    /**
     * Gets the location of the device on the planet
     */
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        lastKnownLocation = locationResult.getLastLocation();
                                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(getActivity(), "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    /**
     * Displays information when the marker is clicked
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the database from the marker.
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
     * This sets and gets details from the marker using Geocoder
     * @param marker The marker object
     */
    public void getMarkerDetails(Marker marker)
    {
        if(marker.equals(mapMarker))
        {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            try{
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1);


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String admin = addresses.get(0).getAdminArea();
                String subAdmin = addresses.get(0).getSubAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d("HERE => ", addresses.get(0).getAddressLine(0));

                // ADD DATA TO BUNDLE
                mapFavoriteBundle.putString("address", address);
                mapFavoriteBundle.putString("locality", city);
                mapFavoriteBundle.putString("admin", admin);
                mapFavoriteBundle.putString("sub_admin", subAdmin);
                mapFavoriteBundle.putString("postal_code", postalCode);
                mapFavoriteBundle.putString("country_name", country);

                if(address != null)
                {
                    marker.setTitle(address);
                }
            }
            catch(IOException e){
                Log.d("ADDRESS ERROR => ", e.getMessage());
            }
        }

    }

    /**
     * This gets the location from the name of whatever is typed into the searchbar
     * @param searchString The search string
     */
    public void getLocationFromSearchbar(CharSequence searchString)
    {

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString.toString(), 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            String fullAddress = address.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = address.getLocality();
            String admin = address.getAdminArea();
            String subAdmin = address.getSubAdminArea();
            String country = address.getCountryName();
            String postalCode = address.getPostalCode();
            String knownName = address.getFeatureName(); // Only if available else return NULL

            Log.d(TAG, ("geoLocate: found a location: ").toUpperCase() + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            // ADD DATA TO BUNDLE
            mapFavoriteBundle.putString("address", fullAddress);
            mapFavoriteBundle.putString("locality", city);
            mapFavoriteBundle.putString("admin", admin);
            mapFavoriteBundle.putString("sub_admin", subAdmin);
            mapFavoriteBundle.putString("postal_code", postalCode);
            mapFavoriteBundle.putString("country_name", country);

            currentMarkerLocation = new LatLng(address.getLatitude(), address.getLongitude());
            markerOptions.position(currentMarkerLocation);
            markerOptions.title(address.getAddressLine(0));
            gMap.clear();
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarkerLocation, DEFAULT_ZOOM));
            gMap.addMarker(markerOptions);
        }
    }

}
