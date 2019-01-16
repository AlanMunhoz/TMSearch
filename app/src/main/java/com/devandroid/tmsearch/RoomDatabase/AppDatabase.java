package com.devandroid.tmsearch.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.devandroid.tmsearch.Model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters({RoomTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoriteMovies";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {

        if(sInstance==null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating a new database isntance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MoviesDAO MoviesDAO();

}
