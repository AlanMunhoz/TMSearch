package com.devandroid.tmsearch.RoomDatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.devandroid.tmsearch.Model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public static final String LOG_TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> mLstMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        Log.d(LOG_TAG, "loadFavorites from DB");
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        mLstMovies = database.MoviesDAO().loadMovies();
    }

    public LiveData<List<Movie>> getFavoriteEntries() {
        return mLstMovies;
    }
}
