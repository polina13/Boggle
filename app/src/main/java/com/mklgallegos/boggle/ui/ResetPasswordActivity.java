package com.mklgallegos.boggle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase
    private Firebase mFirebaseRef;

    //Button
    @Bind(R.id.resetPasswordButton) Button mResetPasswordButton;

    //EditText aka Inputs
    @Bind(R.id.emailEditText) EditText mEmailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        //click listener
        mResetPasswordButton.setOnClickListener(this);

        //define Firebase ref
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetPasswordButton:

                //grab value of inputs
                final String email = mEmailEditText.getText().toString();

                mFirebaseRef.resetPassword(email, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        //toast to indicate password reset was successful
                        Toast.makeText(ResetPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_LONG).show();

                        //redirect user to LoginActivity
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                    }
                });

        }
    }

}
