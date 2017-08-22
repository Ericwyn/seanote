package com.ericwyn.seanote.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by ericwyn on 17-8-22.
 */

public class TimeUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static String transformCurrentTime(String currentTime){
//        return currentTime;
        try {
            return sdf.format(new Date(Long.parseLong(currentTime)));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            Log.d("TimeUtil___________",currentTime);
        }
        return currentTime.substring(0,6);
    }


}
