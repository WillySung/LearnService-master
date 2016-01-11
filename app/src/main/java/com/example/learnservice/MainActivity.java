package com.example.learnservice;

import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnservice.PowerKeyObserver.OnPowerKeyListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    private TextView textTimer;
    public Context mContext;                          //跳出toast訊息的參數
    public UIHandler mUIHandler;
    private PowerKeyObserver mPowerKeyObserver;
    public int Chosentime;
    private Button showUsedtime;
    private Button clearTxt;


    public void ChooseTimeDialog() {

        final String[] time = {"10分鐘", "20分鐘", "30分鐘", "40分鐘", "50分鐘", "60分鐘"};

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
        dialog_list.setTitle("選段時間吧~");
        dialog_list.setItems(time, new DialogInterface.OnClickListener() {
            @Override
            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "你選的是" + time[which], Toast.LENGTH_SHORT).show();
                if (time[which] == "10分鐘") {
                    Chosentime = 10;
                    showTimerDialog();
                } else if (time[which] == "20分鐘") {
                    Chosentime = 20;
                    showTimerDialog();
                } else if (time[which] == "30分鐘") {
                    Chosentime = 30;
                    showTimerDialog();
                } else if (time[which] == "40分鐘") {
                    Chosentime = 40;
                    showTimerDialog();
                } else if (time[which] == "50分鐘") {
                    Chosentime = 50;
                    showTimerDialog();
                } else if (time[which] == "60分鐘") {
                    Chosentime = 60;
                    showTimerDialog();
                } else {

                }
            }
        });
        AlertDialog mdialog_list = dialog_list.create();
        mdialog_list.setCanceledOnTouchOutside(false); // disable click home button and other area
        dialog_list.setCancelable(false);  // disable click back button
        dialog_list.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        textTimer = (TextView) findViewById(R.id.text_timer);              //顯示時間倒數的textview
        mContext = this;

        ChooseTimeDialog();

        showUsedtime = (Button) findViewById(R.id.btn_read_txt);
        showUsedtime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    FileReader fr = new FileReader("/sdcard/output.txt");       //建立FileReader物件，並設定讀取的檔案為SD卡中的output.txt檔案
                    BufferedReader br = new BufferedReader(fr);       //將BufferedReader與FileReader做連結
                    String line, tempstring;
                    String[] tempArray = new String[2];
                    ArrayList myList = new ArrayList();
                    while ((line = br.readLine()) != null) {
                        //br.readLine()是指讀取txt檔的每一行資料,把讀到的資料存到line
                        //再將line丟給tempstring去儲存
                        tempstring = line;

                        //因為我這個test檔的資料格式是-->一行有3個字串，用兩個空白鍵隔開，
                        //tempstring.split("\\s") 會依照空白鍵來切割,剛好切三個,所以這邊我的tempArray的大小才會宣告3
                        tempArray = tempstring.split("\\s");

                        //這邊就是按照順序,一行一行的儲存到動態陣列裡面
                        for (int i = 0; i < tempArray.length; i++) {
                            myList.add(tempArray[i]);
                        }

                    }
                    //這邊的除2,和矩陣的需告大小,其實就是上面講的 "tempArray.length"這個值來做決定的
                    //y小於2,也是從"tempArray.length"的概念來的
                    int k = myList.size() / 2;
                    int count = 0;
                    int readData_sum = 0;
                    String[][] trans_array = new String[k][2];

                    for (int x = 0; x < myList.size() / 2; x++) {
                        for (int y = 0; y < 2; y++) {
                            trans_array[x][y] = (String) myList.get(count);
                            count++; //一個index來決定myList讀取值的位置
                        }
                        readData_sum += Integer.parseInt(trans_array[x][0]);
                    }
                    //這邊我用的發法是二維陣列來存取的，也可以用一維陣列存取
                    //到這邊為止，我們已經成功的將文字檔的資料存放到一個String二維陣列上了

                    int total_hour = readData_sum / 60;
                    int total_min = readData_sum % 60;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("今日使用時間");
                    dialog.setMessage(total_hour + "小時" + total_min + "分鐘");
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        clearTxt = (Button) findViewById(R.id.btn_erase_txt);
        clearTxt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    FileWriter fw = new FileWriter("/sdcard/output.txt", false);
                    BufferedWriter bw = new BufferedWriter(fw); //將BufferedWriter與FileWriter物件做連結
                    bw.write("");
                    bw.close();
                    Toast.makeText(MainActivity.this, "時間記錄已清除", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setRemainTimeText(String text) {   //設定textTimer這個textview所顯示的內容，UIHandler.java有call到
        textTimer.setText(text);
    }

    public void showMsg() {
        Intent intent = new Intent(MainActivity.this, CustomDialogActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if (TimerService.getTimerState().equals(TimerService.State.Running)) {
            textTimer.setText(TimerService.getStringRemainSeconds());

            mUIHandler = new UIHandler(MainActivity.this);
            TimerService.registerHandler(mUIHandler);
            TimerService.resetServiceThreadHandler();

        } else {
            mUIHandler = new UIHandler(MainActivity.this);
            TimerService.registerHandler(mUIHandler);
            textTimer.setText(TimerService.getStringRemainSeconds());
        }
    }

    public void init() {
        mPowerKeyObserver = new PowerKeyObserver(this);
        mPowerKeyObserver.setPowerKeyListener(new OnPowerKeyListener() {
            @Override
            public void onPowerKeyPressed() {
                System.out.println("----> 按下電源鍵");
            }
        });
        mPowerKeyObserver.startListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPowerKeyObserver.stopListen();
    }

    //更改時間之處
    public void showTimerDialog() {
        // TODO Auto-generated method stub

        String stringTimer = "00:00:00";
        int intHour = 0;
        int intMin = Chosentime;
        int intSec = 0;
        textTimer.setText(stringTimer);
        int totalSec = intHour * 3600 + intMin * 60 + intSec;
        TimerService.setRemainSeconds(totalSec);

        Intent intent = new Intent(TimerService.ACTION_PLAY);
        intent.putExtra(TimerService.TAG_TOTAL_SECOND, TimerService.getRemainSeconds());
        startService(intent);
    }
}
