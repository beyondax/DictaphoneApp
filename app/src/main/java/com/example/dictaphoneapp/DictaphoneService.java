package com.example.dictaphoneapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static android.content.ContentValues.TAG;

public class DictaphoneService extends Service {


    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String ACTION_CLOSE = "FOREGROUND_SERVICE_ACTION_CLOSE";
    private static final int NOTIFICATION_ID = 1;


    MyMediaRecorder mMyMediaRecorder = new MyMediaRecorder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d(TAG, "onCreate: called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_CLOSE.equals(intent.getAction())) {
            stopSelf();
        } else {
            startRecording();
            startForeground(NOTIFICATION_ID, createNotification());
        }

        return START_NOT_STICKY;
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent intent = new Intent(this, DictaphoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent intentCloseService = new Intent(this, DictaphoneService.class);
        intentCloseService.setAction(ACTION_CLOSE);
        PendingIntent pendingIntentCloseService = PendingIntent.getService(this, 0, intentCloseService, 0);

        builder.setSmallIcon(R.drawable.ic_recording)
                .setColor(Color.RED)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_stop, getString(R.string.stop_recording), pendingIntentCloseService)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startRecording() {
        mMyMediaRecorder.onRecord(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
