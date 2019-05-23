package com.debruyckere.florian.go4lunch.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.R;

import java.util.ArrayList;

/**
 * Created by Debruyckère Florian on 25/04/2019.
 */
public class DetailRestaurantAdapter extends RecyclerView.Adapter<DetailRestaurantAdapter.DetailRestaurantViewHolder> {

    ArrayList<Colleague> mData;

    public DetailRestaurantAdapter(ArrayList<Colleague> pData){
        mData = pData;
    }

    @NonNull
    @Override
    public DetailRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.detail_restaurant_cell,parent,false);


        return new DetailRestaurantAdapter.DetailRestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRestaurantViewHolder holder, int position) {
        Colleague coll = mData.get(position);
        holder.display(coll);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DetailRestaurantViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImageView;
        private TextView mText;
        private ProgressBar mProgressBar;

        private DetailRestaurantViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.detail_colleague_image);
            mText = itemView.findViewById(R.id.detail_colleague_text);
            mProgressBar = itemView.findViewById(R.id.detail_colleague_progressbar);
        }

        private void display(Colleague pColleague){


            mText.setText(new StringBuilder(pColleague.getName() + " is joining"));
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
