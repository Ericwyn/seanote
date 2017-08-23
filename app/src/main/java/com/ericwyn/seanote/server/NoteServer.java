package com.ericwyn.seanote.server;

import android.util.Log;

import com.ericwyn.seanote.Util.NoteUtils;
import com.ericwyn.seanote.entity.Note;

import java.io.File;
import java.nio.charset.Charset;

import okio.BufferedSink;
import okio.Okio;

/**
 *
 * Created by ericwyn on 17-8-20.
 */

public class NoteServer {
    public static void updateLocalNote(final String filePath, String title, final String word){
        Log.d("NoteServer","filePath:"+NoteUtils.getNoteNameByFilePath(filePath)+",title:"+title);
        if(NoteUtils.getNoteNameByFilePath(filePath).equals(title)){
            //不需要重命名
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedSink bufferSink=null;
                    try{
                        File file=new File(filePath);
                        if (!file.exists()){
                            file.createNewFile();
                        }
                        bufferSink= Okio.buffer(Okio.sink(file));
                        bufferSink.writeString(word, Charset.forName("utf-8"));
                        bufferSink.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.d("NoteServer","成功的更新了笔记,无需重命名");
                }
            }).start();
        }else {
            //需要重命名
            final String newFilePath=NoteUtils.getNewFilePath(filePath,title);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedSink bufferSink=null;
                    File fileOld=new File(filePath);
                    File file=new File(newFilePath);
                    try{
                        fileOld.renameTo(file);
                        bufferSink=Okio.buffer(Okio.sink(file));
                        bufferSink.writeString(word, Charset.forName("utf-8"));
                        bufferSink.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.d("NoteServer","成功的更新了笔记，重命名为"+file.getName());
                }
            }).start();
        }
    }

    public static void saveNewNote(final Note note){
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedSink bufferSink=null;
                File file=new File(note.getFilePath());
                try{
                    bufferSink=Okio.buffer(Okio.sink(file));
                    bufferSink.writeString(note.getWords(), Charset.forName("utf-8"));
                    bufferSink.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                Log.d("NoteServer","新建了笔记："+note.getFilePath());
            }
        }).start();

    }


}
