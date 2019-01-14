package com.devandroid.tmsearch.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Preferences {

    private static String PREFS_FILE = "prefs_file";
    private static String PREFS_TMDB_API_KEY = "prefs_tmdb_api_key";
    private static String PREFS_LIST = "prefs_list";

    public static void saveStringList(Context context, ArrayList<String> lstStrings) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> set = new HashSet<>(lstStrings);
        editor.putStringSet(PREFS_LIST, set);
        editor.commit();
    }

    public static ArrayList<String> restoreStringList(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        Set<String> set = sharedPreferences.getStringSet(PREFS_LIST, null);

        if(set==null)
            return new ArrayList<>();
        return new ArrayList<>(set);
    }

    public static void saveStringTmdbApiKey(Context context, String strApiKey) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREFS_TMDB_API_KEY, strApiKey);
        editor.commit();
    }

    public static String restoreStringTmdbApiKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        String strName = sharedPreferences.getString(PREFS_TMDB_API_KEY, "");

        return strName;
    }


}
