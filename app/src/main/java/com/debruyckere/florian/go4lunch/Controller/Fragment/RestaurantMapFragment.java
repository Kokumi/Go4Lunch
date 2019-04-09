package com.debruyckere.florian.go4lunch.Controller.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debruyckere.florian.go4lunch.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;


public class RestaurantMapFragment extends BaseFragment implements OnMapReadyCallback {

    private Context mContext;
    private MapView mMapView;
    private GoogleMap mMap;
    private PlacesClient mPlacesClient ;
    private Boolean mLocationPermissionGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnowLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 14;



    public RestaurantMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment RestaurantMapFragment.
     */
    public static RestaurantMapFragment newInstance() {
        RestaurantMapFragment fragment = new RestaurantMapFragment();

        //= Places.createClient(getActivity().getApplicationContext())

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_map, container, false);

        Places.initialize(mContext,"AIzaSyCJsENv9ksTlY-n1rjZxFW412I64aVqbvc");
        mPlacesClient = Places.createClient(mContext);

        mMapView = view.findViewById(R.id.fragment_mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try{
            MapsInitializer.initialize(mContext);
        }catch (Exception e){
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();

        getDeviceLocation();

    }

    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults){

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();

    }

    private void updateLocationUI(){
        if(mMap == null){
            return;
        }
        try{
            if(mLocationPermissionGranted){
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else{
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                //mLastKnowLocation = null;
                getLocationPermission();
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void getDeviceLocation(){

        try{
            if(mLocationPermissionGranted){
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            mLastKnowLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                    new LatLng(mLastKnowLocation.getLatitude(),mLastKnowLocation.getLongitude())
                                    ));
                            mMap.setMinZoomPreference(16f);
                        }else {
                            Log.e("TASKERROR",task.getException().getMessage());
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }

        getPlaces();
    }

    public void getPlaces(){
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.TYPES);

        FindCurrentPlaceRequest currentRequest = FindCurrentPlaceRequest.builder(placeFields).build();

        if(ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            final Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(currentRequest);

            placeResponse.addOnSuccessListener(new OnSuccessListener<FindCurrentPlaceResponse>() {
                @Override
                public void onSuccess(FindCurrentPlaceResponse findCurrentPlaceResponse) {
                    for(PlaceLikelihood placeLikelihood : findCurrentPlaceResponse.getPlaceLikelihoods()){
                        if(placeLikelihood.getPlace().getTypes().toString().contains("RESTAURANT")) {
                            Log.i("Map","add marker for: "+placeLikelihood.getPlace().getName());
                            mMap.addMarker(new MarkerOptions().position(placeLikelihood.getPlace().getLatLng())
                                    .title(placeLikelihood.getPlace().getName() + " " + placeLikelihood.getPlace().getTypes()));
                        }

                        /*Log.i("PlaceCurrent","place has likelihood: %f"+placeLikelihood.getPlace().getName()
                                +" "+placeLikelihood.getLikelihood());*/
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("PlaceCurrent","error: "+e.getMessage());
                }
            });
        }
        else{
            Log.e("PlaceCurrent","Permission Denied");
        }

    }
}
