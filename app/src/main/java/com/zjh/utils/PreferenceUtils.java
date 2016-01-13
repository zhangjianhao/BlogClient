package com.zjh.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 张建浩 on 2015/10/26.
 */
public class PreferenceUtils {
    public static void rememberOpenTime(Context context,int fragmenttype,int newtype){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("TIME",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        long currentTime = System.currentTimeMillis();
        editor.putLong("time"+fragmenttype+"_"+newtype,currentTime);
        System.out.println("prfrence this remem time"+currentTime);
        editor.commit();

    }
    public static long getRememberdTime(Context context,int fragmenttype,int newstype){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("TIME",Context.MODE_PRIVATE);
        long past = preferences.getLong("time"+fragmenttype+"_"+newstype,0);
        System.out.println("past"+past);
        return past;
    }
}
