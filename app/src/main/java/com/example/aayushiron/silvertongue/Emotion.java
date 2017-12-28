package com.example.aayushiron.silvertongue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Emotion extends AppCompatActivity {

    TextView happiness, anger, sadness, fear, neutrality, mainEmotion;
    ProgressBar happy, angry, sad, afraid, neutral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        mainEmotion = findViewById(R.id.textView6);

        happiness = findViewById(R.id.textView8);
        happy = findViewById(R.id.progressBar);

        anger = findViewById(R.id.textView9);
        angry = findViewById(R.id.progressBar2);

        sadness = findViewById(R.id.textView10);
        sad = findViewById(R.id.progressBar3);

        fear = findViewById(R.id.textView11);
        afraid = findViewById(R.id.progressBar4);

        neutrality = findViewById(R.id.textView12);
        neutral = findViewById(R.id.progressBar5);

        mainEmotion.setText(MainActivity.emotion);

        happiness.setText("Happiness: " + Double.toString(MainActivity.happiness) + "%");
        anger.setText("Anger: " + Double.toString(MainActivity.angriness) + "%");
        sadness.setText("Sadness: " + Double.toString(MainActivity.sadness) + "%");
        fear.setText("Fear: " + Double.toString(MainActivity.fear) + "%");
        neutrality.setText("Neutrality: " + Double.toString(MainActivity.neutrality) + "%");

        happy.setProgress((int) MainActivity.happiness);
        angry.setProgress((int) MainActivity.angriness);
        sad.setProgress((int) MainActivity.sadness);
        afraid.setProgress((int) MainActivity.fear);
        neutral.setProgress((int) MainActivity.neutrality);
    }
}
