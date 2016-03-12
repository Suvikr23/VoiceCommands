package com.nikhiltyagi.nikhil.voicecommands;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends CustomListenerActivity {

    private Intent speechIntent;
    private SpeechRecognizer speechRecognizer;
    private GameClass newGame;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGame = new GameClass();
    }

    @Override
    protected void onResume(){
        super.onResume();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        updateBoard();
    }

    private void updateBoard(){
        GridLayout boardView = (GridLayout)findViewById(R.id.boardView);
        boardView.removeAllViews();

        char[][] currentConfig = newGame.getBoardConfig();
        for(int i=0;i<4;i++){
            for (int j=0;j<4;j++) {
                TextView textView = new TextView(this);

                textView.setTextSize(40);
                textView.setWidth(100);
                textView.setWidth(100);
                textView.setGravity(Gravity.CENTER);
                boardView.addView(textView);
                if(i==0){
                    if(j==0) textView.setText("");
                    else textView.setText(String.valueOf(j));
                }
                else if(j==0){
                    textView.setText(Character.toString((char) (64 + i)));
                }
                else{
                    textView.setBackgroundResource(R.drawable.border);
                    textView.setText(String.valueOf(currentConfig[i-1][j-1]));
                }
            }
        }

        int gameStatus = newGame.returnGameStatus();
        TextView status = (TextView)findViewById(R.id.status);
        Button listenButton = (Button)findViewById(R.id.listenButton);

        if(gameStatus>0||gameStatus==-1){
            String result;
            if(gameStatus>0)
                result = ((gameStatus == 1) ? "X" : "O")+" Won";
            else
                result = "Game Draw";
            status.setText(result);
            speak(result);
            listenButton.setEnabled(false);
        }
        else{
            status.setText("Player "+newGame.whichPlayer()+" Turn");
        }

        Log.wtf("moved at", newGame.toString());
    }

    @SuppressWarnings("deprecation")
    private void speak(final String result){

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.UK);
                    textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

    }

    @Override
    public void resultsProcessing(ArrayList<String> data) {

        boolean flag = false;
        for (String result:data) {
            for(int i=1;i<4;i++){
                for (int j=1;j<4;j++) {
                    if (result.toUpperCase().contains(Character.toString((char) (64 + i))+String.valueOf(j))){
                        String currentPlayer = newGame.whichPlayer();
                        if(newGame.playerMove(i-1,j-1)) {
                            speak("Player "+currentPlayer+" marked on "+Character.toString((char) (64 + i))+String.valueOf(j));
                            updateBoard();
                            flag = true;
                        }
                        else{
                            speak("Player "+currentPlayer+" can't mark on "+Character.toString((char) (64 + i))+String.valueOf(j));
                            flag = true;
                        }
                        break;
                    }
                }
                if(flag) break;
            }
            if(flag) break;
        }
        if(!flag){
            speak("Unable to understand please repeat.");
        }
    }

    public void startListening(View v){
        speechRecognizer.startListening(speechIntent);
    }
}
