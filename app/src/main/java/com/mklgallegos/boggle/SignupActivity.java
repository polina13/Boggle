package com.mklgallegos.boggle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mklgallegos.boggle.models.User;


import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private Firebase mFirebaseRef;
    public static final String TAG = SignupActivity.class.getSimpleName();

    //Text
    @Bind(R.id.loginTextView) TextView mLoginTextView;

    //Inputs
    @Bind(R.id.firstNamEditText) EditText mFirstNameEditText;
    @Bind(R.id.lastNameEditText) EditText mLastNameEditText;
    @Bind(R.id.emailEditText) EditText mEmailEditText;
    @Bind(R.id.passwordEditText) EditText mPasswordEditText;

    //Buttons
    @Bind(R.id.createNewAccountbutton) Button mCreateNewAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        //define Firebase ref
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        //click listeners
        mCreateNewAccountButton.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTextView:
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.createNewAccountbutton:
                createNewUser();
                break;
        }
    }

    public void createNewUser() {
        final String firstName = mFirstNameEditText.getText().toString();
        final String lastName = mLastNameEditText.getText().toString();
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String uid = result.get("uid").toString();
                createUserInFirebaseHelper(firstName, lastName, email, uid);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.d(TAG, "error occurred " + firebaseError);
            }
        });
    }

    private void createUserInFirebaseHelper(final String firstName, final String lastName, final String email, final String uid) {
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);
        User newUser = new User(firstName, lastName, email);
        userLocation.setValue(newUser);
    }
}