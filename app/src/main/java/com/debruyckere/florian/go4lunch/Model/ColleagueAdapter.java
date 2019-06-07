package com.debruyckere.florian.go4lunch.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class ColleagueAdapter extends RecyclerView.Adapter<ColleagueAdapter.ColleagueViewHolder> {

    private final ArrayList<Colleague> mData;
    private final Context mContext;

    public ColleagueAdapter(ArrayList<Colleague> pData, Context pContext){
        mData = pData;
        mContext = pContext;
    }

    @NonNull
    @Override
    public ColleagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.colleague_cell,parent,false);

        return new ColleagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColleagueViewHolder holder, int position) {
        holder.display(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ColleagueViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTextView;
        private final ImageView mImageView;

        private ColleagueViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.colleague_cell_text);
            mImageView = itemView.findViewById(R.id.colleague_cell_image);
        }

        private void display(Colleague param){
            if(param.getSurname() != null)
            mTextView.setText(new StringBuilder(param.getName()+" "+ param.getSurname()));

            else mTextView.setText(new StringBuilder(param.getName()));

            new FireBaseConnector().GetWishOfColleague(RestaurantListener(),param.getId());

            new userImageDownloader(mImageView).execute(param.getPicture());
        }

        //------------------------
        // LISTENER AND ASYNC TASK
        //------------------------

        /**
         * get the listener to check if the user go somewhere
         * @return the query listener
         */
        private OnCompleteListener<QuerySnapshot> RestaurantListener(){
            return new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    boolean hadChoice = false;
                    if( task.getResult() != null){
                        for(DocumentSnapshot document : task.getResult()){
                            if(document.get("date") == Calendar.getInstance().getTime()){
                                //if he wished to go somewhere
                                hadChoice = true;
                                new FireBaseConnector().getRestaurantData(new OnCompleteListener<FetchPlaceResponse>(){
                                    @Override
                                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                                        if(task.getResult() != null){
                                            //retrieve the restaurant where he go
                                            mTextView.setText(new StringBuilder(mTextView.getText()+ " want go to " + task.getResult().getPlace().getName()));
                                        }
                                    }
                                },mContext,document.get("RestaurantId").toString());

                            }
                        }
                    }
                    if(!hadChoice) mTextView.setText(new StringBuilder(mTextView.getText()+" go nowhere for now"));
                }
            };
        }

    }
}
