package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debruyckere.florian.go4lunch.Model.AlarmReceiver;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.Model.ViewPagerAdapter;
import com.debruyckere.florian.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class appActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

   private DrawerLayout mDrawer;
    private static final String  CHANNEL_ID = "g4l";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        configureViewPager();
        configureNavigationDrawer();
        createNotificationChannel();
        configureAlarmManager();
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
        View headerView = mNavigationView.getHeaderView(0);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawer = findViewById(R.id.app_drawer);
        mDrawer.closeDrawer(GravityCompat.START);

        final TextView tName = headerView.findViewById(R.id.drawer_user_name);
        TextView tEmail = headerView.findViewById(R.id.drawer_email);
        ImageView userImage = headerView.findViewById(R.id.drawer_image);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            tName.setText(currentUser.getDisplayName());
            tEmail.setText(currentUser.getEmail());
            new userImageTask(userImage).execute(currentUser.getPhotoUrl().toString());
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.drawer_lunch:
                //Show the user wish of the day
                new FireBaseConnector().GetWishOfColleague(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.getResult() != null){
                        for(DocumentSnapshot document : task.getResult()){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                           if(dateFormat.format(document.getDate("date")).equals(dateFormat.format(Calendar.getInstance().getTime()))){
                           //if(document.get("date") == Calendar.getInstance().getTime()){

                                //Get data of the restaurant wishes
                               new FireBaseConnector().getRestaurantData(new OnCompleteListener<FetchPlaceResponse>() {
                                    @Override
                                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                                        if(task.getResult() != null){
                                            Toast.makeText(getApplicationContext(),
                                                    "you want go to "+task.getResult().getPlace().getName(),
                                                    Toast.LENGTH_LONG)
                                            .show();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"You go nowhere today",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },getApplicationContext(),document.get("restaurantId").toString());

                            }else {
                                Toast.makeText(getApplicationContext(),"You go nowhere today",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(),"You go nowhere today",Toast.LENGTH_SHORT).show();
                }
            },FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;

            case R.id.drawer_settings:
                Intent settingIntent = new Intent(this,SettingActivity.class);
                startActivity(settingIntent);
                break;

            case R.id.drawer_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent goBack = new Intent(this,MainActivity.class);
                startActivity(goBack);
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

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= 26) {
            CharSequence name = "Go4Lunch notification";
            String description = "Channel of notification for Go4Lunch";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }


    public void configureAlarmManager(){
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //cal.set(Calendar.HOUR,12);               //alarm set to active at 12 hour
        //cal.set(Calendar.MINUTE,0);
        cal.add(Calendar.MINUTE,1);
        cal.set(Calendar.SECOND,0);
        Log.i("ALARM MANAGER","alarm set for: "+cal.getTime());

        PendingIntent pi = PendingIntent.getBroadcast(this,1,new Intent(this, AlarmReceiver.class),PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences preferences = getSharedPreferences("notificationSet",MODE_PRIVATE);

        if(preferences.getBoolean("notification",false) && alarm != null){
            alarm.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);
            Log.i("ALARM MANAGER","alarm activate");
        }else {
            if(alarm != null)
                alarm.cancel(pi);
            Log.i("ALARM MANAGER","alarm disable");
        }
    }
}
