package com.devandroid.tmsearch.RoomDatabase;

import android.arch.persistence.room.TypeConverter;

public class RoomTypeConverter {

    @TypeConverter
    public String stringListToString(String[] lstString) {

        String strResult = "";
        for(int i=0; i<lstString.length; i++) {
            strResult += lstString[i] + "|";
        }
        return strResult;
    }

    @TypeConverter
    public String[] stringToStringList(String str) {

        return str.split("|");
    }

}
