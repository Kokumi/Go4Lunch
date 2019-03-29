package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class DetailRestaurantActivity extends AppCompatActivity {

    private PlacesClient mPlacesClient ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
    }

    private void getData(){
        String placesId = "ChIJpc2-9j4pw0cRmdgmJtzVNN0";

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placesId,placeFields).build();

        mPlacesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                Place place = response.getPlace();
                Log.i("fetch", " Place Found: " + place.getName()+" LAT: "+place.getLatLng());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Fetch","Place unknow: "+e.getMessage());
            }
        });
    }
}
