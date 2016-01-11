package com.example.learnservice;

import java.util.Calendar;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TimerService extends Service {

    private static UIHandler handler;
    public static final String ACTION_PLAY = "com.example.learnservice.action.PLAY";
    public static final String ACTION_STOP = "com.example.learnservice.action.STOP";
    public static final String TAG_TOTAL_SECOND = "TotoalSecond";


    enum State {
        Stopped,
        Running
    }

    ;

    static State mState = State.Stopped;

    public static String stringRemainTimer = "00:00:00";
    private static int remainSeconds;
    private int finalSeconds;
    public Boolean isRun = true;
    public int start_hour;
    public int start_minute;

    private static ServiceThread serviceThread;

    Notification mNotification = null;
    final int NOTIFICATION_ID = 77;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if (action.equals(ACTION_PLAY)) {
            remainSeconds = intent.getIntExtra(TimerService.TAG_TOTAL_SECOND, 0);
            isRun = true;
            startTimer();


        } else if (action.equals(ACTION_STOP)) {
            stopTimer();
        }
        return START_NOT_STICKY;

    }

    public void stopTimer() {
        // stop thread
        mState = State.Stopped;
        isRun = false;
        stopForeground(true);
    }

    public void startTimer() {
        Calendar c = Calendar.getInstance();
        int currentSeconds = (int) (c.getTimeInMillis() / 1000);
        finalSeconds = currentSeconds + remainSeconds;

        mState = State.Running;
        serviceThread = new ServiceThread(handler);
        new Thread(serviceThread).start();

        this.start_hour = c.get(Calendar.HOUR_OF_DAY);
        this.start_minute = c.get(Calendar.MINUTE);
        System.out.println("start time : " + this.start_hour + ":" + this.start_minute);
    }


    public static State getTimerState() {
        return mState;
    }

    public static void registerHandler(Handler uiHandler) {
        handler = (UIHandler) uiHandler;
    }

    public static void setRemainSeconds(int seconds) {
        remainSeconds = seconds;
    }

    public static int getRemainSeconds() {
        return remainSeconds;
    }

    public static String getStringRemainSeconds() {
        return stringRemainTimer;
    }

    public static UIHandler getUIHandler() {
        return handler;
    }

    public static void resetServiceThreadHandler() {
        serviceThread.threadHandler = handler;
    }

    public class ServiceThread implements Runnable {

        private UIHandler threadHandler;

        public ServiceThread(UIHandler handler) {
            super();
            this.threadHandler = handler;
        }

        @Override
        public void run() {

         /* 注册屏幕唤醒时的广播 */
            IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
            TimerService.this.registerReceiver(mScreenOReceiver, mScreenOnFilter);

            /* 注册机器锁屏时的广播 */
            IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
            TimerService.this.registerReceiver(mScreenOReceiver, mScreenOffFilter);

            while (isRun) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Calendar c = Calendar.getInstance();
                int currentSeconds = (int) (c.getTimeInMillis() / 1000);
                if (currentSeconds > finalSeconds) {
                    stopTimer();
                } else {
                    remainSeconds = finalSeconds - currentSeconds;
                    int hours = remainSeconds / 3600;
                    int mins = (remainSeconds % 3600) / 60;
                    int secs = remainSeconds % 60;

                    if (hours == 0) {
                        stringRemainTimer = "00:";
                    } else if (0 < hours && hours < 10) {
                        stringRemainTimer = "0" + Integer.toString(hours) + ":";
                    } else {
                        stringRemainTimer = Integer.toString(hours) + ":";
                    }

                    if (mins == 0) {
                        stringRemainTimer = stringRemainTimer + "00:";
                    } else if (0 < mins && mins < 10) {
                        stringRemainTimer = stringRemainTimer + "0" + Integer.toString(mins) + ":";
                    } else {
                        stringRemainTimer = stringRemainTimer + Integer.toString(mins) + ":";
                    }

                    if (secs == 0) {
                        stringRemainTimer = stringRemainTimer + "00";
                    } else if (0 < secs && secs < 10) {
                        stringRemainTimer = stringRemainTimer + "0" + Integer.toString(secs);
                    } else {
                        stringRemainTimer = stringRemainTimer + Integer.toString(secs);
                    }
                    setUpAsForeground(stringRemainTimer);
                    Message msg = this.threadHandler.obtainMessage();
                    msg.getData().putString(UIHandler.MSG, stringRemainTimer);
                    threadHandler.sendMessage(msg);
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    void setUpAsForeground(String text) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = R.drawable.ic_launcher;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.setLatestEventInfo(getApplicationContext(), "計時中",
                text, pi);
        startForeground(77, mNotification);

    }


    private BroadcastReceiver mScreenOReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("android.intent.action.SCREEN_ON")) {
                System.out.println("—— SCREEN_ON ——");

                Intent i = new Intent(context, MainActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                System.out.println("—— SCREEN_OFF ——");

                Calendar c = Calendar.getInstance();
                int end_hour = c.get(Calendar.HOUR_OF_DAY);
                int end_minute = c.get(Calendar.MINUTE);
                System.out.println("end time : " + end_hour + ":" + end_minute);

                if (end_minute < start_minute) {
                    end_hour--;
                    end_minute += 60;
                    if (end_hour < start_hour) {
                        end_hour += 24;
                    }
                    int used_hour = end_hour - start_hour;
                    int used_minute = end_minute - start_minute;
                    System.out.println("used  time = " + used_hour + "小時" + used_minute + "分鐘");
                    int input_time = (used_hour * 60 + used_minute);
                    try {
                        FileWriter fw = new FileWriter("/sdcard/output.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw); //將BufferedWriter與FileWriter物件做連結
                        bw.write(input_time + " min");
                        bw.newLine();
                        bw.close();
                        System.out.println("時間已存");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    int used_hour = end_hour - start_hour;
                    int used_minute = end_minute - start_minute;
                    System.out.println("used  time = " + used_hour + "小時" + used_minute + "分鐘");
                    int input_time = used_hour * 60 + used_minute;
                    try {
                        FileWriter fw = new FileWriter("/sdcard/output.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw); //將BufferedWriter與FileWriter物件做連結
                        bw.write(input_time + " min");
                        bw.newLine();
                        bw.close();
                        System.out.println("時間已存");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

}