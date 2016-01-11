package com.example.learnservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Jack on 2015/12/28.
 */
public class BaseActivity extends Activity {

    private Context mContext;
    private IntentFilter mIntentFilter;
    private PowerKeyObserver.PowerKeyBroadcastReceiver mPowerKeyBroadcastReceiver;
    private PowerKeyObserver.OnPowerKeyListener mOnPowerKeyListener;
    private PowerKeyObserver mPowerKeyObserver;


    private static boolean isRunInBackground;

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        isRunInBackground = false;
        super.onStart();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        isRunInBackground = true;
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (isRunInBackground) {
            System.out.println("背景執行中");


        }
        super.onStop();
    }


}

