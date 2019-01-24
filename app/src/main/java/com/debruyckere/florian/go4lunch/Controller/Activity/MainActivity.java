package com.debruyckere.florian.go4lunch.Controller.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.debruyckere.florian.go4lunch.Model.Colleague;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;
import com.debruyckere.florian.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //go4lunch-3b9d8
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    private Button mLoginButton;
    private Button mTestButton;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        mLoginButton = findViewById(R.id.main_login_button);
        mTestButton = findViewById(R.id.main_testbutton);
        mText = findViewById(R.id.main_testText);

        onClickLoginButton();
    }


    public void onClickLoginButton(){

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseConnector fBC = new FireBaseConnector(getBaseContext());
                ArrayList<Colleague> toTest = fBC.getColleagues();
                try {
                    Log.i("TEST CONNECTOR", "Colleague1 " + toTest.get(0).getName());
                }catch (IllegalStateException e){
                    Log.e("TEST ERROR",e.getMessage());
                }
            }
        });
    }



    public void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build() // SUPPORT GOOGLE
                                        //,new AuthUI.IdpConfig.EmailBuilder().build()
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
                mText.setText("Welcome "+mAuth.getCurrentUser().getEmail());
            }
        } else { // ERRORS
            if (response == null) {
                mText.setText("canceled");
            } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                mText.setText("no network");
            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                mText.setText("Unknow Bug");

            }
        }
        }
    }
