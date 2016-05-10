package com.mklgallegos.boggle.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;
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
    @Bind(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;

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
        final String confirmPassword = mConfirmPasswordEditText.getText().toString();

        boolean validEmail = isValidEmail(email);
        boolean validLastName = isValidFirstName(firstName);
        boolean validFirstName = isValidLastName(lastName);
        boolean validPassword = isValidPassword(password, confirmPassword);

        if (!validEmail || !validFirstName || !validLastName || !validPassword) return;

        mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String uid = result.get("uid").toString();
                createUserInFirebaseHelper(firstName, lastName, email, uid);
                Toast.makeText(SignupActivity.this, "Account Creation Successful", Toast.LENGTH_SHORT).show();

                //transition to LoginActivity once user has created an account
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {

            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

            mEmailEditText.setError("Please enter a valid email address", d);
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidFirstName(String name) {
        if (name.equals("")) {

            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);

            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mFirstNameEditText.setError("Please enter your first name", d);
            return false;
        }
        return true;
    }

    private boolean isValidLastName(String name) {
        if (name.equals("")) {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mLastNameEditText.setError("Please enter your last name", d);
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.equals("")) {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mPasswordEditText.setError("Password cannot be blank", d);
            return false;
        }

        if (!(password.equals(confirmPassword))) {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_outline_white_18dp, null);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mConfirmPasswordEditText.setError("Passwords do not match", d);
            return false;
        }
        return true;
    }
}