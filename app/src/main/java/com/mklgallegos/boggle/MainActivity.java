package com.mklgallegos.boggle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.testTextView) TextView mTestTextView;
    @Bind(R.id.generateNewStringButton) Button mGenerateNewStringButton;
    @Bind(R.id.shuffleStringButton) Button mShuffleStringButton;
    @Bind(R.id.addWordButton) Button mAddWordButton;
    @Bind(R.id.inputStringEditText) EditText  mInputStringEditText;
    public ArrayList<String> list = new ArrayList<String>();

    public String generateString() {
        Random random = new Random();
        char[] text = new char[8];
        String consonants = "bcdfghjklmnpqrstvwxyz";
        String vowels = "aeiou";
        for (int i = 0; i < 6; i++)  {
            text[i] = consonants.charAt(random.nextInt(consonants.length()));
        }
        for (int i = 6; i < 8; i++) {
            text[i] = vowels.charAt(random.nextInt(vowels.length()));
        }
        return new String(text);
    }

    public String shuffleString(String text) {
        Random random = new Random();
        //convert string to array of characters
        char[] charArray = text.toCharArray();
        //run array of characters through shuffle algorithm
        for( int i=0 ; i<charArray.length ; i++ ) {
            int j = random.nextInt(charArray.length);
            // Swap letters
            char temp = charArray[i]; charArray[i] = charArray[j];  charArray[j] = temp;
        }
        //return string
        return new String(charArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTestTextView.setText(generateString());

        mGenerateNewStringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestTextView.setText(shuffleString(generateString()));
            }
        });

        mShuffleStringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestTextView.setText(shuffleString(mTestTextView.getText().toString()));
            }
        });

        mAddWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //collect input from user
                String userInput = mInputStringEditText.getText().toString();

                //log inputs
                Log.d(TAG, userInput);

                list.add(userInput);

                Log.d(TAG, list.toString());

                mInputStringEditText.setText(null);
            }
        });
    }
}
