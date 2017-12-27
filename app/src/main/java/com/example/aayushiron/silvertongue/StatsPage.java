package com.example.aayushiron.silvertongue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatsPage extends AppCompatActivity {

    TextView emotion, speed, wpm;
    String statement;
    int wpmAway;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);
        emotion = findViewById(R.id.textView3);
        speed = findViewById(R.id.textView4);
        wpm = findViewById(R.id.textView5);

        emotion.setText("You are speaking in a " + MainActivity.emotion + " tone.");

        wpm.setText("You are saying " + Integer.toString(MainActivity.wpm) + " words per minute.");

        if (MainActivity.wpm <= 150 && MainActivity.wpm >= 110) {
            statement = "just right";
            wpmAway = 0;
        } else if (MainActivity.wpm < 110) {
            statement = "slow";
            wpmAway = 110 - MainActivity.wpm;
        } else if (MainActivity.wpm > 150) {
            statement = "fast";
            wpmAway = MainActivity.wpm - 150;
        }
        speed.setText("You are speaking " + statement + ". You are " + Integer.toString(wpmAway) + " words per minute away from speaking at a normal pace.");
    }
}
