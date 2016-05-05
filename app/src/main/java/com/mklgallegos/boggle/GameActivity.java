package com.mklgallegos.boggle;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = GameActivity.class.getSimpleName();
    @Bind(R.id.testTextView) TextView mTestTextView;
    @Bind(R.id.generateNewStringButton) Button mGenerateNewStringButton;
    @Bind(R.id.shuffleStringButton) Button mShuffleStringButton;
    @Bind(R.id.addWordButton) Button mAddWordButton;
    @Bind(R.id.inputStringEditText) EditText  mInputStringEditText;
    @Bind(R.id.endRoundButton) Button mEndRoundButton;
    @Bind(R.id.timerTextView) TextView mTimerTextView;
    public ArrayList<String> list = new ArrayList<String>();
    HashSet<String> dictionary = new HashSet<>();

    public void loadDictionary() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("web2")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                dictionary.add(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

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
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        loadDictionary();

        mGenerateNewStringButton.setOnClickListener(this);
        mShuffleStringButton.setOnClickListener(this);
        mAddWordButton.setOnClickListener(this);
        mEndRoundButton.setOnClickListener(this);

        Integer dictSize = dictionary.size();
        String dictSizeString = dictSize.toString();

        Log.d(TAG, dictSizeString);


        mTestTextView.setText(generateString());

        new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 180000 && millisUntilFinished >= 130000 ) {
                    mTimerTextView.setText("02:" + ((millisUntilFinished / 1000) - 120));
                } else if (millisUntilFinished < 130000 && millisUntilFinished >= 120000) {
                    mTimerTextView.setText("02:" + "0" + ((millisUntilFinished / 1000) - 120));
                } else if (millisUntilFinished < 120000 && millisUntilFinished >= 70000) {
                    mTimerTextView.setText("01:" + ((millisUntilFinished / 1000) - 60));
                } else if (millisUntilFinished < 70000 && millisUntilFinished >= 60000) {
                    mTimerTextView.setText("01:" + "0" + ((millisUntilFinished / 1000) - 60));
                } else if (millisUntilFinished < 60000 && millisUntilFinished >=10000) {
                    mTimerTextView.setText("00:" + (millisUntilFinished / 1000));
                } else if (millisUntilFinished < 10000) {
                    mTimerTextView.setText("00:" + "0" + (millisUntilFinished / 1000));
                }
            }

            public void onFinish() {
                mTimerTextView.setText("done!");
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("list", list);
                startActivity(intent);
                finish();
            }
        }.start();
    }



        @Override
            public void onClick(View v) {
            switch (v.getId()) {
                case R.id.generateNewStringButton:
                    mTestTextView.setText(shuffleString(generateString()));
                    list.clear();
                    break;
                case R.id.shuffleStringButton:
                    mTestTextView.setText(shuffleString(mTestTextView.getText().toString()));
                    break;
                case R.id.addWordButton:
                    //collect input
                    String userInput = mInputStringEditText.getText().toString();
                    String randomGeneratedString = mTestTextView.getText().toString();

                    char[] randomCharArray = randomGeneratedString.toCharArray();

                    String testString = new String();

                    if (userInput.matches("")) {

                    }


                    if (userInput.length() >= 3) {

                        if (dictionary.contains(userInput)) {

                            for (int i = 0; i < userInput.length(); i++ ) {

                                for (int j = 0; j < randomCharArray.length; j++) {

                                    if (userInput.charAt(i) == randomCharArray[j]) {
                                        testString += userInput.charAt(i);
                                        randomCharArray[j] = 0;
                                        break;
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(GameActivity.this, "That's not a word!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!userInput.matches("")) {
                        if (testString.equals(userInput)) {
                            if (list.size() > 0) {
                                boolean isRepeat = false;
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).equals(userInput)) {
                                        Toast.makeText(GameActivity.this, "No Repeats!", Toast.LENGTH_SHORT).show();
                                        isRepeat = true;
                                        break;
                                    }
                                }
                                if (isRepeat == false) {
                                    list.add(userInput);
                                    Toast.makeText(GameActivity.this, "Good job!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                list.add(userInput);
                                Toast.makeText(GameActivity.this, "Good job!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GameActivity.this, "illegal word!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GameActivity.this, "you must enter a word", Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, list.toString());
                    mInputStringEditText.setText(null);
                    break;
                case R.id.endRoundButton:
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    intent.putExtra("list", list);
                    startActivity(intent);
                    break;
            }
        }
    }