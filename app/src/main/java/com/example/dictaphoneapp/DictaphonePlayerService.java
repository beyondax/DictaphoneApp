package com.example.dictaphoneapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;
import static com.example.dictaphoneapp.DictaphoneRecorderService.ACTION_CLOSE;


public class DictaphonePlayerService extends Service {


    private static final int NOTIFICATION_ID = 102;
    private static final String CHANNEL_ID = "Playing";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        if (ACTION_CLOSE.equals(intent.getAction())) {
            stopSelf();
        }

        startPlaying();

        startForeground(NOTIFICATION_ID, createNotification());

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_player_channel_name);
            String description = getString(R.string.notification_player_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: called");

        stopPlaying();

    }

    private void stopPlaying() {

    }

    private Notification createNotification() {
        return null;
    }

    private void startPlaying() {
    }


}
