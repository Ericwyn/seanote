package com.ericwyn.seanote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.ericwyn.seanote.R;
import com.ericwyn.seanote.Util.NoteUtils;
import com.ericwyn.seanote.entity.Note;
import com.ericwyn.seanote.server.NoteServer;

import java.io.File;

public class EditActivity extends AppCompatActivity {
    private boolean firstCreateFlag=true;

    private boolean saveFlag=false;
    private Toolbar mToolbar;
    private EditText et_title;
    private EditText et_word;


    private String fileDir;

    private Note note;
    private String title_first="";
    private String words_first="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et_title=(EditText)findViewById(R.id.et_title_edit_act);
        et_word=(EditText) findViewById(R.id.et_word_edit_act);

        mToolbar=(Toolbar) findViewById(R.id.toolbar_edit_act);
        mToolbar.setTitle("笔记编辑");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.fancy_white));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.fancy_white));

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_save_edit_act){
                    saveNote();
                }
                return true;
            }
        });

        if(getIntent()!=null){
            Intent intent=getIntent();
            firstCreateFlag=intent.getBooleanExtra("create",false);
            if(!firstCreateFlag){
                //如果是从PreAct转跳而来，那么intent需要一个json化的note对象
                note= JSON.parseObject(intent.getStringExtra("obj"),Note.class);
                title_first=note.getTitle();
                words_first=note.getWords();
                et_title.setText(note.getTitle());
                et_word.setText(note.getWords());
            }else {
                //如果是新建的笔记，那么需要传入一个create 参数为boolean，传入dir参数，标记其文件夹（为未来的笔记列表做准备）
                note=new Note();
                long createTime= System.currentTimeMillis();
                //设置好id和createTime
                note.setId(""+createTime);
                fileDir=intent.getStringExtra("dir");
            }
        }

    }

    /**
     * 修改从前已经存在的笔记
     */
    private void updateNote(){
            Intent intent=new Intent();
            //有重命名的情况
            String oldPath=note.getFilePath();
            if(!NoteUtils.getNoteNameByFilePath(oldPath).equals(note.getTitle())){
                note.setFilePath(NoteUtils.getNewFilePath(oldPath,note.getTitle()));
            }
            intent.putExtra("obj",JSON.toJSON(note).toString());
            NoteServer.updateLocalNote(oldPath,note.getTitle(),note.getWords());
            this.setResult(RESULT_OK,intent);
    }

    /**
     * 保存一个全新的笔记
     *
     */
    private void  saveNewNote(){
        Intent intent=new Intent(EditActivity.this,PreviewActivity.class);
        intent.putExtra("obj",JSON.toJSON(note).toString());
        NoteServer.saveNewNote(note);
        this.setResult(RESULT_OK,intent);
    }


    private void saveNote(){
        if(firstCreateFlag){
            if(et_word.getText().toString().equals("") && et_title.getText().toString().equals("")){
                Intent intent=new Intent(EditActivity.this,PreviewActivity.class);
                this.setResult(RESULT_CANCELED,intent);
                finish();
            }else {
                note.setTitle(et_title.getText().toString());
                note.setWords(et_word.getText().toString());
                note.setFileName(note.getTitle()+"=-="+note.getId()+".md");
                note.setCreateTime(NoteUtils.getNoteCreateTime(note.getFileName()));
                File noteDir=new File(StartActivity.seanoDir,fileDir);
                note.setFilePath(new File(noteDir,note.getFileName()).getAbsolutePath());
                saveNewNote();
                finish();
            }
        }else {
            note.setTitle(et_title.getText().toString());
            note.setWords(et_word.getText().toString());
            updateNote();
            finish();
        }
    }

    /**
     * 退出前询问是否保存
     */
    public void showSaveDialog(){
            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
            //积极按钮
            alertDialogBuilder.setPositiveButton(R.string.edit_act_positive_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveNote();
                }
            });
            //消极按钮
            alertDialogBuilder.setNegativeButton(R.string.edit_act_negative_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setTitle("写入修改");
            alertDialog.setMessage("笔记已经过了修改，是否需要保存修改？");

            alertDialog.show();//将dialog显示出来
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_act_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!et_title.getText().toString().equals(title_first) || !et_word.getText().toString().equals(words_first)){
            showSaveDialog();
        }else {
            finish();
        }
    }
}
