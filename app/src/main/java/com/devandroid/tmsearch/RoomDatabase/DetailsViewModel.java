package com.devandroid.tmsearch.RoomDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.devandroid.tmsearch.Model.Movie;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    public static final String LOG_TAG = DetailsViewModel.class.getSimpleName();

    private LiveData<List<Movie>> mMovie;

    public DetailsViewModel(AppDatabase database, String movieId) {
        Log.d(LOG_TAG, "searchTitle DB");
        mMovie = database.MoviesDAO().searchTitle(movieId);
    }

    public LiveData<List<Movie>> getFavoriteEntry() {
        return mMovie;
    }
}
