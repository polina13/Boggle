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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.mklgallegos.boggle.Constants;
import com.mklgallegos.boggle.R;
import com.mklgallegos.boggle.models.Game;
import com.mklgallegos.boggle.models.User;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AccountActivity.class.getSimpleName();

    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;

    //buttons
    @Bind(R.id.updateEmailButton) Button mUpdateEmailButton;
    @Bind(R.id.changePasswordButton) Button mChangePasswordButton;
    @Bind(R.id.deleteAccountButton) Button mDeleteAccountButton;

    //text views
    @Bind(R.id.firstNameTextView) TextView mFirstNameTextView;
    @Bind(R.id.lastNameTextView) TextView mLastNameTextView;
    @Bind(R.id.emailTextView) TextView mEmailTextView;
    @Bind(R.id.highScoreTextView) TextView mHighScoreTextView;
    @Bind(R.id.gamesPlayedTextView) TextView mGamesPlayedTextView;
    @Bind(R.id.avgScoreTextView) TextView mAvgScoreTextView;
    @Bind(R.id.cumScoreTextView) TextView mCumScoreTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        //shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        final String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);

        //firebase
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);
        Firebase firebaseUserGamesRef = new Firebase(Constants.FIREBASE_URL_GAMES + "/" + userUid);

        final Query returnAllChildNodes = new Firebase(Constants.FIREBASE_URL_GAMES).child(mUId);

        returnAllChildNodes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long numberOfGamesPlayed = dataSnapshot.getChildrenCount();


                Iterable<DataSnapshot> gamesPlayed = dataSnapshot.getChildren();

                ArrayList<Integer> scores = new ArrayList<Integer>();

                Integer total = 0;

                for (DataSnapshot data : gamesPlayed) {
                    Game game = data.getValue(Game.class);
                    Integer totalPoints = game.getTotalPoints();

                    scores.add(totalPoints);
                    total += totalPoints;
                }

                Collections.sort(scores);

                Integer highscore = scores.get(scores.size() - 1);


                Double avg = total.doubleValue()/numberOfGamesPlayed.intValue();

                Log.d(TAG, highscore.toString());
                Log.d(TAG, total.toString());
                Log.d(TAG, numberOfGamesPlayed.toString());
                Log.d(TAG, avg.toString());

                mAvgScoreTextView.setText(avg.toString());
                mHighScoreTextView.setText(highscore + " points");
                mGamesPlayedTextView.setText(numberOfGamesPlayed.toString());
                mCumScoreTextView.setText(total.toString());

                returnAllChildNodes.removeEventListener(this);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });



        //click listeners
        mUpdateEmailButton.setOnClickListener(this);
        mChangePasswordButton.setOnClickListener(this);
        mDeleteAccountButton.setOnClickListener(this);


        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                mFirstNameTextView.setText(user.getFirstName());
                mLastNameTextView.setText(user.getLastName());
                mEmailTextView.setText(user.getEmail());

                mUserRef.removeEventListener(this);

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
            case R.id.changePasswordButton:
                Intent changePasswordActivityIntent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
                startActivity(changePasswordActivityIntent);
                break;
            case R.id.deleteAccountButton:
                Intent deleteAccountActivityIntent = new Intent(AccountActivity.this, DeleteAccountActivity.class);
                deleteAccountActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(deleteAccountActivityIntent);
                finish();
                break;
        }

    }
}
