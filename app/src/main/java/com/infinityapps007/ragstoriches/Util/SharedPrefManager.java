package com.infinityapps007.ragstoriches.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.infinityapps007.ragstoriches.R;

/**
 * Created by Magic Lenz on 01-Nov-17.
 */

public class SharedPrefManager {


    private static final String SHARED_PREF_NAME = "ragstoriches";

    public static boolean setThemeValue(boolean token, Context mCtx){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(mCtx.getResources().getString(R.string.key_night_mode),token);
        editor.apply();
        return true;
    }

    public static boolean set_Disk_Cache_Value(boolean token, Context mCtx){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(mCtx.getResources().getString(R.string.key_disk_cache),token);
        editor.apply();
        return true;
    }

    public static boolean setAdsFreeValue(int token, Context mCtx){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("infinityapps007_r2r_adsfree",token);
        editor.apply();
        return true;
    }


    public static int   getAdsFreeValue(Context mCtx){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("infinityapps007_r2r_adsfree",0);
    }

    public static boolean   getThemeValue(Context mCtx){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(mCtx.getResources().getString(R.string.key_night_mode),false);
    }

    public static boolean   get_Disk_Cahce_Value(Context mCtx){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(mCtx.getResources().getString(R.string.key_disk_cache),false);
    }

    public static boolean adsfree(Context context) {
        if(SharedPrefManager.getAdsFreeValue(context.getApplicationContext())==1){
            return true;
        }
        return false;
    }


}
