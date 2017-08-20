package com.ericwyn.seanote.server;

import android.os.Environment;

import com.ericwyn.seanote.entity.Note;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/**
 *
 * Created by ericwyn on 17-8-20.
 */

public class DataServer {


    public static List<Note> loadData(){
        List<Note> dataList=new ArrayList<>();
        Source source = null;
        BufferedSource bufferedSource = null;

        File seanoDir=new File(Environment.getExternalStorageDirectory(),"seanote");
        if(!seanoDir.isDirectory()){
            seanoDir.mkdir();
        }
        try {

            File file = new File(seanoDir,"testFile1.md");
            source = Okio.source(file);
            bufferedSource = Okio.buffer(source);

        } catch (IOException e){
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 创建测试文件的方法
     */
    private void createTestFile(){
        File seanoDir=new File(Environment.getExternalStorageDirectory(),"seanote");
        File[] files=seanoDir.listFiles();

        for (File file:files){

        }
    }


}
