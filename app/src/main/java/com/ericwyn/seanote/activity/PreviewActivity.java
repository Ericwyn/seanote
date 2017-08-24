package com.ericwyn.seanote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.ericwyn.seanote.R;
import com.ericwyn.seanote.entity.Note;
import com.mittsu.markedview.MarkedView;


public class PreviewActivity extends AppCompatActivity {
    private String TAG="PreviewActivity";
    private MarkedView md_word;
    private Toolbar mToolbar;
    private Note note;
    public static final int REQUEST_CODE_EDIT_ACT=10086;
    public static final int REQUEST_CODE_FIRST_EDIT_ACT=10096;

    public static final int CREATE_NOTE_POSITION=10010;
    private String title="";

    //一个flag来标明这个act的生成是否是为了跳转，如果是为了跳转至EditAct（即是新建一个Note），那么这个flag始终为true
    private static boolean redirectFlag=false;

    private int positon=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mToolbar=(Toolbar) findViewById(R.id.toolbar_preAct);
        mToolbar.setTitle("预览");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.fancy_white));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.fancy_white));

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_edit_pre_act){
                    Intent intent=new Intent(PreviewActivity.this,EditActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("obj",JSON.toJSON(note).toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,REQUEST_CODE_EDIT_ACT);
                }
                return true;
            }
        });

        md_word=(MarkedView)findViewById(R.id.previewMdv_preAct);

        if(getIntent()!=null){
            Intent intent=getIntent();
            if(intent.getBooleanExtra("create",false)){
                redirectFlag=true;
                Intent intent2=new Intent(PreviewActivity.this,EditActivity.class);
                intent2.putExtra("create",true);
                intent2.putExtra("dir",intent.getStringExtra("dir"));
                startActivityForResult(intent2,REQUEST_CODE_FIRST_EDIT_ACT);
            }else {
                note= JSON.parseObject((String)intent.getStringExtra("obj"),Note.class);
                //如果是从EditAct 直接转跳过来，那么position就是10010，
                // 10010的情况下代表这时候的笔记预览是新建的笔记预览
                positon=intent.getIntExtra("position",CREATE_NOTE_POSITION);
                showMdText(md_word,note);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pre_act_menu, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_EDIT_ACT){
            if(resultCode==RESULT_OK){
                note=JSON.parseObject(data.getStringExtra("obj"),Note.class);
                showMdText(md_word,note);
            }
        }else if(requestCode == REQUEST_CODE_FIRST_EDIT_ACT){
            if(resultCode==RESULT_OK){
                note=JSON.parseObject(data.getStringExtra("obj"),Note.class);
                showMdText(md_word,note);
            }else if(resultCode == RESULT_CANCELED){
                Log.d(TAG,"编辑返回Main");
                Intent intent=new Intent();
                intent.putExtra("position",-1);
                setResult(RESULT_OK,intent);
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        backToMain();
    }

    public void showMdText(MarkedView mv, Note note){
        mv.setMDText("### **"+note.getTitle()+"**\n\n"+"---"+"\n\n"+note.getWords());
    }

    public void backToMain(){
        if(redirectFlag){
            Log.d(TAG,"redirect 后的返回Main");
            Intent intent=new Intent(PreviewActivity.this,MainActivity.class);
            intent.putExtra("obj",JSON.toJSON(note).toString());
            intent.putExtra("position",CREATE_NOTE_POSITION);
            setResult(RESULT_OK,intent);
            redirectFlag=false;
            finish();
        }else {
            Log.d(TAG,"编辑返回Main");
            Intent intent=new Intent();
            intent.putExtra("obj",JSON.toJSON(note).toString());
            intent.putExtra("position",positon);
            setResult(RESULT_OK,intent);
            finish();
        }

    }
}
