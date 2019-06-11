package com.debruyckere.florian.go4lunch.Model;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Task to download the user image
 */
public class userImageDownloader extends AsyncTask<String,Void, Drawable> {

    private final WeakReference<ImageView> mImageReference;

    public userImageDownloader(ImageView pImage){
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
