package com.ericwyn.seanote.activity;

import android.content.Intent;
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

                Intent intent = new Intent(StartActivity.this, InitActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1200);

    }
}
