package com.ericwyn.seanote.server;

import android.os.Environment;

import com.ericwyn.seanote.Util.FileUtil;
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
            for (File file:seanoDir.listFiles()){
                source = Okio.source(file);
                bufferedSource = Okio.buffer(source);
                Note note=new Note(file.getName(),bufferedSource.readUtf8());
                note.setFilePath(file.getAbsolutePath());
                dataList.add(note);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return dataList;
    }

    /**
     * 创建测试文件的方法
     */
    private static void createTestFile(){
        File seanoDir=new File(Environment.getExternalStorageDirectory(),"seanote");
        File[] files=seanoDir.listFiles();
        boolean flag=true;
        for (File file:files){
            if(file.getName().startsWith("testFile")){
                flag=false;
                break;
            }
        }
        if(flag){
            File file=new File(seanoDir, FileUtil.getFileName("testFile"));
        }
    }

    public static String getWordShowInCard(String text){
        text=text.replaceAll("\n\n","\n");
        if(text.length()>250){
            return text.substring(0,250);
        }else{
            return text;
        }
    }


}
