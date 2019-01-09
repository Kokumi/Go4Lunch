package com.debruyckere.florian.go4lunch.Model;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class FireBaseConnector {
    private static final int RC_SIGN_IN = 123;

   /* public void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), //EMAIL
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())) // SUPPORT GOOGLE
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }*/

}
