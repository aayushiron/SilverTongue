package com.example.aayushiron.silvertongue;

import android.app.Activity;
import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
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
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OnDSListener {

    Vokaturi vokaturiApi;
    Button startEmotion, startSpeed, stopEmotion, stopSpeed;
    EmotionProbabilities emotionProbabilities = null;
    public static String emotion;
    DroidSpeech droidSpeech;
    double startTime;
    double elapsedTime;
    public static int wpm;
    public static double happiness, sadness, neutrality, angriness, fear;
    PermissionManager permissionManager;
    SpeechToText speechService;
    private StreamPlayer player = new StreamPlayer();
    private MicrophoneHelper microphoneHelper;

    private MicrophoneInputStream capture;
    private boolean listening = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        microphoneHelper = new MicrophoneHelper(this);

        try {
            vokaturiApi = Vokaturi.getInstance(getApplicationContext());
        } catch (VokaturiException e) {
            e.printStackTrace();
        }

        if (SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
            droidSpeech = new DroidSpeech(this, null);
            droidSpeech.setOnDroidSpeechListener(this);
            droidSpeech.setOfflineSpeechRecognition(true);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot get words per minute as Speech package is not installed", Toast.LENGTH_SHORT).show();
        }

        stopEmotion = findViewById(R.id.button2);
        stopEmotion.setVisibility(View.GONE);

        stopSpeed = findViewById(R.id.button4);
        stopSpeed.setVisibility(View.GONE);

        startEmotion = findViewById(R.id.button);

        startEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Please start presenting", Toast.LENGTH_SHORT).show();
                try {
                    vokaturiApi.startListeningForSpeech();
                } catch (VokaturiException e) {
                    e.printStackTrace();
                }
                startEmotion.setVisibility(View.GONE);
                startSpeed.setVisibility(View.GONE);
                stopEmotion.setVisibility(View.VISIBLE);
            }
        });

        startSpeed = findViewById(R.id.button3);

        startSpeed.setVisibility(View.GONE);

        startSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "Please start presenting", Toast.LENGTH_SHORT).show();
//                if (!listening) {
//                    capture = microphoneHelper.getInputStream(true);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                speechService.recognizeUsingWebSocket(capture, getRecognizeOptions(), new MicrophoneRecognizeDelegate());
//                            } catch (Exception e) {
//
//                            }
//                        }
//                    }).start();
//                    listening = true;
//                } else {
//                    microphoneHelper.closeInputStream();
//                    listening = false;
//                }


                startSpeed.setVisibility(View.GONE);
                startEmotion.setVisibility(View.GONE);
                stopSpeed.setVisibility(View.VISIBLE);
            }
        });

        stopEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    emotionProbabilities = vokaturiApi.stopListeningAndAnalyze();
                    emotionProbabilities.scaledValues(5);
                    Emotion capturedEmotion = Vokaturi.extractEmotion(emotionProbabilities);

                    emotion = capturedEmotion.toString();

                    happiness = emotionProbabilities.Happiness * 100;
                    neutrality = emotionProbabilities.Neutrality * 100;
                    angriness = emotionProbabilities.Anger * 100;
                    sadness = emotionProbabilities.Sadness * 100;
                    fear = emotionProbabilities.Fear * 100;

                    Intent i = new Intent(getApplicationContext(), com.example.aayushiron.silvertongue.Emotion.class);
                    startActivity(i);
                } catch (VokaturiException e) {
                    if (e.getErrorCode() == VokaturiException.VOKATURI_NOT_ENOUGH_SONORANCY) {
                        Toast.makeText(getApplicationContext(), "Unable to hear you. Please try again in a less noisy area or speak a little louder.", Toast.LENGTH_LONG).show();
                    } else if (e.getErrorCode() == VokaturiException.VOKATURI_DENIED_PERMISSIONS) {
                        Toast.makeText(getApplicationContext(), "Please give the application Microphone and Writing access", Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

        stopSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 60000;
                try {
                    droidSpeech.closeDroidSpeechOperations();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

                //onDroidSpeechFinalResult("Test");

                //Toast.makeText(getApplicationContext(), "After closing operations", Toast.LENGTH_LONG).show();

                stopSpeed.setVisibility(View.GONE);
//
//                Intent i = new Intent(getApplicationContext(), Speed.class);
//                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        if(supportedSpeechLanguages.contains("en-US")) {
            // Setting the droid speech preferred language as english if found
            droidSpeech.setPreferredLanguage("en-US");
        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {

    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {

    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        Toast.makeText(getApplicationContext(), finalSpeechResult, Toast.LENGTH_LONG).show();

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

        Intent i = new Intent(getApplicationContext(), Speed.class);
        startActivity(i);
    }

    @Override
    public void onDroidSpeechClosedByUser() {

    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), Speed.class);
        startActivity(i);
    }
}
