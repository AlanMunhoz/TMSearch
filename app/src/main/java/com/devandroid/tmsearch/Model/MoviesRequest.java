package com.devandroid.tmsearch.Model;

import org.parceler.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Parcel use
 * (1) Annotate the class with the @Parcel decorator.
 * (2) Use only public fields (private fields cannot be detected during annotation) that need to be serialized.
 * (3) Create a public constructor with no arguments for the annotation library.
 */
@Parcel
public class MoviesRequest {

    @SerializedName("page")
    public String mStrPage;

    @SerializedName("total_results")
    public String mStrTotalResuts;

    @SerializedName("total_pages")
    public String mStrTotalPages;

    @SerializedName("results")
    public ArrayList<Movie> mMovies;

    public MoviesRequest() {}

    public String getmStrPage() { return mStrPage; }
    public void setmStrPage(String mStrPage) { this.mStrPage = mStrPage; }
    public String getmStrTotalResuts() { return mStrTotalResuts; }
    public void setmStrTotalResuts(String mStrTotalResuts) { this.mStrTotalResuts = mStrTotalResuts; }
    public String getmStrTotalPages() { return mStrTotalPages; }
    public void setmStrTotalPages(String mStrTotalPages) { this.mStrTotalPages = mStrTotalPages; }
    public ArrayList<Movie> getmMovies() { return mMovies; }
    public void setmMovies(ArrayList<Movie> mMovies) { this.mMovies = mMovies; }

    public int getSize() {
        if(mMovies == null) return 0;
        return mMovies.size();
    }

    public Movie getItem(int index) {
        if(mMovies == null) return null;
        return mMovies.get(index);
    }

}
