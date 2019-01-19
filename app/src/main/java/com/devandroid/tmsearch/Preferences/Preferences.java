package com.devandroid.tmsearch.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Preferences {

    private static String PREFS_FILE = "prefs_file";
    private static String PREFS_TMDB_API_KEY = "prefs_tmdb_api_key";
    private static String PREFS_WIDGET_LIST = "prefs_list";
    private static String PREFS_WIDGET_TITLE = "prefs_movie_name";

    public static void saveStringList(Context context, ArrayList<String> lstStrings) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> set = new HashSet<>(lstStrings);
        editor.putStringSet(PREFS_WIDGET_LIST, set);
        editor.commit();
    }

    public static ArrayList<String> restoreStringList(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        Set<String> set = sharedPreferences.getStringSet(PREFS_WIDGET_LIST, null);

        if(set==null) {
            return new ArrayList<>();
        }

        /**
         * handling Set<String> to String[] ordered
         */
        int i = 0;
        String[] lstOrdered = new String[set.size()];
        for (String s: set) { lstOrdered[i++] = s; }
        Arrays.sort(lstOrdered, Collections.reverseOrder());

        /**
         * converting String[] to ArrayList<String>
         */
        ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(lstOrdered));

        return new ArrayList<String>(stringList);
    }

    public static void saveStringMovie(Context context, String strMovie) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREFS_WIDGET_TITLE, strMovie);
        editor.commit();
    }

    public static String restoreStringMovie(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        String strName = sharedPreferences.getString(PREFS_WIDGET_TITLE, "");

        return strName;
    }

}
