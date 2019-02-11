package com.debruyckere.florian.go4lunch.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class FireBaseConnector {

    public void getColleague(OnCompleteListener pListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Colleague> toReturn=new ArrayList<>();

        db.collection("Colleague")
                .get()
                .addOnCompleteListener(pListener);
    }
}
