package com.example.aayushiron.silvertongue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Speed extends AppCompatActivity {

    TextView speed, fasterSlower, range;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        speed = findViewById(R.id.textView24);
        fasterSlower = findViewById(R.id.textView27);
        range = findViewById(R.id.textView28);

        speed.setText(MainActivity.wpm);
        if (MainActivity.wpm <= 150 && MainActivity.wpm >= 110) {
            fasterSlower.setText("You are speaking just right");
        } else if (MainActivity.wpm < 110) {
            fasterSlower.setText("Try to speak faster");
            range.setText("The optimal range is 110 - 150 wpm");
        } else if (MainActivity.wpm > 150) {
            fasterSlower.setText("Try to speak slower");
            range.setText("The optimal range is 110 - 150 wpm");
        }

    }
}
