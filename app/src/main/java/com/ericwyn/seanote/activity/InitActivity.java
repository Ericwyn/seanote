package com.ericwyn.seanote.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ericwyn.seanote.R;
import com.ericwyn.seanote.adapter.InitActVpAdapter;
import com.ericwyn.seanote.server.SeafileServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InitActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Button btn_checkServer;
    private TextInputLayout til_server_url;

    private TextView tv_serverurl;
    private Button btn_checkAccount;
    private TextInputLayout til_account;
    private TextInputLayout til_pw;
    private TextView tv_checkAcRes_InitAct;

    private TextView tv_seafile_version;
//    private TextView tv_seafile_feature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        mViewPager=(ViewPager)findViewById(R.id.initViewPager_initAct);

        LayoutInflater lf=LayoutInflater.from(InitActivity.this);
        View view1=lf.inflate(R.layout.init_layout_1,null);
        View view2=lf.inflate(R.layout.init_layout_2,null);

        List<View> viewContainter  = new ArrayList<View>();

        viewContainter.add(view1);
        viewContainter.add(view2);
        mViewPager.setAdapter(new InitActVpAdapter(viewContainter));


        til_server_url =(TextInputLayout)viewContainter.get(0).findViewById(R.id.til_serverurl_InitAct);
        btn_checkServer =(Button)viewContainter.get(0).findViewById(R.id.bt_checkServerUrl_InitVP);
        tv_serverurl=(TextView)viewContainter.get(0).findViewById(R.id.tv_serverurl);

        tv_seafile_version=(TextView)viewContainter.get(1).findViewById(R.id.tv_seafile_v_IniAct);
//        tv_seafile_feature=(TextView)viewContainter.get(1).findViewById(R.id.tv_seafile_f_IniAct);
        btn_checkAccount=(Button)viewContainter.get(1).findViewById(R.id.bt_checkAccount_InitVP);
        til_account=(TextInputLayout)viewContainter.get(1).findViewById(R.id.til_account_InitAct);
        til_pw=(TextInputLayout)viewContainter.get(1).findViewById(R.id.til_pw_InitAct);
        tv_checkAcRes_InitAct=(TextView)viewContainter.get(1).findViewById(R.id.tv_checkAcRes_InitAct);

        btn_checkServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SeafileServer.checkServer(til_server_url.getEditText().getText().toString(),new Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_serverurl.setTextColor(getResources().getColor(R.color.fancy_accent));
                                    tv_serverurl.setText("服务器错误");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    JSONObject jsonObject= JSONObject.parseObject(response.body().toString());
//                                    JSONObject jsonObject1 =jsonObject.getJSONObject("features");
//                                    tv_seafile_version.setText("Seafile 服务器版本:"+jsonObject.getString("version"));
                                    SeafileServer.saveServerUrl(til_server_url.getEditText().getText().toString());
                                    tv_seafile_version.setText("服务器地址  :  "+SeafileServer.getServer_url());
                                    mViewPager.setCurrentItem(1);
                                }
                            });
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_checkAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeafileServer.getUserToken(til_account.getEditText().getText().toString(),
                        til_pw.getEditText().getText().toString(),
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_checkAcRes_InitAct.setTextColor(getResources().getColor(R.color.fancy_accent));
                                        tv_checkAcRes_InitAct.setText("用户验证失败");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_checkAcRes_InitAct.setTextColor(getResources().getColor(R.color.fancy_accent));
                                        String token="";
                                        try {
                                            token= JSON.parseObject(response.body().string()).getString("token");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if(token.equals("null")){
                                            tv_checkAcRes_InitAct.setTextColor(getResources().getColor(R.color.fancy_accent));
                                            tv_checkAcRes_InitAct.setText("用户验证失败");
                                        }else {
                                            tv_checkAcRes_InitAct.setTextColor(getResources().getColor(R.color.fancy_dark_black));
                                            tv_checkAcRes_InitAct.setText("登录成功，token ："+token);
                                        }
                                    }
                                });
                            }
                        });
            }
        });


        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
}
