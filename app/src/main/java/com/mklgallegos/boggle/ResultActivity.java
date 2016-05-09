package com.mklgallegos.boggle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mklgallegos.boggle.models.User;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = ResultActivity.class.getSimpleName();

    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;
    private Firebase mFirebaseRef;
    @Bind(R.id.playerNameTextView) TextView mPlayerNameTextView;
    @Bind(R.id.timestampValueTextView) TextView mTimestampValueTextView;
    @Bind(R.id.totalPointsValueTextView) TextView mTotalPointsValueTextView;
    @Bind(R.id.listView) ListView mListView;
    @Bind(R.id.playAgainButton) Button mPlayAgainButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);

        Date date = new Date();
        String dateString = date.toString();
        mTimestampValueTextView.setText(dateString);

        mPlayAgainButton.setOnClickListener(this);



            mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    mPlayerNameTextView.setText(user.getFirstName());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, "Read failed");
                }
            });

        Intent intent = getIntent();

        //retrieve the extras sent with the intent from Activity
        ArrayList<String> list = intent.getStringArrayListExtra("list");

        Integer totalScore = 0;

        for (String word : list) {
            if (word.length() == 3 || word.length() == 4) {
                totalScore += 1;
            } else if (word.length() == 5) {
                totalScore += 2;
            } else if (word.length() == 6) {
                totalScore += 3;
            } else if (word.length() == 7) {
                totalScore += 5;
            } else if (word.length() >= 8) {
                totalScore += 11;
            }
            Log.d(TAG, word);
        }


        Log.d(TAG, totalScore.toString());

        mTotalPointsValueTextView.setText(totalScore.toString());

        String[] listArr = new String[list.size()];
        listArr = list.toArray(listArr);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listArr);
        mListView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playAgainButton:
                Intent gameIntent = new Intent(ResultActivity.this, GameActivity.class);
                startActivity(gameIntent);
                break;
        }
    }
}
