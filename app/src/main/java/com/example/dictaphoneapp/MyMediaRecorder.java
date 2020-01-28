package com.example.dictaphoneapp;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MyMediaRecorder {

    private static final String LOG_TAG = "AudioRecordTest";

    private static String fileName = Environment.getDataDirectory().toString();

    private MediaRecorder recorder = null;

    private MediaPlayer player = null;


    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            Log.d(TAG, "startPlaying: " + fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        File outputFolder = new File("sdcard/RecordedAudio/MyAudio");
        File output = new File(outputFolder.getAbsolutePath() + new Date().getTime() + ".3gp");
        Log.i(TAG, "startRecording: " + output.getAbsolutePath());
        recorder.setOutputFile(output.getAbsolutePath());
        recorder.setMaxDuration(3000);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }


    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}