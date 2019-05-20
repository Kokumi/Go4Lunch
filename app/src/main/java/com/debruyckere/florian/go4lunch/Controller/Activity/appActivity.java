package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.Model.ViewPagerAdapter;
import com.debruyckere.florian.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class appActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

   private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        configureViewPager();
        configureNavigationDrawer();
    }

    private void configureViewPager(){
        ViewPager pager = findViewById(R.id.app_viewpager);

        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabs=findViewById(R.id.app_tablayout);

        tabs.setupWithViewPager(pager);

        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    private void configureNavigationDrawer(){
        NavigationView mNavigationView = findViewById(R.id.app_navigation);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawer = findViewById(R.id.app_drawer);
        mDrawer.closeDrawer(GravityCompat.START);

        final TextView tName = findViewById(R.id.drawer_user_name);
        TextView tEmail = findViewById(R.id.drawer_email);
        ImageView userImage = findViewById(R.id.drawer_image);

        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //UserInfo user = currentUser.getProviderData().get(0);

        tName.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                tName.setText(currentUser.getDisplayName());
            }
        },1000);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.drawer_lunch:
                break;
            case R.id.drawer_settings:
                break;
            case R.id.drawer_logout:
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
        super.onBackPressed();
    }

    static class userImageTask extends AsyncTask<String,Void, Drawable>{

        private WeakReference<ImageView> mImageView;

        private userImageTask(ImageView pImageView){
            mImageView = new WeakReference<>(pImageView);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            mImageView.get().setImageDrawable(drawable);
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
