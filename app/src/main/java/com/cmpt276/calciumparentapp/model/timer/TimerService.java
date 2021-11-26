package com.cmpt276.calciumparentapp.model.timer;

import android.app.AlarmManager;
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

import androidx.annotation.Nullable;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.recievers.AlarmReceiver;

/**
 * Logic for the timer background service
 */
public class TimerService extends Service {

    /**
     * TimerService communicates with the activity using broadcasts
     * There are three different filters used for broadcasts
     *
     * TIME_BROADCAST_FILTER is the filter this service uses to
     *                       broadcast the time remaining on the timer
     *
     * TIMER_SERVICE_REQUEST_FILTER is the filter used to broadcast requests to this activity.
     *
     * TIMER_RUNNING_BROADCAST_FILTER is the filter this service uses to broadcast
     *                                if the timer is currently running
     */
    public static final String TIME_BROADCAST_FILTER = "TIME_BROADCAST_FILTER";
    public static final String TIMER_SERVICE_REQUEST_FILTER = "TIMER_SERVICE_REQUEST_FILTER";
    public static final String TIMER_RUNNING_BROADCAST_FILTER = "TIMER_RUNNING_BROADCAST_FILTER";

    /**
     * Keys broadcast by this activity
     *
     * TIME_REMAINING_BROADCAST:
     *      Filter: TIME_BROADCAST_FILTER
     *      Value: long representing the time remaining on the timer in ms
     *      Occurrence: Broadcast whenever the timer updates either by time passing or
     *                  when this service received a request to refresh the time
     *
     * TIMER_RUNNING_BROADCAST:
     *      Filter: TIMER_RUNNING_BROADCAST_FILTER
     *      Value: boolean indicating if the timer is running or not
     *      Occurrence: Broadcast after this service received a request to broadcast this
     */
    public static final String TIME_REMAINING_BROADCAST = "TIME_REMAINING";
    public static final String TIMER_RUNNING_BROADCAST = "TIMER_RUNNING_BROADCAST";

    /**
     * Keys received by this activity.
     * All of these keys use the TIMER_SERVICE_REQUEST_FILTER
     *
     * PAUSE_TIMER_INTENT:
     *      Value: A boolean. True indicates a request to pause the timer
     *
     * Resume_TIMER_INTENT:
     *      Value: A boolean. True indicates a request to resume the timer
     *
     * REFRESH_TIME_INTENT:
     *      Value: A boolean. True indicates a request for this service to update
     *             the timer notification and immediately broadcast the time remaining on the timer.
     *
     * TIMER_RUNNING_REQUEST_INTENT:
     *      Value: A boolean. True indicates a request for this service to broadcast
     *             weather or not the timer is currently running.
     */
    public static final String PAUSE_TIMER_INTENT = "PAUSE_TIMER_INTENT";
    public static final String RESUME_TIMER_INTENT = "RESUME_TIMER_INTENT";
    public static final String REFRESH_TIME_INTENT = "REFRESH_TIMER_SERVICE_TIME";
    public static final String TIMER_RUNNING_REQUEST_INTENT = "REQUEST_TIMER_RUNNING_INTENT";


    private final long TIMER_INTERVAL = 100;
    private TimerNotifications timerNotifications;
    private CountDownTimer timer;
    private NotificationManager notificationManager;
    private long timeRemaining;
    private boolean timerRunning = false;

    @Override
    public void onCreate() {
        setupBroadcastReceiver();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        timerNotifications = new TimerNotifications(getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
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
        startForeground(TimerNotifications.NOTIFICATION_ID, timerNotifications.getTimerNotification(length));
        return START_NOT_STICKY;
    }

    private void updateNotification(long newTime){
        Notification notification = timerNotifications.getTimerNotification(newTime);

        notificationManager.notify(TimerNotifications.NOTIFICATION_ID, notification);
    }

    private void setupTimer(long length){
        // update every second
        timer = new CountDownTimer(length, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                refreshTime();
            }

            @Override
            public void onFinish() {
                startAlarm();
                stopSelf();
            }
        }.start();

        timerRunning = true;
    }

    private void pauseTimer() {
        timer.cancel();
        timerNotifications.generatePauseNotification();
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

    public void startAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE);

        AlarmManager.AlarmClockInfo timeInfo = new AlarmManager.AlarmClockInfo(0, pendingIntent);

        alarmManager.setAlarmClock(timeInfo, pendingIntent);
    }

    private void setupBroadcastReceiver() {
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
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















