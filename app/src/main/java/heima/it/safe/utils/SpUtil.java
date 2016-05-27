package heima.it.safe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import heima.it.safe.constant.Constant;

/**
 * 作者：张琦 on 2016/5/18 19:49
 */
public class SpUtil {
    public static final String SP_URI = Constant.SP_URI;
    public static void putBoolean(Context context,String key,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(SP_URI,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,flag).apply();
    }

    public static boolean getBoolean(Context context,String key,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(SP_URI,Context.MODE_PRIVATE);
        return sp.getBoolean(key,flag);
    }
    public static void putString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(SP_URI,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }
    public static String getString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(SP_URI,Context.MODE_PRIVATE);
        return sp.getString(key,value);
    }

    public static void putInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(SP_URI,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }
    public static int getInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(SP_URI,Context.MODE_PRIVATE);
        return sp.getInt(key,value);
    }
}
