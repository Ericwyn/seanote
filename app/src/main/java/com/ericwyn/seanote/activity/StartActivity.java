package com.ericwyn.seanote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ericwyn.seanote.R;
import com.ericwyn.seanote.server.SeafileServer;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //findInitData 在使用的时候会判断sp是否已经存储了token，并初始化SeafileServer 里面各个static 全局变量
                if(SeafileServer.findInitData(StartActivity.this)){
                    Intent intent=new Intent(StartActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(StartActivity.this, InitActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1200);

    }

}
