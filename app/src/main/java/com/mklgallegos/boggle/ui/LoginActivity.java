package com.mklgallegos.boggle.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();


    //Firebase
    private Firebase mFirebaseRef;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private ProgressDialog mAuthProgressDialog;

    @Bind(R.id.signUpTextView) TextView mSignUpTextView;

    //button
    @Bind(R.id.loginButton) Button mLoginButton;

    //Inputs
    @Bind(R.id.emailEditText) EditText mEmailEditText;
    @Bind(R.id.passwordEditText) EditText mPasswordEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mSignUpTextView.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        //auth progress dialog
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("loading...");
        mAuthProgressDialog.setMessage("Authentication in progress...");
        mAuthProgressDialog.setCancelable(false);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpTextView:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.loginButton:
                loginWithPassword();
                break;
        }
    }

    public void loginWithPassword() {
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (email.equals("")) {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mEmailEditText.setError("Please enter your email", d);
        }

        if (password.equals("")) {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mPasswordEditText.setError("Password cannot be blank", d);
        }

        mAuthProgressDialog.show();

        mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                mAuthProgressDialog.dismiss();
                if (authData != null) {
                    String userInfo = authData.toString();
                    Log.d(TAG, "Currently logged in: " + userInfo);

                    String userUid = authData.getUid();
                    mSharedPreferencesEditor.putString(Constants.KEY_UID, userUid).apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

                mAuthProgressDialog.dismiss();
                switch (firebaseError.getCode()) {
                    case FirebaseError.INVALID_EMAIL:
                        mEmailEditText.setError("Invalid Email", d);
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        mEmailEditText.setError("Please check that you entered your email correctly", d);
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        mEmailEditText.setError(firebaseError.getMessage(), d);
                        break;
                    case FirebaseError.NETWORK_ERROR:
                        showErrorToast("There was a problem with the network connection");
                        break;
                    default:
                        showErrorToast(firebaseError.toString());
                }
            }

        });
    }



    private void showErrorToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}