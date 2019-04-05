package com.debruyckere.florian.go4lunch.Model;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

    public ArrayList<Colleague> getColleaguesTest(){
        ArrayList<Colleague> result = new ArrayList<>();

        result.add(new Colleague("1","Pilica","Gusto"));

        return result;
    }

    public ArrayList<Restaurant> getRestaurantsData(){
        ArrayList<Restaurant> result = new ArrayList<>();

        result.add(new Restaurant(10,"Le point","12 Leclerc Mouvaux","Français","11h30 - 14H",1));

        return result;
    }

}
