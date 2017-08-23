package com.ericwyn.seanote.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.ericwyn.seanote.R;
import com.ericwyn.seanote.server.SeafileServer;

import java.io.File;

public class StartActivity extends hei.permission.PermissionActivity {

    public static File seanoDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                seanoDir = new File(Environment.getExternalStorageDirectory(),"seanote");
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
                        },R.string.per_storage,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
