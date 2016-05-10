package com.mklgallegos.boggle.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;
import com.mklgallegos.boggle.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangeEmailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ChangeEmailActivity.class.getSimpleName();

    private SharedPreferences mSharedPreferences;

    private Firebase mFirebaseRef;

    //Buttons
    @Bind(R.id.updateEmailButton)
    Button mUpdateEmailButton;

    //Inputs
    @Bind(R.id.oldEmailEditText)
    EditText mOldEmailEditText;
    @Bind(R.id.passwordEditText)
    EditText mPasswordEditText;
    @Bind(R.id.newEmailEditText)
    EditText mNewEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        ButterKnife.bind(this);

        //click listeners
        mUpdateEmailButton.setOnClickListener(this);

        //define Firebase ref
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChangeEmailActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateEmailButton:
                //grab value of inputs
                final String oldEmail = mOldEmailEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();
                final String newEmail = mNewEmailEditText.getText().toString();


                mFirebaseRef.changeEmail(oldEmail, password, newEmail, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        
                        final Firebase firebaseUserRef = new Firebase(Constants.FIREBASE_URL_USERS);

                        Query findOldEmail = firebaseUserRef.orderByChild("email").equalTo(oldEmail);

                        findOldEmail.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
                                Firebase specificUserRef = new Firebase(Constants.FIREBASE_URL_USERS + "/" + userUid);
                                specificUserRef.child("email").setValue(newEmail);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                    }
                });


        }
    }



}