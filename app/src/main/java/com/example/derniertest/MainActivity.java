package com.example.derniertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.media.MediaPlayer;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private static final String LOG_TAG="AudioRecording";
    private static String FileName=null;

    private static final int REQUEST_AUDIO_PERMISSION_CODE=1;

    ImageButton recordButton,playButton,stopButton,pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton=findViewById(R.id.record);
        playButton=findViewById(R.id.play);
        stopButton=findViewById(R.id.stop);
        pauseButton=findViewById(R.id.pause);

        stopButton.setEnabled(false);
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);

        FileName= Environment.getExternalStorageDirectory().getAbsolutePath();

        FileName+="/AudioRecording.3gp";

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    stopButton.setEnabled(true);
                    recordButton.setEnabled(false);
                    playButton.setEnabled(false);
                    pauseButton.setEnabled(false);

                    mediaRecorder = new MediaRecorder();

                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(FileName);

                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "prepare() failed");
                    }

                    mediaRecorder.start();
                    Toast.makeText(MainActivity.this, "Recording..", Toast.LENGTH_SHORT).show();

                }
                else{
                    RequiresPermission();
                }
            }
        });

       stopButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               pauseButton.setEnabled(true);
               playButton.setEnabled(true);
               stopButton.setEnabled(false);
               recordButton.setEnabled(true);


               mediaRecorder.stop();
               mediaRecorder.release();
               mediaRecorder=null;

               Toast.makeText(MainActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
           }
       });

       playButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               pauseButton.setEnabled(true);
               playButton.setEnabled(false);
               recordButton.setEnabled(true);
               stopButton.setEnabled(false);

               mediaPlayer=new MediaPlayer();

               try {
                   mediaPlayer.setDataSource(FileName);
                   mediaPlayer.prepare();
                   mediaPlayer.start();
                   Toast.makeText(MainActivity.this, "Recording start Playing", Toast.LENGTH_SHORT).show();
               } catch (IOException e) {
                   e.printStackTrace();
                   Log.e(LOG_TAG, "prepare() failed");
               }
           }


       });

       pauseButton.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               mediaPlayer.release();
               mediaPlayer=null;

               Toast.makeText(MainActivity.this, " Audio Playing Stopped", Toast.LENGTH_SHORT).show();
           }
       });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_AUDIO_PERMISSION_CODE:
                if(grantResults.length>0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                    break;
                }

        }

    public boolean CheckPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

      return result==PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;

    }

   public void RequiresPermission(){
       ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE},REQUEST_AUDIO_PERMISSION_CODE);
   }

    }

