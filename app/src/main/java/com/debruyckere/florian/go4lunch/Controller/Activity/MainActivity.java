package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.debruyckere.florian.go4lunch.Model.AlarmReceiver;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //go4lunch-3b9d8
    private static final int RC_SIGN_IN = 123;
    private static final String  CHANNEL_ID = "g4l";
    private FirebaseAuth mAuth;

    @BindView(R.id.main_login_button)Button mLoginButton ;
    @BindView(R.id.main_testbutton)Button mTestButton;
    @BindView(R.id.main_testText)TextView mText;
    @BindView(R.id.main_outbutton)Button mOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAuth=FirebaseAuth.getInstance();

        mLoginButton = findViewById(R.id.main_login_button);
        mTestButton = findViewById(R.id.main_testbutton);
        mText = findViewById(R.id.main_testText);
        mOutButton = findViewById(R.id.main_outbutton);


        createNotificationChannel();
        configureAlarmManager();
        onClickParameter();

    }

    public void onClickParameter(){

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });


        mOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen();
            }
        });
    }

    //--------------------------
    // Firebase authentication
    //--------------------------


    public void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build() // SUPPORT GOOGLE
                                ))
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode,resultCode,data);
    }


    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                //mText.setText("Welcome "+mAuth.getCurrentUser().getEmail());
                signUpFirebase();
                nextScreen();
            } else {
                if(response != null && response.getError() != null)
                Log.e("Sign UP ERROR",response.getError().getMessage());
            }
        } else { // ERRORS
            if (response == null) {
                Toast.makeText(this,"Sign in Canceled", Toast.LENGTH_SHORT).show();
            } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Toast.makeText(getApplicationContext(),"No network, verify your Connection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Unfortunately, a unknown error has Arrived", Toast.LENGTH_SHORT).show();
                Log.e("Sign UP ERROR",response.getError().getMessage());
            }
        }
    }

    public void signUpFirebase(){
        OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() & task.getResult ()!= null & mAuth.getCurrentUser() != null){
                    Boolean alreadySignIn = false;
                    for(QueryDocumentSnapshot document :task.getResult()){
                        if(mAuth.getCurrentUser().getUid().equals(document.getId()))
                            alreadySignIn = true;

                    }
                    if(!alreadySignIn){
                        new FireBaseConnector().registerColleague(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Welcome new user", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },mAuth.getCurrentUser().getDisplayName()
                        ,mAuth.getCurrentUser().getUid()
                        ,mAuth.getCurrentUser().getPhotoUrl().toString());
                    }
                }else{
                    Log.w("Database error : ",task.getException());
                }
            }
        };

        FireBaseConnector FBC = new FireBaseConnector();
        FBC.getColleague(listener);
    }




    //------------------
    // Notification
    //------------------

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
        AlarmManager alarm = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,12);               //alarm set to active at 12 hour
        cal.set(Calendar.MINUTE,0);
        //cal.add(Calendar.MINUTE,1);
        cal.set(Calendar.SECOND,0);

        PendingIntent pi = PendingIntent.getBroadcast(this,0,new Intent(this, AlarmReceiver.class),0);
        SharedPreferences preferences = getSharedPreferences("notificationSet",MODE_PRIVATE);

        if(preferences.getBoolean("notification",false) && alarm != null){
            alarm.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);
        }else {
            if(alarm != null)
            alarm.cancel(pi);
        }
    }

    private void nextScreen(){
        Intent intent = new Intent(this,appActivity.class);
        startActivity(intent);
    }
}
