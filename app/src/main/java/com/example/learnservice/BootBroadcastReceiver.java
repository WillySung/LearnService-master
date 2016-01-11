package com.example.learnservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent MainIntent = new Intent(context, MainActivity.class);
            MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(MainIntent);
        }
    }
}