package com.ericwyn.seanote.Util;

/**
 *
 * Created by ericwyn on 17-8-22.
 */

public class FileUtil {
    public static String getFileName(String noteTitle){
        return noteTitle+"=-="+System.currentTimeMillis()+".md";
    }

    public static String getNoteName(String fileName){
        String[] temp=fileName.split("=-=");
        return temp[0];
    }

    public static String getNoteCreateTime(String fileName){
        String[] temp=fileName.split("=-=");
        return TimeUtil.transformCurrentTime(temp[1].replace(".md",""));
    }

    public static String getNoteFileId(String fileName){
        String[] temp=fileName.split("=-=");
        return temp[1].substring(0,temp[1].length()-3);
    }

}
