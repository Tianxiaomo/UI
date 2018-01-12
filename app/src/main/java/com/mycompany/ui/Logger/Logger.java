package com.mycompany.ui.Logger;

import android.util.Log;

/**
 * Created by qkz on 2018/1/11.
 */

public class Logger {
    public static int LOG_LEVEL = 6;
    public static int ERROR = 1;
    public static int WARN = 2;
    public static int INFO = 3;
    public static int DEBUG = 4;
    public static int VERBOS = 5;

    private static String Tag = null;

    public void setTag(String Tag){
        this.Tag = Tag;
    }

    public static void e(String msg){
        if(LOG_LEVEL>ERROR)
            Log.e(Tag, msg);
    }

    public static void w(String msg){
        if(LOG_LEVEL>WARN)
            Log.w(Tag, msg);
    }
    public static void i(String msg){
        if(LOG_LEVEL>INFO)
            Log.i(Tag, msg);
    }
    public static void d(String msg){
        if(LOG_LEVEL>DEBUG)
            Log.d(Tag, msg);
    }
    public static void v(String msg){
        if(LOG_LEVEL>VERBOS)
            Log.v(Tag, msg);
    }

    public static void e(String tag,String msg){
        if(LOG_LEVEL>ERROR)
            Log.e(tag, msg);
    }

    public static void w(String tag,String msg){
        if(LOG_LEVEL>WARN)
            Log.w(tag, msg);
    }
    public static void i(String tag,String msg){
        if(LOG_LEVEL>INFO)
            Log.i(tag, msg);
    }
    public static void d(String tag,String msg){
        if(LOG_LEVEL>DEBUG)
            Log.d(tag, msg);
    }
    public static void v(String tag,String msg){
        if(LOG_LEVEL>VERBOS)
            Log.v(tag, msg);
    }
}
