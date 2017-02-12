package com.kongzue.analogtouch;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout activityMain;
    private Button btnPosition;
    private TextView txtPosition;
    private Button btnStart;
    private Button btnStop;
    private EditText editTimes;
    private CheckBox chkRdm;

    private void assignViews() {
        activityMain = (LinearLayout) findViewById(R.id.activity_main);
        btnPosition = (Button) findViewById(R.id.btn_position);
        txtPosition = (TextView) findViewById(R.id.txt_position);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        editTimes = (EditText) findViewById(R.id.edit_times);
        chkRdm = (CheckBox) findViewById(R.id.chk_rdm);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();
        setEvent();
        initData();
    }

    private void initData() {
        if (Cache.x!=0 || Cache.y!=0){
            btnPosition.setText("重新定位");
            txtPosition.setText("x:"+Cache.x+";y:"+Cache.y);
            btnStart.setEnabled(true);
            btnStop.setEnabled(true);
        }
    }

    private void setEvent() {

        chkRdm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(">", "onCheckedChanged: "+b);
                Cache.isRdm=b;
            }
        });

        btnPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if(!Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    } else {
                        Intent intent = new Intent(MainActivity.this, PositionService.class);
                        Cache.intentGetPosition=intent;
                        startService(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, PositionService.class);
                    Cache.intentGetPosition=intent;
                    startService(intent);
                    finish();
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!editTimes.getText().toString().isEmpty()){
                    Cache.times=Integer.parseInt(editTimes.getText().toString());
                }else{
                    Cache.times=3;
                }

                if (Build.VERSION.SDK_INT >= 23) {
                    if(!Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    } else {
                        Intent intent = new Intent(MainActivity.this, FxService.class);
                        Cache.intentGetPosition=intent;
                        startService(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, FxService.class);
                    Cache.intentGetPosition=intent;
                    startService(intent);
                    finish();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FxService.class);
                stopService(intent);
            }
        });
    }
}
