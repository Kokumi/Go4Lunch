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

import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;


public class RestaurantMapFragment extends BaseFragment implements OnMapReadyCallback {

    private Context mContext;
    //private MapView mMapView;
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
        return new RestaurantMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() != null)
        mContext = getActivity().getApplicationContext();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_map, container, false);

        Places.initialize(mContext,"AIzaSyCJsENv9ksTlY-n1rjZxFW412I64aVqbvc");
        mPlacesClient = Places.createClient(mContext);

        MapView mapView = view.findViewById(R.id.fragment_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try{
            MapsInitializer.initialize(mContext);
        }catch (Exception e){
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

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
            updateLocationUI();
        } else {
            if(getActivity()!= null)
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
        /*switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }*/
        if(requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mLocationPermissionGranted = true;

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
                        if(task.isSuccessful() && task.getResult() != null){
                            mLastKnowLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                    new LatLng(mLastKnowLocation.getLatitude(),mLastKnowLocation.getLongitude())
                                    ));
                            mMap.setMinZoomPreference(16f);
                        }else {
                            if(task.getException() != null)
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
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.TYPES);

        FindCurrentPlaceRequest currentRequest = FindCurrentPlaceRequest.builder(placeFields).build();

        if(ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            final Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(currentRequest);

            placeResponse.addOnSuccessListener(new OnSuccessListener<FindCurrentPlaceResponse>() {
                @Override
                public void onSuccess(FindCurrentPlaceResponse findCurrentPlaceResponse) {
                    for(final PlaceLikelihood placeLikelihood : findCurrentPlaceResponse.getPlaceLikelihoods()){
                        if(placeLikelihood.getPlace().getTypes() != null
                                && placeLikelihood.getPlace().getTypes().toString().contains("RESTAURANT")
                                && placeLikelihood.getPlace().getLatLng() != null) {


                            new FireBaseConnector().getWishByAddress(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.getResult() != null){
                                        MarkerOptions marker;
                                        if(task.getResult().size() > 0){
                                            marker = new MarkerOptions()
                                                    .position(placeLikelihood.getPlace().getLatLng())
                                                    .title(placeLikelihood.getPlace().getName())
                                                    .snippet(placeLikelihood.getPlace().getTypes().get(0).toString())
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                        }else {
                                            marker = new MarkerOptions()
                                                    .position(placeLikelihood.getPlace().getLatLng())
                                                    .title(placeLikelihood.getPlace().getName())
                                                    .snippet(placeLikelihood.getPlace().getTypes().get(0).toString());
                                        }
                                        mMap.addMarker(marker);
                                    }
                                }
                            },placeLikelihood.getPlace().getAddress());


                            Log.i("Map","add marker for: "+placeLikelihood.getPlace().getName());
                            /*mMap.addMarker(new MarkerOptions().position(placeLikelihood.getPlace().getLatLng())
                                    .title(placeLikelihood.getPlace().getName() + " " + placeLikelihood.getPlace().getTypes().get(0)));*/
                        }
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
