package com.HeyWorld.countwndowntimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final long START_TIME_IN_MILLIS = 600000;

    private TextView txtViewCountDown;
    private Button startPauseBtn, resetBtn;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;

    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    //onCreate
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        txtViewCountDown = findViewById (R.id.txt_view_count_down);
        startPauseBtn = findViewById (R.id.button_start_pause);
        resetBtn = findViewById (R.id.button_reset);

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

        updateCountDownTxt (); /*-- We will show the time that left --*/
    }
    //onCreate

    //Start timer. It runs inside startPauseBtn()
    private void startTimer(){
        Log.d (TAG, "startTimer: start timer method ran OK");

        countDownTimer = new CountDownTimer (timeLeftInMillis, 1000) {
            @Override
            public void onTick (long l){
            timeLeftInMillis = l;
            updateCountDownTxt();
            }

            @Override
            public void onFinish (){
            timerRunning = false;
            startPauseBtn.setText ("Start");
            startPauseBtn.setVisibility (View.INVISIBLE);
            resetBtn.setVisibility (View.VISIBLE);
            }
        }.start ();

        timerRunning = true;
        startPauseBtn.setText ("Pause");
        resetBtn.setVisibility (View.INVISIBLE);
    }
    //Start timer. It runs inside startPauseBtn()

    //It shows the time. It runs inside startTimer()
    private void updateCountDownTxt(){
        Log.d (TAG, "updateCountDownTxt: timer start working");

        int minutes = (int) (timeLeftInMillis / 1000) / 60; /*-- To convert the time to minutes-*/
        int seconds = (int) (timeLeftInMillis / 1000) % 60; /*-- Converts the result of minutes to seconds --*/

        String timeLeftFormatted = String.format (Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txtViewCountDown.setText (timeLeftFormatted);
    }
    //It shows the time. It runs inside startTimer()

    //Pause the timer. It runs inside StartPauseBtn()
    private void pauseTimer(){
        Log.d (TAG, "pauseTimer: timer paused");

        countDownTimer.cancel ();
        timerRunning = false;
        startPauseBtn.setText ("Start");
        resetBtn.setVisibility (View.VISIBLE);
    }
    //Pause the timer. It runs inside StartPauseBtn()

    //Reset the timer. It runs inside ResetBtn()
    private void resetTimer(){
        Log.d (TAG, "resetTimer: timer rested it");

        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownTxt ();
        resetBtn.setVisibility (View.INVISIBLE);
        startPauseBtn.setVisibility (View.VISIBLE);
    }
    //Reset the timer. It runs inside ResetBtn()



}//Main