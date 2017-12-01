package com.infinityapps007.ragstoriches;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by Magic Lenz on 7/27/2017.
 */

public class MyApplication extends Application {

    private static AdRequest adRequest;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static AdRequest getAdRequest()
    {
        if(adRequest==null){
            adRequest = new AdRequest.Builder()
                 //   .addTestDevice("D47D5133A7BE8486DE3403D4FE8EDE44")
                    .build();
        }
        return adRequest;
    }

}
