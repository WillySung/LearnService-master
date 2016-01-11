package com.example.learnservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.lang.String;
import java.io.FileInputStream;


public class PowerKeyObserver {
    private Context mContext;
    private IntentFilter mIntentFilter;
    public OnPowerKeyListener mOnPowerKeyListener;
    private PowerKeyBroadcastReceiver mPowerKeyBroadcastReceiver;


    public PowerKeyObserver(Context context) {
        this.mContext = context;
    }

    //注册廣播接收者
    public void startListen() {
        mIntentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mPowerKeyBroadcastReceiver = new PowerKeyBroadcastReceiver();
        mContext.registerReceiver(mPowerKeyBroadcastReceiver, mIntentFilter);
        System.out.println("----> 開始監聽");
    }

    //取消廣播接收者
    public void stopListen() {
        if (mPowerKeyBroadcastReceiver != null) {
            mContext.unregisterReceiver(mPowerKeyBroadcastReceiver);
            System.out.println("----> 停止監聽");
        }
    }

    // 對外暴露接口
    public void setPowerKeyListener(OnPowerKeyListener powerKeyListener) {
        mOnPowerKeyListener = powerKeyListener;
    }

    // 回調接口
    public interface OnPowerKeyListener {
        public void onPowerKeyPressed();
    }

    //廣播接收者
    public class PowerKeyBroadcastReceiver extends BroadcastReceiver {
        public int starttime_got;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {      //讓倒數暫停
                mOnPowerKeyListener.onPowerKeyPressed();
                /*Calendar c2 = Calendar.getInstance();
                int endtime = (int)c2.getTimeInMillis()/1000;
                System.out.println("endtime"+endtime);
                int usedtime = endtime-starttime_got;
                System.out.println("usedtime="+usedtime+"秒");*/

                Intent i = new Intent();
                i.setClass(context, motherfucker.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            if (action.equals(Intent.ACTION_SCREEN_ON)) {  //讓倒數重新開始
                mOnPowerKeyListener.onPowerKeyPressed();
                /*Calendar c1 = Calendar.getInstance();
                int starttime = (int)c1.getTimeInMillis()/1000;
                System.out.println("starttime"+starttime);
                this.starttime_got = starttime;*/
            }
        }
    }

    public class motherfucker extends Activity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_main);

            Intent intent123 = new Intent(TimerService.ACTION_STOP);
            startService(intent123);
        }
    }
}