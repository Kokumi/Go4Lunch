package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.debruyckere.florian.go4lunch.Model.Colleague;
import com.debruyckere.florian.go4lunch.Model.DetailRestaurantAdapter;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.Model.Wish;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailRestaurantActivity extends AppCompatActivity {

    private TextView mTitle, mTypeAddress;
    private ImageButton mValidationButton,mCallButton,mLikeButton,mWebsiteButton;
    private ImageView mImage;
    private RecyclerView mRecyclerView;
    private Restaurant mRestaurant;
    private ProgressBar mProgressBar;
    private WebView mWebView;

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
        mWebView = findViewById(R.id.detail_webview);

        Intent intent = getIntent();
        String restaurantId = intent.getStringExtra("RESTAURANTDATA");
        getData(restaurantId);
        buttonParameter();
    }

    /**
     * show data of the current restaurant
     */
    private void display(){
        mTitle.setText(mRestaurant.getName());
        mTypeAddress.setText(new StringBuilder(mRestaurant.getType()+" - "+mRestaurant.getAddress()));
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * configure buttons
     */
    private void buttonParameter(){
        //add Wish button
        mValidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null) {
                    Colleague user = new Colleague(currentUser.getUid(), currentUser.getDisplayName(), "");
                    Wish wish = new Wish(Calendar.getInstance().getTime(), user, mRestaurant);
                    Log.i("WISH ADD", "date: " + wish.getDate().toString() + " for "
                            + wish.getColleague().getName() + " " + wish.getColleague().getSurname() + " to " + wish.getRestaurant().getName());

                    new FireBaseConnector().addWish(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            mValidationButton.setImageResource(R.drawable.ic_success_icon);
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("ADD WISH", "Failure " + e.getMessage());
                        }
                    }, wish);
                }
            }
        });

        //configure button for call
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+mRestaurant.getPhoneNumber()));
                //startActivity(callIntent);
            }
        });

        //configure like button
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                new FireBaseConnector().addLike(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        mLikeButton.setImageResource(R.drawable.ic_heart_full);
                    }
                },FirebaseAuth.getInstance().getCurrentUser().getUid()
                ,mRestaurant.getId());

            }
        });

        //configure website button
        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, mRestaurant.getWebUri());
                startActivity(browserIntent);*/
                if(mRestaurant.getWebUri() != null) {
                    mWebView.loadUrl(mRestaurant.getWebUri().toString());
                    mWebView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(),"No website.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * get data of the restaurant
     * @param pId id of the restaurant
     */
    private void getData(String pId){
        new FireBaseConnector().getRestaurantData(
                new OnCompleteListener<FetchPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            AddressComponents aC = task.getResult().getPlace().getAddressComponents();

                            //get opening hours
                            String openHour= "not this day";
                            if(task.getResult().getPlace().getOpeningHours()!= null) {
                                for (Period p : task.getResult().getPlace().getOpeningHours().getPeriods()) {
                                    if (p.getClose() != null && p.getClose().getTime().getHours() <= 19 &&
                                            p.getClose().getDay().ordinal() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) {
                                        openHour = (p.getClose().getTime().getHours() + "H " + p.getClose().getTime().getMinutes());
                                    }
                                }
                            }

                            //get id, name,address, type, rating
                            if(aC != null && task.getResult().getPlace().getTypes() != null)
                                mRestaurant = new Restaurant(task.getResult().getPlace().getId(),
                                        task.getResult().getPlace().getName(),
                                        aC.asList().get(0).getName()+" "+aC.asList().get(1).getName(),
                                        task.getResult().getPlace().getTypes().get(0).toString(),
                                        openHour,
                                        task.getResult().getPlace().getUserRatingsTotal());

                            //get phone number
                            if(task.getResult().getPlace().getPhoneNumber()!=null)
                                mRestaurant.setPhoneNumber(task.getResult().getPlace().getPhoneNumber());
                            //get website url
                            if(task.getResult().getPlace().getWebsiteUri()!= null)
                                mRestaurant.setWebUri(task.getResult().getPlace().getWebsiteUri());

                            //get photo
                            new FireBaseConnector().getRestaurantPhoto(getPhotoListener(),getApplicationContext(),task.getResult().getPlace());

                            display();
                            getColleagueData();

                            //check if the user already liked the current restaurant
                            if(FirebaseAuth.getInstance().getCurrentUser() != null)
                                new FireBaseConnector().getLike(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        boolean liked = false;
                                        if(task.getResult() != null) {
                                            for (QueryDocumentSnapshot document : task.getResult()){
                                                if(document.get("restaurantId").toString().equals(mRestaurant.getId()))
                                                    liked = true;
                                            }
                                        }
                                        if(liked)
                                            mLikeButton.setImageResource(R.drawable.ic_heart_full);
                                    }
                                },FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }else {
                            if(task.getException()!= null)
                                Log.e("DETAIL_TASK",task.getException().getMessage());
                        }
                    }
                }, this, pId);
    }

    /**
     * get listener to retrieve the photo of the current restaurant
     * @return
     */
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

    /**
     * get colleague who wish to go in the current restaurant
     */
    private void getColleagueData(){
        final FireBaseConnector FBC = new FireBaseConnector();
        //step1: Get ID from wish by restaurant's address
        FBC.getWishByAddress(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult() != null){
                    for(QueryDocumentSnapshot document : task.getResult()){

                        //check if it's the current day
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        if(dateFormat.format(document.getDate("date")).equals(dateFormat.format(Calendar.getInstance().getTime()))) {

                            //check if it's a current user's today wish
                            if(FirebaseAuth.getInstance().getCurrentUser() != null &&
                                    document.getData().get("colleagueId").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                mValidationButton.setImageResource(R.drawable.ic_success_icon);
                            }else {
                                String colleagueId = document.getData().get("colleagueId").toString();

                                FBC.getColleagueById(getColleagueListener(), colleagueId);
                            }
                        }
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

    /**
     * get colleague Data
     * @return listener who collect data
     */
    private OnCompleteListener<DocumentSnapshot> getColleagueListener(){
        return new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<Colleague> data = new ArrayList<>();
                if(task.isSuccessful() && task.getResult() != null){
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() != null){

                        Colleague colleague = new Colleague(document.getId(),
                                                            document.getData().get("name").toString());

                        colleague.setPicture(document.getData().get("photo").toString());

                        data.add(colleague);
                        }
                        RecyclerParameter(data);
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

    /**
     * configure the colleague recycler view
     * @param pData data of colleagues
     */
    private void RecyclerParameter(ArrayList<Colleague> pData){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new DetailRestaurantAdapter(pData));
    }
}
