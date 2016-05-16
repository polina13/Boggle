package com.mklgallegos.boggle.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeleteAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Firebase mFirebaseRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;

    //inputs
    @Bind(R.id.emailEditText) EditText mEmailEditText;
    @Bind(R.id.passwordEditText) EditText mPasswordEditText;

    //buttons
    @Bind(R.id.deleteAccountButton) Button mDeleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        ButterKnife.bind(this);

        mDeleteAccountButton.setOnClickListener(this);

        //define Firebase ref
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);



    }

    public void deleteUser(){

        //collect inputs
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        mFirebaseRef.removeUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {



                Firebase firebaseUserRef = new Firebase(Constants.FIREBASE_URL_USERS + "/" + mUId);
                firebaseUserRef.removeValue();

                Firebase firebaseUserGamesRef = new Firebase(Constants.FIREBASE_URL_GAMES + "/" + mUId);
                firebaseUserGamesRef.removeValue();

                Intent intent = new Intent(DeleteAccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteAccountButton:
                deleteUser();
                break;
        }

    }
}
