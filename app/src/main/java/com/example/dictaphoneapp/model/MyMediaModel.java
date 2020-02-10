package com.example.dictaphoneapp.model;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MyMediaModel {

    private static final String TAG = "MyMediaRecorder";

    private final String AUDIO_FORMAT = ".3gp";
    private final String AUDIO_FOLDER = "Recorded";
    private final String FILE_PREFIX = "MyAudio";

    private MediaPlayer player;
    private MediaRecorder recorder;

    private boolean isRecording = false;
    private boolean isPaused = false;
    private boolean isPlaying = false;


//    public boolean isPaused() {
//        return isPaused;
//    }
//    public boolean isRecording() {
//        return isRecording;
//    }

    public boolean isPlaying() {
        return isPlaying;
    }


    public void onRecord(boolean start) {
        if (start) {
            isRecording = true;
            startRecording();
        } else {
            isRecording = false;
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

    public void onPlay(boolean start) {
        stopPlaying();
    }

//    public void onPause(boolean start) {
//        if (start) {
//            isPaused = true;
//            player.pause();
//        } else {
//            isPaused = false;
//            player.start();
//        }
//    }


    private void startPlaying(String filePath) {

        player = new MediaPlayer();

        try {

            Log.d(TAG, "startPlaying: " + filePath);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            player.setDataSource(filePath);
            player.prepareAsync();

        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }

    }

    private void startRecording() {
        recorder = new MediaRecorder();

        createDir();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File output = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + AUDIO_FOLDER, FILE_PREFIX
                + new SimpleDateFormat("HH-mm-ss FF-LL-YY").format(new Date())
                + AUDIO_FORMAT);

        recorder.setOutputFile(output.getAbsolutePath());
        recorder.setMaxDuration(0);

        try {

            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            Log.e(TAG, "prepare() failed" + e);
        }
    }

    private void createDir() {
        new File(Environment.getExternalStorageDirectory().getAbsolutePath(), File.separator + AUDIO_FOLDER).mkdirs();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public void getFilesList(ArrayList<String> arrayList) {
        File outputFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + AUDIO_FOLDER);
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