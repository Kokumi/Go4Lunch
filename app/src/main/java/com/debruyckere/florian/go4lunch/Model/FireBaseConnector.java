package com.debruyckere.florian.go4lunch.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class FireBaseConnector {
    private static final int RC_SIGN_IN = 123;

    private DatabaseReference mDatabase;

    public FireBaseConnector(Context pContext){
        FirebaseApp.initializeApp(pContext);
        mDatabase = FirebaseDatabase.getInstance().getReference();}


    public ArrayList<Colleague> getColleagues(){
        final ArrayList<Colleague> toReturn=new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Colleague post = dataSnapshot.getValue(Colleague.class);
                toReturn.add(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE","loadColleague:onCancelled",databaseError.toException());
            }
        });


        return toReturn;
    }
}
