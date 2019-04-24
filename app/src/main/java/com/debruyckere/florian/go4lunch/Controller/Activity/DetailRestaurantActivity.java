package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;

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

                            display();
                        }else {
                            if(task.getException()!= null)
                            Log.e("DETAIL_TASK",task.getException().getMessage());
                        }
                    }
                }, this, pId);
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

            }
        });

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
