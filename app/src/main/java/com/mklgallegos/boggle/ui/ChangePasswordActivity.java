package com.mklgallegos.boggle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ChangePasswordActivity.class.getSimpleName();

    //Firebase
    private Firebase mFirebaseRef;

    //Buttons
    @Bind(R.id.changePasswordButton) Button mChangePasswordButton;

    //EditTexts
    @Bind(R.id.oldPasswordEditText) EditText mOldPasswordEditText;
    @Bind(R.id.newPasswordEditText) EditText mNewPasswordEditText;
    @Bind(R.id.confirmNewPasswordEditText) EditText mConfirmNewPasswordEditText;
    @Bind(R.id.emailEditText) EditText mEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        //click listeners
        mChangePasswordButton.setOnClickListener(this);

        //define Firebase ref
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePasswordButton:

                //grab value of inputs
                final String email = mEmailEditText.getText().toString();
                final String oldPassword = mOldPasswordEditText.getText().toString();
                final String newPassword = mNewPasswordEditText.getText().toString();


                mFirebaseRef.changePassword(email, oldPassword, newPassword, new Firebase.ResultHandler() {



                    @Override
                    public void onSuccess() {

                        //notify user password change was successful
                        Toast.makeText(ChangePasswordActivity.this, "Password Change Successful!", Toast.LENGTH_LONG).show();

                        //implement intent to transition user back to AccountActivity
                        Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
