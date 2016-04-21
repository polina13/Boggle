package com.mklgallegos.boggle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.testTextView) TextView mTestTextView;
    @Bind(R.id.generateNewStringButton) Button mGenerateNewStringButton;

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

//      Scramble
        for( int i=0 ; i<text.length ; i++ ) {
            int j = random.nextInt(text.length);
            // Swap letters
            char temp = text[i]; text[i] = text[j];  text[j] = temp;
        }
        return new String(text);
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
                mTestTextView.setText(generateString());
            }
        });
    }
}
