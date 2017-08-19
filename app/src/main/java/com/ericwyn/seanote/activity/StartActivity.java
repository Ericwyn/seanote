package com.ericwyn.seanote.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ericwyn.seanote.R;

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
                if(isLogin()){
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

    /**
     * initdata 当中含有 server url，username，password，和用户对应的token,
     * @return
     */
    public boolean isLogin(){
        SharedPreferences initdate = getSharedPreferences("initdata", MODE_PRIVATE);
        if(initdate.getString("token","null").equals("null") ){
            return false;
        }else {
            return true;
        }
    }

}
