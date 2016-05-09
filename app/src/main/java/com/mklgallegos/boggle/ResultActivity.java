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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mklgallegos.boggle.models.Game;
import com.mklgallegos.boggle.models.User;

import java.util.ArrayList;


import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = ResultActivity.class.getSimpleName();

    private ArrayList<String> list;
    private String[] listArr;
    private ArrayAdapter adapter;

    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;
    private Firebase mFirebaseRef;
    private Firebase mGameLocationRef;
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

        mPlayAgainButton.setOnClickListener(this);


        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);



        mGameLocationRef = new Firebase(Constants.FIREBASE_URL_GAMES + "/" + mUId);

        mGameLocationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Game game = snapshot.getValue(Game.class);

                mTotalPointsValueTextView.setText(game.getTotalPoints().toString());
                mTimestampValueTextView.setText(game.getDate().toString());

                list = game.getList();
                listArr = new String[list.size()];
                listArr = list.toArray(listArr);
                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_expandable_list_item_1, listArr);
                mListView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String string) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


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
