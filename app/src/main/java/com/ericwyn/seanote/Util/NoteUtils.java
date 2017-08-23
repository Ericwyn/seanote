package com.ericwyn.seanote.Util;

/**
 *
 * Created by ericwyn on 17-8-22.
 */

public class NoteUtils {
    public static String getFileName(String noteTitle){
        return noteTitle+"=-="+System.currentTimeMillis()+".md";
    }

    public static String getNoteNameByFilePath(String filePath){
        String[] temp=filePath.split("/");
        return getNoteNameByFileName(temp[temp.length-1]);
    }
    public static String getNoteNameByFileName(String fileName){
        String[] temp=fileName.split("=-=");
        return temp[0].replace(".md","");
    }

    public static String getNoteCreateTime(String fileName){
        String[] temp=fileName.split("=-=");
        return TimeUtil.transformCurrentTime(temp[1].replace(".md",""));
    }

    public static String getNoteFileId(String fileName){
        String[] temp=fileName.split("=-=");
        return temp[1].substring(0,temp[1].length()-3);
    }

    public static String getNoteDir(String filePath){
        String[] temp=filePath.split("/");
        return filePath.replace(temp[temp.length-1],"");
    }
    public static String getNewFileName(String filePath,String newNoteName){
        return newNoteName+"=-="+getNoteFileId(filePath)+".md";
    }

    public static String getNewFilePath(String filePath,String newNoteName){
        return getNoteDir(filePath)+"/"+newNoteName+"=-="+getNoteFileId(filePath)+".md";
    }
}
