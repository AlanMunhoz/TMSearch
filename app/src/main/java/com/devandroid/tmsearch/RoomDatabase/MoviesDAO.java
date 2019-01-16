package com.devandroid.tmsearch.RoomDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.devandroid.tmsearch.Model.Movie;

import java.util.List;

@Dao
public interface MoviesDAO {

    @Query("SELECT * FROM Favorite")
    LiveData<List<Movie>> loadMovies();

    @Query("SELECT * FROM Favorite WHERE mStrId = :movieId")
    LiveData<List<Movie>> searchTitle(String movieId);

    @Insert
    void insertMovie(Movie favoriteEntry);

    @Delete
    void deleteMovie(Movie favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie favoriteEntry);

}
