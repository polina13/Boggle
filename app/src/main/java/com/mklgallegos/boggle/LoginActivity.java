package com.mklgallegos.boggle;

import android.content.Intent;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private Firebase mFirebaseRef;

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

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mSignUpTextView.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
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
            mEmailEditText.setError("Please enter your email");
        }

        if (password.equals("")) {
            mPasswordEditText.setError("Password cannot be blank");
        }

        mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                if (authData != null) {
                    String userInfo = authData.toString();
                    Log.d(TAG, "Currently logged in: " + userInfo);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                switch (firebaseError.getCode()) {
                    case FirebaseError.INVALID_EMAIL:
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        mEmailEditText.setError("Please check that you entered your email correctly");
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        mEmailEditText.setError(firebaseError.getMessage());
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