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
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.Model.Wish;
import com.debruyckere.florian.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

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



        onClickParameter();

    }

    public void task(){
        FireBaseConnector FBC = new FireBaseConnector();

        OnSuccessListener<DocumentReference> sucessLi = new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i("FirebaseConnector","Wish Added");
            }
        };

        OnFailureListener failLi = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("FirebaseConnector","Fail");
                e.printStackTrace();
            }
        };
        Colleague me = new Colleague("sYleg2zzfVrkXygyMXvN" ,"Test","Bertrand");
        Restaurant restaurant = new Restaurant("1","12 Pierre du feu");
        Wish myWish = new Wish(Calendar.getInstance().getTime(),me,restaurant);

        FBC.addWish(sucessLi,failLi,myWish);

    }

    public void getTask(){
        final ArrayList<Colleague> toReturn=new ArrayList<>();
        OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document :task.getResult()){
                        Colleague colleague = new Colleague(document.getId()
                                ,(String) document.getData().get("name")
                                ,(String) document.getData().get("surname"));

                        toReturn.add(colleague);
                    }
                }else{
                    Log.w("Database error : ",task.getException());
                }
                Log.v("Database answer",toReturn.get(0).getName() + " " +toReturn.get(0).getSurname());
            }
        };

        FireBaseConnector FBC = new FireBaseConnector();
        FBC.getColleague(listener);
    }



    public void onClickParameter(){

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task();
            }
        });

        mOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen();
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
                Intent intent = new Intent(this,appActivity.class);
                startActivity(intent);
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

    private void nextScreen(){
        Intent intent = new Intent(this,appActivity.class);
        startActivity(intent);
    }
}
