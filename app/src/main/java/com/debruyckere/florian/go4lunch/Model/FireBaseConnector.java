package com.debruyckere.florian.go4lunch.Model;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class FireBaseConnector {

    /**
     * get all colleague
     * @param pListener listener to handle answer
     */
    public void getColleague(OnCompleteListener<QuerySnapshot> pListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Colleague")
                .get()
                .addOnCompleteListener(pListener);
    }

    /**
     * search a colleague by it's ID
     * @param pListener listener to handle answer
     * @param pId ID of the wanted colleague
     */
    public void getColleagueById(OnCompleteListener<DocumentSnapshot> pListener, String pId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Colleague").document(pId)
                .get()
                .addOnCompleteListener(pListener);
    }

    /**
     * add wish to database
     * @param pSuccessListener listener to handle success
     * @param pFailListener listener to handle failure
     * @param pWish wish to added
     */
    public void addWish(OnSuccessListener<DocumentReference> pSuccessListener, OnFailureListener pFailListener, Wish pWish){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> wish = new HashMap<>();
        wish.put("colleagueId",pWish.getColleague().getId());
        wish.put("date", Calendar.getInstance().getTime());
        wish.put("restaurantAdresse",pWish.getRestaurant().getAddress());
        wish.put("restaurantId",pWish.getRestaurant().getId());

        db.collection("Wish")
                .add(wish)
                .addOnSuccessListener(pSuccessListener)
                .addOnFailureListener(pFailListener);
    }

    /**
     * add Like to Database
     * @param pCompleteListener listener to handle answer
     * @param pColleagueId colleague's Id who liked
     * @param pRestaurantId restaurant's id liked
     */
    public void addLike(OnCompleteListener<DocumentReference> pCompleteListener,String pColleagueId, String pRestaurantId){
        FirebaseFirestore db =FirebaseFirestore.getInstance();

        Map<String,String> like = new HashMap<>();
        like.put("colleagueId",pColleagueId);
        like.put("restaurantId",pRestaurantId);

        db.collection("Like")
                .add(like)
                .addOnCompleteListener(pCompleteListener);
    }

    /**
     * register new user
     * @param pCompleteListener listener to handle answer
     * @param pName user's name
     * @param pId user's Id
     * @param pPhotoUri user's Photo URI
     */
    public void registerColleague(OnCompleteListener<Void> pCompleteListener, String pName, String pId, String pPhotoUri){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,String> colleagueMap = new HashMap<>();
        colleagueMap.put("name",pName);
        colleagueMap.put("photo",pPhotoUri);

        db.collection("Colleague").document(pId)
                .set(colleagueMap)
                .addOnCompleteListener(pCompleteListener);
    }

    /**
     * Get all Wish
     * @param pListener listener to handle answer
     */
    public void getWish(OnCompleteListener<QuerySnapshot> pListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Wish")
                .get()
                .addOnCompleteListener(pListener);
    }

    /**
     * search wish of a colleague
     * @param pListener listener to handle answer
     * @param pId colleague's ID
     */
    public void GetWishOfColleague(OnCompleteListener<QuerySnapshot> pListener, String pId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Wish")
                .whereEqualTo("colleagueId",pId)
                .get()
                .addOnCompleteListener(pListener);
    }

    /**
     * search wish about a restaurant
     * @param pListener listener to handle answer
     * @param pAddress restaurant's address
     */
    public void getWishByAddress(OnCompleteListener<QuerySnapshot> pListener, String pAddress){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Wish")
                .whereEqualTo("restaurantAdresse",pAddress)
                .get()
                .addOnCompleteListener(pListener);
    }

    /**
     * search Like of a colleague
     * @param pListener listener to handle answer
     * @param pColleagueId colleague's ID
     */
    public void getLike(OnCompleteListener<QuerySnapshot> pListener, String pColleagueId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Like")
                .whereEqualTo("colleagueId",pColleagueId)
                .get()
                .addOnCompleteListener(pListener);
    }

    /**
     * get User's Location
     * @param pContext Context needed
     * @param pListener listener to handle answer
     */
    public void getUserLocation(Context pContext, OnCompleteListener<Location> pListener){
        FusedLocationProviderClient FusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(pContext);

        if(ContextCompat.checkSelfPermission(pContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            FusedLocationProviderClient.getLastLocation().addOnCompleteListener(pListener);
        }
    }

    /**
     * get Data of all nearby places
     * @param pListener listener to handle answer
     * @param pContext Context needed
     */
    public void getRestaurantsData(OnCompleteListener<FindCurrentPlaceResponse> pListener, Context pContext){
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

    /**
     * Search Data of one place
     * @param pListener listener to handle answer
     * @param pContext Context needed
     * @param pId place's ID
     */
    public void getRestaurantData(OnCompleteListener<FetchPlaceResponse> pListener, Context pContext, String pId){
        PlacesClient client = Places.createClient(pContext);

        final List<Place.Field> placesFields= Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,
                Place.Field.TYPES,Place.Field.RATING,Place.Field.PHOTO_METADATAS,Place.Field.OPENING_HOURS,
                Place.Field.ADDRESS_COMPONENTS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(pId,placesFields).build();

        client.fetchPlace(request).addOnCompleteListener(pListener);
    }

    /**
     * search the photo of a place
     * @param pListener listener to handle answer
     * @param pContext Context needed
     * @param pPlaces place
     */
    public void getRestaurantPhoto(OnCompleteListener<FetchPhotoResponse> pListener, Context pContext, Place pPlaces){
        PlacesClient client = Places.createClient(pContext);

        if(pPlaces.getPhotoMetadatas() != null) {
            PhotoMetadata photoMetadata = pPlaces.getPhotoMetadatas().get(0);
            //String attribution = photoMetadata.getAttributions();

            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .build();

            client.fetchPhoto(photoRequest).addOnCompleteListener(pListener);
        }
    }

}
