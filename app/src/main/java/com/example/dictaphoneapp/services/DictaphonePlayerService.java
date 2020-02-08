package com.example.dictaphoneapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dictaphoneapp.DictaphoneActivity;
import com.example.dictaphoneapp.R;
import com.example.dictaphoneapp.model.MyMediaRecorder;



public class DictaphonePlayerService extends Service {


    public static final String CHANNEL_ID = "PlayerServiceChannel";
    public static final String TAG = "DictaphonePlayerService";
    public static final String FILE_LIST_ADAPTER_FILE_PATH = "FileListAdapterFilePath";
    public static final String FILEPATH = "FILEPATH";
    public static final String ACTION_PLAY = "PLAYER_SERVICE_ACTION_PLAY";
    public static final String ACTION_STOP = "PLAYER_SERVICE_ACTION_STOP";
    public static final String ACTION_START = "PLAYER_SERVICE_START";
    public static boolean PLAYER_SERVICE_RUNNING;
    private String filePath;


    private static final int NOTIFICATION_ID = 103;
    private MyMediaRecorder mMyMediaRecorder = new MyMediaRecorder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PLAYER_SERVICE_RUNNING = true;
        createNotificationChannel();
        Log.d(TAG, "onCreate: called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");

        if (intent.getStringExtra(FILE_LIST_ADAPTER_FILE_PATH) != null) {
            filePath = intent.getExtras().getString(FILE_LIST_ADAPTER_FILE_PATH);
        }


        if (ACTION_START.equals(intent.getAction())) {
            startForeground(NOTIFICATION_ID, createNotification());
            startPlaying(filePath);
        }

        if (ACTION_PLAY.equals(intent.getAction())) {
            Log.d(TAG, "onStartCommand: ACTION_PLAY");
//            filePath = intent.getExtras().getString(FILEPATH);
            Log.d(TAG, filePath);

            startPlaying(intent.getExtras().getString("FILEPATH"));
        }

        if (ACTION_STOP.equals(intent.getAction())) {
            Log.d(TAG, "onStartCommand: ACTION_STOP");
            stopPlaying();
        }


        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_player_channel_name);
            String description = getString(R.string.notification_player_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent intent = new Intent(this, DictaphoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent stopPlaying = new Intent(this, DictaphonePlayerService.class);
        stopPlaying.putExtra(FILEPATH, filePath);
        stopPlaying.setAction(ACTION_STOP);
        PendingIntent pendingIntentStopPlaying = PendingIntent.getService(this, 0, stopPlaying, 0);


        Intent startPlaying = new Intent(this, DictaphonePlayerService.class);
        startPlaying.putExtra(FILEPATH, filePath);
        startPlaying.setAction(ACTION_PLAY);
        PendingIntent pendingIntentStartPlaying = PendingIntent.getService(this, 0, startPlaying, 0);


        builder.setSmallIcon(R.drawable.ic_playing)
                .setColor(Color.GREEN)
                .setContentTitle(getString(R.string.notification_player_title))
                .setContentText(filePath)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(new NotificationCompat.Action(R.drawable.ic_baseline_play_arrow_24, getString(R.string.start_playing), pendingIntentStartPlaying))
                .addAction(new NotificationCompat.Action(R.drawable.ic_baseline_stop_24, getString(R.string.stop_playing), pendingIntentStopPlaying))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }

    private void stopPlaying() {

        mMyMediaRecorder.onPlay(false);

    }

    private void startPlaying(String path) {

        mMyMediaRecorder.onPlay(true, path);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PLAYER_SERVICE_RUNNING = false;
        Log.d(ContentValues.TAG, "onDestroy: called");
    }

}
