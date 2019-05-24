package com.debruyckere.florian.go4lunch.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Debruyckère Florian on 24/05/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String  CHANNEL_ID = "g4l";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        getWishData();
    }

    private NotificationCompat.Builder notificationBuilder(String pName){
        return new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setContentTitle("Your lunch")
                .setContentText("today you choose to go: "+ pName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void getWishData(){
        final NotificationManagerCompat nMC= NotificationManagerCompat.from(mContext);

        new FireBaseConnector().GetWishOfColleague(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult() != null){
                    for(DocumentSnapshot document : task.getResult()){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        if(dateFormat.format(document.getDate("date")).equals(dateFormat.format(Calendar.getInstance().getTime()))){

                            //Get data of the restaurant wishes
                            new FireBaseConnector().getRestaurantData(new OnCompleteListener<FetchPlaceResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                                    if(task.getResult() != null){

                                        nMC.notify(0,notificationBuilder(task.getResult().getPlace().getName()).build());

                                    }else {
                                        nMC.notify(0,notificationBuilder("nowhere").build());
                                    }
                                }
                            },mContext,document.get("restaurantId").toString());

                        }else {
                            nMC.notify(0,notificationBuilder("nowhere").build());
                        }
                    }
                }
                nMC.notify(0,notificationBuilder("nowhere").build());
            }
        }, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}
