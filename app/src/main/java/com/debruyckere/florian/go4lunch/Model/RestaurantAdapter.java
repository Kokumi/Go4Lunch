package com.debruyckere.florian.go4lunch.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.R;

import java.util.ArrayList;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private ArrayList<Restaurant> data ;

    public RestaurantAdapter(){
        data = new FireBaseConnector().getRestaurantsData();

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

        private TextView mName,mTypeAddress,mOpen,mDistance,mColleague;
        private ImageView mRestaurantImage,mStar1,mStar2,mStar3;

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

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //TODO: when click go to detail
                }
            });
        }
        private void display(Restaurant pRest){
            mName.setText(pRest.getName());
            mTypeAddress.setText(new StringBuilder(pRest.getType()+" - "+pRest.getAddress()));
            mOpen.setText(pRest.getOpen());
            //mDistance.setText();
            //mColleague.setText();
            //mRestaurantImage.setText();

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
            }


        }
    }
}
