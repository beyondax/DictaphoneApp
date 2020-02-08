package com.example.dictaphoneapp.services;

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

import com.example.dictaphoneapp.DictaphoneActivity;
import com.example.dictaphoneapp.R;
import com.example.dictaphoneapp.model.MyMediaRecorder;

public class DictaphoneRecorderService extends Service {

    public static boolean RECORDER_SERVICE_RUNNING;
    public static final String CHANNEL_ID = "PlayerServiceChannel";
    public static final String ACTION_CLOSE = "RECORDER_SERVICE_ACTION_CLOSE";
    public static String TAG = "DictaphoneRecorderService";

    private static final int NOTIFICATION_ID = 1;
    private MyMediaRecorder mMyMediaRecorder = new MyMediaRecorder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        RECORDER_SERVICE_RUNNING = true;
        createNotificationChannel();
        Log.d(TAG, "onCreate: called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_CLOSE.equals(intent.getAction())) {
            stopRecording();
            stopSelf();
        } else {
            startForeground(NOTIFICATION_ID, createNotification());
            startRecording();
        }

        return START_NOT_STICKY;
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent intent = new Intent(this, DictaphoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent intentCloseService = new Intent(this, DictaphoneRecorderService.class);
        intentCloseService.setAction(ACTION_CLOSE);
        PendingIntent pendingIntentCloseService = PendingIntent.getService(this, 0, intentCloseService, 0);

        builder.setSmallIcon(R.drawable.ic_recording)
                .setColor(Color.RED)
                .setContentTitle(getString(R.string.notification_recorder_title))
                .setContentText(getString(R.string.notification_recorder_description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(new NotificationCompat.Action(0, getString(R.string.stop_recording), pendingIntentCloseService))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_recorder_channel_name);
            String description = getString(R.string.notification_recorder_description);
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

    private void stopRecording() {
        mMyMediaRecorder.onRecord(false);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: RecorderService called");
        super.onDestroy();
        RECORDER_SERVICE_RUNNING = false;
        stopSelf();
    }
}
