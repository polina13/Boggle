package com.mklgallegos.boggle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mklgallegos.boggle.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.changeEmailButton) Button mChangeEmailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        //click listeners
        mChangeEmailButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeEmailButton:
                Intent intent = new Intent(AccountActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
                break;
        }

    }
}
