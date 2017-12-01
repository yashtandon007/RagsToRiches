package com.infinityapps007.ragstoriches.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Magic Lenz on 01-Nov-17.
 */

public class Utili {

    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String toTitleCase(CharSequence givenString) {

        String[] arr = givenString.toString().split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static LinkedHashMap<String,Data> getReverserLinkedHashMap(LinkedHashMap<String, Data> hmap) {
        // getting keySet() into Set

        LinkedHashMap<String,Data> hmap_reversed = new LinkedHashMap<String,Data>();
        Set<String> set = hmap.keySet();

        // get Iterator from key set
        Iterator<String> itr = set.iterator();


        // convert to ArrayList of key set
        java.util.List<String> alKeys = new ArrayList<String>(hmap.keySet());

        // reverse order of keys
        Collections.reverse(alKeys);

        // iterate LHM using reverse order of keys
        for(String strKey : alKeys){
            hmap_reversed.put(strKey,hmap.get(strKey));

        }

        return hmap_reversed;
    }

}
