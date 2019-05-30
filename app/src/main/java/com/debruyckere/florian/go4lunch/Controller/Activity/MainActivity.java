package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //go4lunch-3b9d8
    private static final int RC_SIGN_IN = 123;

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


        //createNotificationChannel();
        //configureAlarmManager();
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
            } else if (response.getError() != null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
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
                    boolean alreadySignIn = false;
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





    private void nextScreen(){
        Intent intent = new Intent(this,appActivity.class);
        startActivity(intent);
    }
}
