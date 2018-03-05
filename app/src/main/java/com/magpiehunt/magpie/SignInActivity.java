package com.magpiehunt.magpie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;

/**
 * Author:  Blake Impecoven
 * Date:    11/14/17.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignInActivity";
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
    SignInButton mSignInButton;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        // Initialize SignInButton
        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);

    }//end onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            default:
                return;
        }//end switch
    }//end onClick

    private void signIn() {
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }//end signIn

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Google Sign-In was successful, authenticate with Firebase
                user = FirebaseAuth.getInstance().getCurrentUser();
                log.e(TAG, user.getDisplayName());
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                // Google Sign-In failed.
                Log.e(TAG, "Google Sign-In failed.");
            }//if: successful. else: failure.
        }//end if
    }//end
}
