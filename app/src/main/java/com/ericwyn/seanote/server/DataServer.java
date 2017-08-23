package com.ericwyn.seanote.server;

import com.ericwyn.seanote.entity.Note;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static com.ericwyn.seanote.activity.StartActivity.seanoDir;

/**
 *
 * Created by ericwyn on 17-8-20.
 */

public class DataServer {


    public static List<Note> loadData(){
        List<Note> dataList=new ArrayList<>();
        Source source = null;
        BufferedSource bufferedSource = null;
        if(!seanoDir.isDirectory()){
            seanoDir.mkdir();
        }
        try {
            dataList =loadFileFromDir(seanoDir);
        }catch (IOException e){
            e.printStackTrace();
        }
        Collections.sort(dataList, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });

        return dataList;
    }

    public static String getWordShowInCard(String text){
        text=text.replaceAll("\n\n","\n");
        if(text.length()>150){
            return text.substring(0,150);
        }else{
            return text;
        }
    }

    private static List<Note> loadFileFromDir(File dir) throws IOException{
        List<Note> dataRes=new ArrayList<>();
        for (File file:dir.listFiles()){
            if(file.isDirectory()){
                dataRes.addAll(loadFileFromDir(file));
            }else {
                try {
                    Source source = null;
                    BufferedSource bufferedSource = null;
                    source = Okio.source(file);
                    bufferedSource = Okio.buffer(source);
                    Note note=new Note(file.getName(),bufferedSource.readUtf8());
                    note.setFilePath(file.getAbsolutePath());
                    dataRes.add(note);
                }catch (IOException e){
                    throw e;
                }
            }
        }
        return dataRes;
    }

}
