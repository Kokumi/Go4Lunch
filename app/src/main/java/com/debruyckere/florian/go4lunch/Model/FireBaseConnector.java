package com.debruyckere.florian.go4lunch.Model;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class FireBaseConnector {

    public void getColleague(OnCompleteListener pListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Colleague")
                .get()
                .addOnCompleteListener(pListener);
    }

    public void addWish(OnSuccessListener<DocumentReference> pSuccessListener, OnFailureListener pFailListener, Wish pWish){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> wish = new HashMap<>();
        wish.put("colleagueId",pWish.getColleague().getId());
        wish.put("date", Calendar.getInstance().getTime());
        wish.put("restaurantAdresse",pWish.getRestaurant().getAddress());

        db.collection("Wish")
                .add(wish)
                .addOnSuccessListener(pSuccessListener)
                .addOnFailureListener(pFailListener);
    }

    public void getWish(OnCompleteListener pListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Wish")
                .get()
                .addOnCompleteListener(pListener);
    }

    public void getUserLocation(Context pContext, OnCompleteListener pListener){
        FusedLocationProviderClient FusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(pContext);

        if(ContextCompat.checkSelfPermission(pContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            FusedLocationProviderClient.getLastLocation().addOnCompleteListener(pListener);
        }
    }

    /*
    // Create a new user with a first and last name
Map<String, Object> user = new HashMap<>();
user.put("first", "Ada");
user.put("last", "Lovelace");
user.put("born", 1815);

// Add a new document with a generated ID
db.collection("users")
        .add(user)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
     */

    public void getRestaurantsData(OnCompleteListener pListener, Context pContext){
        PlacesClient client = Places.createClient(pContext);

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,
                Place.Field.TYPES,Place.Field.RATING,Place.Field.PHOTO_METADATAS,Place.Field.ADDRESS/*,Place.Field.OPENING_HOURS*/);
        FindCurrentPlaceRequest currentRequest = FindCurrentPlaceRequest.builder(placeFields).build();

        if(ContextCompat.checkSelfPermission(pContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            final Task<FindCurrentPlaceResponse> placeResponse = client.findCurrentPlace(currentRequest);
            placeResponse.addOnCompleteListener(pListener);
        }
    }

    public void getRestaurantPhoto(OnSuccessListener pListener, Context pContext, Place pPlaces){
        PlacesClient client = Places.createClient(pContext);

        PhotoMetadata photoMetadata = pPlaces.getPhotoMetadatas().get(0);

        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(50)
                .setMaxHeight(30)
                .build();

        client.fetchPhoto(photoRequest).addOnSuccessListener(pListener);
    }

}
