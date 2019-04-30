package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.Model.Colleague;
import com.debruyckere.florian.go4lunch.Model.DetailRestaurantAdapter;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailRestaurantActivity extends AppCompatActivity {

    private TextView mTitle, mTypeAddress;
    private ImageButton mValidationButton,mCallButton,mLikeButton,mWebsiteButton;
    private ImageView mImage;
    private RecyclerView mRecyclerView;
    private Restaurant mRestaurant;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        mTitle = findViewById(R.id.detail_title);
        mTypeAddress = findViewById(R.id.detail_typeaddress);
        mImage = findViewById(R.id.detail_image);
        mValidationButton = findViewById(R.id.detail_validation_button);
        mCallButton = findViewById(R.id.detail_call);
        mLikeButton = findViewById(R.id.detail_like);
        mWebsiteButton = findViewById(R.id.detail_website);
        mRecyclerView = findViewById(R.id.detail_recycler);
        mProgressBar = findViewById(R.id.detail_progress);

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("RESTAURANTDATA");
        getData(restaurantId);
        buttonParameter();
    }

    private void display(){
        mTitle.setText(mRestaurant.getName());
        mTypeAddress.setText(new StringBuilder(mRestaurant.getType()+" - "+mRestaurant.getAddress()));
        mProgressBar.setVisibility(View.GONE);
    }

    private void buttonParameter(){
        mValidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ADD WISH with user-ID
            }
        });

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Maybe
            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Search how consider a like with API
            }
        });

        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ADD a opener to site
            }
        });
    }

    private void getData(String pId){
        new FireBaseConnector().getRestaurantData(
                new OnCompleteListener<FetchPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            AddressComponents aC = task.getResult().getPlace().getAddressComponents();
                            String openHour= "not this day";
                            if(task.getResult().getPlace().getOpeningHours()!= null) {
                                for (Period p : task.getResult().getPlace().getOpeningHours().getPeriods()) {
                                    if (p.getClose() != null && p.getClose().getTime().getHours() <= 19 &&
                                            p.getClose().getDay().ordinal() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) {
                                        openHour = (p.getClose().getTime().getHours() + "H " + p.getClose().getTime().getMinutes());
                                    }
                                }
                            }

                            if(aC != null && task.getResult().getPlace().getTypes() != null)
                                mRestaurant = new Restaurant(task.getResult().getPlace().getId(),
                                        task.getResult().getPlace().getName(),
                                        aC.asList().get(0).getName()+" "+aC.asList().get(1).getName(),
                                        task.getResult().getPlace().getTypes().get(0).toString(),
                                        openHour,
                                        task.getResult().getPlace().getUserRatingsTotal());

                            if(task.getResult().getPlace().getPhoneNumber()!=null)
                                mRestaurant.setPhoneNumber(task.getResult().getPlace().getPhoneNumber());
                            if(task.getResult().getPlace().getWebsiteUri()!= null)
                                mRestaurant.setWebUri(task.getResult().getPlace().getWebsiteUri());

                            new FireBaseConnector().getRestaurantPhoto(getPhotoListener(),getApplicationContext(),task.getResult().getPlace());

                            display();
                            getColleagueData();
                        }else {
                            if(task.getException()!= null)
                                Log.e("DETAIL_TASK",task.getException().getMessage());
                        }
                    }
                }, this, pId);
    }

    private OnCompleteListener<FetchPhotoResponse> getPhotoListener(){
        return new OnCompleteListener<FetchPhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<FetchPhotoResponse> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    mRestaurant.setImage(task.getResult().getBitmap());
                    mImage.setImageBitmap(task.getResult().getBitmap());
                }
            }
        };

    }

    private void getColleagueData(){
        final FireBaseConnector FBC = new FireBaseConnector();
        //step1: Get ID from wish with @
        FBC.getWishByAddress(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String colleagueId = document.getData().get("colleagueId").toString();

                        FBC.getColleagueById(getColleagueListener(),colleagueId);
                    }
                }else{
                    if (task.getException() != null)
                    Log.e("Error","task Error"+ task.getException().toString());
                    else{
                        Log.e("Error","NoData");
                    }
                }
            }
        },mRestaurant.getAddress());
    }

    private OnCompleteListener<DocumentSnapshot> getColleagueListener(){
        return new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<Colleague> data = new ArrayList<>();
                if(task.isSuccessful() && task.getResult() != null){
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() != null){

                        Colleague colleague = new Colleague(document.getId(),
                                                            document.getData().get("name").toString(),
                                                            document.getData().get("surname").toString());

                        data.add(colleague);
                        }
                        RecyclerParamater(data);
                }else{
                    if (task.getException() != null)
                        Log.e("Error","task Error"+ task.getException().toString());
                    else{
                        Log.e("Error","NoData");
                    }
                }
            }
        };
    }

    private void RecyclerParamater(ArrayList<Colleague> pData){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new DetailRestaurantAdapter(pData));
    }
}
