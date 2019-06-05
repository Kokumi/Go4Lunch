package com.debruyckere.florian.go4lunch.Model;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Debruyckère Florian on 25/04/2019.
 */
public class DetailRestaurantAdapter extends RecyclerView.Adapter<DetailRestaurantAdapter.DetailRestaurantViewHolder> {

    private final ArrayList<Colleague> mData;

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

        private final ImageView mImageView;
        private final TextView mText;
        private final ProgressBar mProgressBar;

        private DetailRestaurantViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.detail_colleague_image);
            mText = itemView.findViewById(R.id.detail_colleague_text);
            mProgressBar = itemView.findViewById(R.id.detail_colleague_progressbar);
        }

        private void display(Colleague pColleague){
            new userImageTask(mImageView).execute(pColleague.getPicture());
            mText.setText(new StringBuilder(pColleague.getName() + " is joining"));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * task to download the user image
     */
    static class userImageTask extends AsyncTask<String,Void, Drawable>{

        final WeakReference<ImageView> mImageReference;

        private userImageTask(ImageView pImage){
            mImageReference = new WeakReference<>(pImage);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            mImageReference.get().setImageDrawable(drawable);
            super.onPostExecute(drawable);
        }

        @Override
        protected Drawable doInBackground(String... strings) {
            Drawable retour = null;
            try {
                InputStream is = (InputStream) new URL(strings[0]).getContent();
                retour = Drawable.createFromStream(is,"src name");
            }catch (Exception e){
                Log.e("User Image Error",e.getMessage());
            }

            return retour;
        }
    }
}
