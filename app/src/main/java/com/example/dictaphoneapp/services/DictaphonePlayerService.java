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

import static com.example.dictaphoneapp.services.DictaphoneRecorderService.ACTION_CLOSE;


public class DictaphonePlayerService extends Service {


    public static final String CHANNEL_ID = "Playing";
    public static final String TAG = "DictaphonePlayerService";
    public static final String FILE_LIST_ADAPTER_FILE_PATH = "FileListAdapterFilePath";
    private static final int NOTIFICATION_ID = 103;
    public static boolean PLAYER_SERVICE_RUNNING;
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

        if (ACTION_CLOSE.equals(intent.getAction())) {
            stopSelf();
            
        } else {
            String path = intent.getExtras().getString(FILE_LIST_ADAPTER_FILE_PATH);
            startPlaying(path);
            startForeground(NOTIFICATION_ID, createNotification());
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

        Intent intentCloseService = new Intent(this, DictaphonePlayerService.class);
        intentCloseService.setAction(ACTION_CLOSE);
        PendingIntent pendingIntentCloseService = PendingIntent.getService(this, 0, intentCloseService, 0);

        builder.setSmallIcon(R.drawable.ic_playing)
                .setColor(Color.GREEN)
                .setContentTitle(getString(R.string.notification_player_title))
                .setContentText(getString(R.string.notification_player_description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(new NotificationCompat.Action(R.drawable.ic_stop, getString(R.string.stop_playing), pendingIntentCloseService))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }

    private void stopPlaying(String path) {

        mMyMediaRecorder.onPlay(false, path);

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
