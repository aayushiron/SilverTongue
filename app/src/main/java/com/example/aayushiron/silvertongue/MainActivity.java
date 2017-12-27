package com.example.aayushiron.silvertongue;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karan.churi.PermissionManager.PermissionManager;
import com.projects.alshell.vokaturi.Emotion;
import com.projects.alshell.vokaturi.EmotionProbabilities;
import com.projects.alshell.vokaturi.Vokaturi;
import com.projects.alshell.vokaturi.VokaturiException;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnDSListener, OnDSPermissionsListener {

    Vokaturi vokaturiApi;
    Button record, stop;
    EmotionProbabilities emotionProbabilities = null;
    public static String emotion;
    DroidSpeech droidSpeech;
    double startTime;
    double elapsedTime;
    public static int wpm;
    PermissionManager permissionManager;
    TextView txt;
    int MY_DATA_CHECK_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        droidSpeech = new DroidSpeech(this, null);
        droidSpeech.setOnDroidSpeechListener(this);
        droidSpeech.setOfflineSpeechRecognition(false);
        droidSpeech.setContinuousSpeechRecognition(true);

        txt = findViewById(R.id.textView);

        try {
            vokaturiApi = Vokaturi.getInstance(getApplicationContext());
        } catch (VokaturiException e) {
            e.printStackTrace();
        }

        stop = findViewById(R.id.button2);
        stop.setVisibility(View.GONE);

        record = findViewById(R.id.button);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Please start presenting", Toast.LENGTH_SHORT).show();
                startTime = System.currentTimeMillis();
                try {
                    vokaturiApi.startListeningForSpeech();
                } catch (VokaturiException e) {
                    e.printStackTrace();
                }
                droidSpeech.startDroidSpeechRecognition();
                record.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                droidSpeech.closeDroidSpeechOperations();
                elapsedTime = (System.currentTimeMillis() - startTime) / 3600000;
                try {
                    emotionProbabilities = vokaturiApi.stopListeningAndAnalyze();
                } catch (VokaturiException e) {
                    e.printStackTrace();
                }

                Emotion capturedEmotion = Vokaturi.extractEmotion(emotionProbabilities);

                emotion = capturedEmotion.toString();

                Intent i = new Intent(getApplicationContext(), StatsPage.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {

    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {

    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {

    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult)
    {

        String s = finalSpeechResult;
        int count = 1;

        for (int i = 0; i < s.length() - 1; i++)
        {
            if ((s.charAt(i) == ' ') && (s.charAt(i + 1) != ' '))
            {
                count++;

            }
        }
        wpm = (int) (count/elapsedTime);
    }

    @Override
    public void onDroidSpeechClosedByUser() {

    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny) {

    }


}
