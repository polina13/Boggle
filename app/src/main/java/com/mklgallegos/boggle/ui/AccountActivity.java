package com.mklgallegos.boggle.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;
import com.mklgallegos.boggle.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AccountActivity.class.getSimpleName();


    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;

    @Bind(R.id.updateEmailButton) Button mUpdateEmailButton;


    @Bind(R.id.firstNameTextView) TextView mFirstNameTextView;
    @Bind(R.id.lastNameTextView) TextView mLastNameTextView;
    @Bind(R.id.emailTextView) TextView mEmailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        //click listeners
        mUpdateEmailButton.setOnClickListener(this);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);

        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                mFirstNameTextView.setText(user.getFirstName());
                mLastNameTextView.setText(user.getLastName());
                mEmailTextView.setText(user.getEmail());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Read failed");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateEmailButton:
                Intent intent = new Intent(AccountActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
                break;
        }

    }
}
