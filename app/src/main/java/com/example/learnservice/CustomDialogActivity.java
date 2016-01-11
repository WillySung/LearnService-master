package com.example.learnservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.net.Uri;


public class CustomDialogActivity extends Activity {
    TextView text_content;
    TextView text_title;
    ArrayAdapter<String> VideoList;
    private String[] video = {"請選擇", "頸部伸展", "背部伸展", "手部伸展"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        text_title = (TextView) findViewById(R.id.text_title);
        text_content = (TextView) findViewById(R.id.text_content);

        //Spinner
        Spinner spinner = (Spinner) findViewById(R.id.MySpinner);
        VideoList = new ArrayAdapter<String>(CustomDialogActivity.this, android.R.layout.simple_spinner_item, video);
        VideoList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(VideoList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long arg3) {

                switch (video[position]) {
                    case "頸部伸展":
                        //Youtube
                        Intent intent1 = new Intent();
                        intent1.setAction(Intent.ACTION_VIEW);
                        intent1.setPackage("com.google.android.youtube");
                        intent1.setData(Uri.parse("https://www.youtube.com/watch?v=P3J-1iYP-hM"));
                        intent1.putExtra("force_fullscreen", true);
                        startActivity(intent1);
                        finish();
                        break;
                    case "背部伸展":
                        //Youtube
                        Intent intent2 = new Intent();
                        intent2.setAction(Intent.ACTION_VIEW);
                        intent2.setPackage("com.google.android.youtube");
                        intent2.setData(Uri.parse("https://www.youtube.com/watch?v=X_pgWmR9BqE"));
                        intent2.putExtra("force_fullscreen", true);
                        startActivity(intent2);
                        finish();
                        break;
                    case "手部伸展":
                        //Youtube
                        Intent intent3 = new Intent();
                        intent3.setAction(Intent.ACTION_VIEW);
                        intent3.setPackage("com.google.android.youtube");
                        intent3.setData(Uri.parse("https://www.youtube.com/watch?v=QKcTThzrEkQ"));
                        intent3.putExtra("force_fullscreen", true);
                        startActivity(intent3);
                        finish();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(CustomDialogActivity.this, "你根本沒選", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit() {//退出確認
        AlertDialog ad = new AlertDialog.Builder(CustomDialogActivity.this).create();
        ad.setTitle("離開");
        ad.setMessage("確定要放棄健康嗎?");

        ad.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                CustomDialogActivity.this.finish();//關閉activity
            }
        });
        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //不退出不用執行任何操作
            }
        });
        ad.setCanceledOnTouchOutside(false); // disable click home button and other area
        ad.setCancelable(false);  // disable click back button
        ad.show();//顯示對話框
    }


}