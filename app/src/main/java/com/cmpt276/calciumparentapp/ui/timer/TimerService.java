package com.cmpt276.calciumparentapp.ui.timer;

import static com.cmpt276.calciumparentapp.ui.timer.Timer.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

public class TimerService extends Service {
    // Key used when this service broadcasts the current time remaining
    public static final String TIME_BROADCAST_FILTER = "TIME_BROADCAST_FILTER";

    // Key used to broadcast requests to this service
    public static final String TIMER_SERVICE_REQUEST_FILTER = "TIMER_SERVICE_REQUEST_FILTER";

    // The filter used to broadcast if the timer is running
    public static final String TIMER_RUNNING_BROADCAST_FILTER = "TIMER_RUNNING_BROADCAST_FILTER";

    public static final String TIME_REMAINING_BROADCAST = "TIME_REMAINING";
    public static final String PAUSE_TIMER_INTENT = "PAUSE_TIMER_INTENT";
    public static final String RESUME_TIMER_INTENT = "RESUME_TIMER_INTENT";
    public static final String REFRESH_TIME_INTENT = "REFRESH_TIMER_SERVICE_TIME";
    public static final String TIMER_RUNNING_REQUEST_INTENT = "REQUEST_TIMER_RUNNING_INTENT";
    public static final String TIMER_RUNNING_BROADCAST = "TIMER_RUNNING_BROADCAST";
    public static final int NOTIFICATION_ID = 1;

    private TimerLogic timerLogic;
    private CountDownTimer timer;
    private NotificationManager notificationManager;
    private long timeRemaining;
    private boolean timerRunning = false;

    @Override
    public void onCreate() {
        timerLogic = TimerLogic.getInstance();
        setupBroadcastReceiver();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("Service", "service onDestroy called!");
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long length = intent.getLongExtra(getString(R.string.timer_length_extra), -1);
        if(length == -1) {
            throw new IllegalStateException("Timer service started without providing a length");
        }

        setupTimer(length);
        startForeground(NOTIFICATION_ID, getTimerNotification(length));
        return START_NOT_STICKY;
    }

    private PendingIntent getNotificationPendingIntent() {
        Intent notificationIntent = new Intent(this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return pendingIntent;
    }

    private Notification getTimerNotification(long timeRemaining){

        PendingIntent pendingIntent = getNotificationPendingIntent();

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.timer_notification_title))
                .setContentText(timerLogic.getTimerText(timeRemaining))
                .setSmallIcon(R.drawable.timer_service_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
    }

    private void updateNotification(long newTime){
        Notification notification = getTimerNotification(newTime);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void generatePauseNotification() {
        PendingIntent pendingIntent = getNotificationPendingIntent();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.timer_notification_title))
                .setContentText("Timer Paused")
                .setSmallIcon(R.drawable.timer_service_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void setupTimer(long length){
        // update every second
        timer = new CountDownTimer(length, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                refreshTime();
            }

            @Override
            public void onFinish() {
                stopSelf();
            }
        }.start();

        timerRunning = true;
    }

    private void pauseTimer() {
        timer.cancel();
        generatePauseNotification();
        timerRunning = false;
    }

    private void resumeTimer() {
        setupTimer(timeRemaining);
    }

    private void refreshTime() {
        updateNotification(timeRemaining);
        talk(timeRemaining);
    }

    private void broadcastCurrentState() {
        Intent i = new Intent();

        i.putExtra(TIMER_RUNNING_BROADCAST, timerRunning);
        i.setAction(TIMER_RUNNING_BROADCAST_FILTER);
        sendBroadcast(i);
    }

    public void talk(long ms) {
        Intent i = new Intent();
        i.putExtra(TIME_REMAINING_BROADCAST, ms);
        i.setAction(TIME_BROADCAST_FILTER);
        sendBroadcast(i);
    }

    private void setupBroadcastReceiver() {
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("Timer service", "Broadcast received!!");
                if(intent.getBooleanExtra(PAUSE_TIMER_INTENT, false)){
                    pauseTimer();
                }

                if(intent.getBooleanExtra(REFRESH_TIME_INTENT, false)){
                    refreshTime();
                }

                if(intent.getBooleanExtra(RESUME_TIMER_INTENT, false)){
                    resumeTimer();
                }

                if(intent.getBooleanExtra(TIMER_RUNNING_REQUEST_INTENT, false)){
                    broadcastCurrentState();
                }
            }
        };

        IntentFilter filter = new IntentFilter(TIMER_SERVICE_REQUEST_FILTER);
        this.registerReceiver(br, filter);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}















