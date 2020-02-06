package com.example.dictaphoneapp.model;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MyMediaRecorder {

    private static final String LOG_TAG = "AudioRecordTest";

    private static final String AUDIO_FORMAT = ".3gp";
    private static final String RECORDED_AUDIO_FOLDER = "RecordedAudio";
    private static final String FILE_PREFIX = "MyAudio";

    private MediaRecorder recorder;

    private MediaPlayer player;


    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean start, String filePath) {
        if (start) {
            startPlaying(filePath);
        } else {
            stopPlaying();
        }
    }

    public void onPlay(boolean stop) {
        stopPlaying();
    }

    private void startPlaying(String filePath) {
        player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            Log.d(TAG, "startPlaying: " + filePath);
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

        new File(Environment.getExternalStorageDirectory().getAbsolutePath(), File.separator + RECORDED_AUDIO_FOLDER).mkdirs();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File output = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + RECORDED_AUDIO_FOLDER, FILE_PREFIX + new Date().getTime() + AUDIO_FORMAT);

        recorder.setOutputFile(output.getAbsolutePath());
        recorder.setMaxDuration(0);

        try {

            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed" + e);
        }


    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

    }

    public void getFilesList(ArrayList<String> arrayList) {
        File outputFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + RECORDED_AUDIO_FOLDER);
        File[] files = outputFolder.listFiles();

        if (files != null) {
            int i = 0;
            for (File f : files) {
                if (f.isFile()) {
                    try {
                        arrayList.add(i, f.getAbsolutePath());
                        Log.d(TAG, "files: " + f.getAbsolutePath());
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}