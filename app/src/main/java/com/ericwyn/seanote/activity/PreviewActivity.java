package com.ericwyn.seanote.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ericwyn.seanote.R;

import us.feras.mdv.MarkdownView;

public class PreviewActivity extends AppCompatActivity {
    private TextView tv_title;
    private MarkdownView md_word;
    private String title="";
    private String word="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(R.string.pre_act_toolbar_title);
        }
        if(getIntent()!=null){
            Intent intent=getIntent();
            title=intent.getStringExtra("title");
            word=intent.getStringExtra("word");
        }

        tv_title=(TextView)findViewById(R.id.previewTitle_preAct);
        md_word=(MarkdownView)findViewById(R.id.previewMdv_preAct);
        md_word.getSettings().setSupportZoom(true);
        md_word.getSettings().setBuiltInZoomControls(true);
        md_word.getSettings().setDisplayZoomControls(false);
        tv_title.setText(title);
        md_word.loadMarkdown(word);

    }
}
