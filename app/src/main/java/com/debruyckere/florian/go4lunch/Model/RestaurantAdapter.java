package com.debruyckere.florian.go4lunch.Model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.Controller.Activity.DetailRestaurantActivity;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private ArrayList<Restaurant> data ;
    private Context mContext;

    public RestaurantAdapter(ArrayList<Restaurant> pData,Context pContext){
        data = pData;
        mContext = pContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.restaurant_cell,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Restaurant rest = data.get(position);
        holder.display(rest);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{

        private FireBaseConnector mFireBase = new FireBaseConnector();
        private Restaurant mRestaurant;
        private TextView mName,mTypeAddress,mOpen,mDistance,mColleague;
        private ImageView mRestaurantImage,mStar1,mStar2,mStar3,mStar4,mStar5;
        private ProgressBar mProgressBar;

        private MyViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.restaurant_cell_name);
            mTypeAddress= itemView.findViewById(R.id.restaurant_cell_address);
            mOpen= itemView.findViewById(R.id.restaurant_cell_open);
            mDistance= itemView.findViewById(R.id.restaurant_cell_distance);
            mColleague= itemView.findViewById(R.id.restaurant_cell_colleague);
            mRestaurantImage= itemView.findViewById(R.id.restaurant_cell_image);
            mStar1= itemView.findViewById(R.id.restaurant_cell_star1);
            mStar2= itemView.findViewById(R.id.restaurant_cell_star2);
            mStar3= itemView.findViewById(R.id.restaurant_cell_star3);
            mStar4= itemView.findViewById(R.id.restaurant_cell_star4);
            mStar5= itemView.findViewById(R.id.restaurant_cell_star5);
            mProgressBar = itemView.findViewById(R.id.restaurant_cell_progress);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(mContext, DetailRestaurantActivity.class);
                    intent.putExtra("RESTAURANTDATA",mRestaurant.getId());
                    mContext.startActivity(intent);
                }
            });
        }
        private void display(Restaurant pRest){
            mRestaurant = pRest;
            mName.setText(pRest.getName());
            mDistance.setText(new StringBuilder(pRest.getDistance()+" m"));
            //mRestaurantImage.setImageBitmap(pRest.getImage());


            mFireBase.getWish(getWishListener(pRest));
            mFireBase.getRestaurantData(getPlaceCompleteListener(),mContext,pRest.getId());

            switch(pRest.getRate()){
                case 1:mStar1.setVisibility(View.VISIBLE);
                    break;
                case 2:mStar1.setVisibility(View.VISIBLE);
                       mStar2.setVisibility(View.VISIBLE);
                    break;
                case 3: mStar1.setVisibility(View.VISIBLE);
                    mStar2.setVisibility(View.VISIBLE);
                    mStar3.setVisibility(View.VISIBLE);
                    break;
                case 4:mStar1.setVisibility(View.VISIBLE);
                    mStar2.setVisibility(View.VISIBLE);
                    mStar3.setVisibility(View.VISIBLE);
                    mStar4.setVisibility(View.VISIBLE);
                    break;
                case 5:mStar1.setVisibility(View.VISIBLE);
                    mStar2.setVisibility(View.VISIBLE);
                    mStar3.setVisibility(View.VISIBLE);
                    mStar4.setVisibility(View.VISIBLE);
                    mStar5.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private OnCompleteListener<QuerySnapshot> getWishListener(final Restaurant pRest){

            return new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() && task.getResult() != null){
                        int nbWish = 0;
                        for(QueryDocumentSnapshot document :task.getResult()){
                            if(document.getData().get("restaurantAdresse") == pRest.getAddress() &&
                            document.getData().get("date") == Calendar.getInstance().getTime()){
                                nbWish++;

                            }
                        }
                        mColleague.setText(new StringBuilder(nbWish+ " Wish"));

                    }else{
                        if(task.getException() != null)
                        Log.e("WISH LISTENER",task.getException().toString());
                    }
                }
            };
        }

        private OnCompleteListener<FetchPlaceResponse> getPlaceCompleteListener(){

            return new OnCompleteListener<FetchPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                    if(task.isSuccessful() && task.getResult()!= null){
                        if(task.getResult().getPlace().getOpeningHours() != null) {

                            OpeningHours oH = task.getResult().getPlace().getOpeningHours();
                            //Log.i("oH Weekday",oH.getWeekdayText().get(0));

                            for(Period p : oH.getPeriods()){
                                if(p.getClose()!=null && p.getClose().getTime().getHours() <= 19 &&
                                   p.getClose().getDay().ordinal() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1){
                                    mRestaurant.setOpen(p.getClose().getTime().getHours()+"H "+p.getClose().getTime().getMinutes());
                                }
                            }

                            mOpen.setText(mRestaurant.getOpen());
                        }

                        AddressComponents aC = task.getResult().getPlace().getAddressComponents();

                        if(aC != null)
                        mRestaurant.setAddress(aC.asList().get(0).getName()+" "+aC.asList().get(1).getName());

                        mTypeAddress.setText(new StringBuilder(mRestaurant.getType()+" - "+mRestaurant.getAddress()));

                        mFireBase.getRestaurantPhoto(getPhotoListener(),mContext,task.getResult().getPlace());
                    }else {
                        if(task.getException()!=null)
                        Log.e("COMPLETE PLACE LIST",task.getException().toString());
                    }
                }
            };
        }

        private OnCompleteListener<FetchPhotoResponse> getPhotoListener(){
            return new OnCompleteListener<FetchPhotoResponse>(){
                @Override
                public void onComplete(@NonNull Task<FetchPhotoResponse> task) {
                    if(task.isSuccessful() && task.getResult()!= null){
                        mRestaurantImage.setImageBitmap(task.getResult().getBitmap());
                    }
                    mProgressBar.setVisibility(View.GONE);
                }
            };
        }
    }
}
