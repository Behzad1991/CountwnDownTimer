package com.HeyWorld.countwndowntimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText inputEditTxt;
    private TextView txtViewCountDown;
    private Button startPauseBtn, resetBtn, setBtn;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;

    private long timeLeftInMillis;
    private long endTime, startTimeInMillis;

    //onCreate
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        inputEditTxt = findViewById (R.id.input);
        txtViewCountDown = findViewById (R.id.txt_view_count_down);
        startPauseBtn = findViewById (R.id.button_start_pause);
        resetBtn = findViewById (R.id.button_reset);
        setBtn = findViewById (R.id.set_btn);

        /*-- It sets the time based on user's desire --*/
        setBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view){
            String sInput = inputEditTxt.getText ().toString ();

            if(sInput.length () == 0){
                Toast.makeText (MainActivity.this, "Field cannot be empty!", Toast.LENGTH_SHORT).show ();
                return;
            }

            long millisInput = Long.parseLong (sInput) * 60000; /** It converts the time to minutes*/
            if(millisInput == 0){
                Toast.makeText (MainActivity.this, "Enter a valid number", Toast.LENGTH_SHORT).show ();
                return;
            }

            setTime (millisInput);
            inputEditTxt.setText ("");
            }
        });
        /*-- It sets the time based on user's desire --*/

        /*-- Start pause btn runs --*/
        startPauseBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view){
                if(timerRunning){
                    Log.d (TAG, "onClick: timer is running and we paused it");
                    pauseTimer();
                }
                else{
                    Log.d (TAG, "onClick: Timer started");
                    startTimer();
                }
            }
        });
        /*-- Start pause btn runs --*/

        /*-- Reset btn runs --*/
        resetBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view){
                resetTimer();
            }
        });
        /*-- Reset btn runs --*/

    }
    //onCreate

    //It sets time
    private void setTime(long milliseconds){
        startTimeInMillis = milliseconds;
        resetTimer ();
        closeKeyBoard ();
    }
    //It sets time

    //Start timer. It runs inside startPauseBtn()
    private void startTimer(){
        Log.d (TAG, "startTimer: start timer method ran OK");

        endTime = System.currentTimeMillis () + timeLeftInMillis;

        countDownTimer = new CountDownTimer (timeLeftInMillis, 1000) {
            @Override
            public void onTick (long l){
            timeLeftInMillis = l;
            updateCountDownTxt();
            }

            @Override
            public void onFinish (){
            timerRunning = false;
            updateWatchInterface ();
            }
        }.start ();

        timerRunning = true;
        updateWatchInterface ();
    }
    //Start timer. It runs inside startPauseBtn()

    //Pause the timer. It runs inside StartPauseBtn()
    private void pauseTimer(){
        Log.d (TAG, "pauseTimer: timer paused");

        countDownTimer.cancel ();
        timerRunning = false;
        updateWatchInterface ();
    }
    //Pause the timer. It runs inside StartPauseBtn()

    //Reset the timer. It runs inside ResetBtn()
    private void resetTimer(){
        Log.d (TAG, "resetTimer: timer rested it");

        timeLeftInMillis = startTimeInMillis;
        updateCountDownTxt ();
        updateWatchInterface ();
    }
    //Reset the timer. It runs inside ResetBtn()

    //It shows the time. It runs inside startTimer()
    private void updateCountDownTxt(){
        Log.d (TAG, "updateCountDownTxt: timer start working");

        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60; /*-- To convert the time to minutes-*/
        int seconds = (int) (timeLeftInMillis / 1000) % 60; /*-- Converts the result of minutes to seconds --*/

        String timeLeftFormatted;
        if(hours > 0){
            timeLeftFormatted = String.format (Locale.getDefault(), "%d:%02d:%02d",hours, minutes, seconds);
        }else{
            timeLeftFormatted = String.format (Locale.getDefault(), "%02d:%02d",  minutes, seconds);
        }
        txtViewCountDown.setText (timeLeftFormatted);
    }
    //It shows the time. It runs inside startTimer()

    //Handle the buttons condition in this method. It will be used inside all funs that need updated button
    private void updateWatchInterface(){
        Log.d (TAG, "updateButton: update buttons");

        if(timerRunning){
            inputEditTxt.setVisibility (View.INVISIBLE);
            setBtn.setVisibility (View.INVISIBLE);
            resetBtn.setVisibility (View.INVISIBLE);
            startPauseBtn.setText ("Pause");
        }else{
            inputEditTxt.setVisibility (View.VISIBLE);
            setBtn.setVisibility (View.VISIBLE);
            startPauseBtn.setText ("Start");

            if(timeLeftInMillis < 1000){
                startPauseBtn.setVisibility (View.INVISIBLE);
            }else{
                startPauseBtn.setVisibility (View.VISIBLE);
            }

            if(timeLeftInMillis < startTimeInMillis){
                resetBtn.setVisibility (View.VISIBLE);
            }else{
                resetBtn.setVisibility (View.INVISIBLE);
            }
        }
    }
    //Handle the buttons condition in this method. It will be used inside all funs that need updated button


    //It will close the soft keyboard.
    private void closeKeyBoard(){
    View v = this.getCurrentFocus ();
    if(v != null){
        InputMethodManager imm = (InputMethodManager) getSystemService (INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow (v.getWindowToken (), 0);
    }
    }
    //It will close the soft keyboard.

    //We need to let the timer run even if the app is closed. By using Stop & Start we save the data
    @Override
    protected void onStop (){
        super.onStop ();

        SharedPreferences prefs = getSharedPreferences ("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();

        editor.putLong ("startTimeInMillis", startTimeInMillis);
        editor.putLong ("millisLeft", timeLeftInMillis);
        editor.putBoolean ("timerRunning", timerRunning);
        editor.putLong ("endTime", endTime);

        editor.apply (); /**It will save the above values*/

        if(countDownTimer != null){
            countDownTimer.cancel ();
        }
    }

    @Override
    protected void onStart (){
        super.onStart ();

        SharedPreferences prefs = getSharedPreferences ("prefs", MODE_PRIVATE);

        startTimeInMillis = prefs.getLong ("startTimeInMillis", 60000);
        timeLeftInMillis = prefs.getLong ("millisLeft", startTimeInMillis);
        timerRunning = prefs.getBoolean ("timerRunning", false);

        updateCountDownTxt ();
        updateWatchInterface ();

        if(timerRunning){
            endTime = prefs.getLong ("endTime", 0);
            timeLeftInMillis = endTime - System.currentTimeMillis ();

            if(timeLeftInMillis < 0){
                timeLeftInMillis = 0;
                timerRunning = false;
                updateCountDownTxt ();
                updateWatchInterface ();
            }else{
                startTimer ();
            }
        }
    }
    //We need to let the timer run even if the app is closed. By using Stop & Start we save the data




}//Main