package com.example.learnservice;

import android.os.Handler;
import android.os.Message;

public class UIHandler extends Handler {

    private MainActivity mActivity;
    public static final String MSG = "msg";
    public String finalReaminTime = "00:00:00";
    public PowerKeyObserver mPowerKeyObserver;

    public UIHandler(MainActivity activity) {
        super();
        this.mActivity = activity;

    }

    @Override
    public void handleMessage(Message msg) {
        String text = msg.getData().getString(MSG);
        this.mActivity.setRemainTimeText(text);

        if (text.equals(finalReaminTime)) {      //時間倒數到00:00:00
            // this.mActivity.makeFinishToast();   //顯示時間到的訊息
            this.mActivity.showMsg();
        }
    }
}
