package com.devandroid.tmsearch.Model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


/**
 * Parcel use
 * (1) Annotate the class with the @Parcel decorator.
 * (2) Use only public fields (private fields cannot be detected during annotation) that need to be serialized.
 * (3) Create a public constructor with no arguments for the annotation library.
 */
@Parcel
public class Movie {

    @SerializedName("vote_count")
    public String mStrVoteCount;

    @SerializedName("id")
    public String mStrId;

    @SerializedName("video")
    public String mStrVideo;

    @SerializedName("vote_average")
    public String mStrVoteAverage;

    @SerializedName("title")
    public String mStrTitle;

    @SerializedName("popularity")
    public String mStrPopularity;

    @SerializedName("poster_path")
    public String mStrPosterPath;

    @SerializedName("original_language")
    public String mStrOriginalLanguage;

    @SerializedName("original_title")
    public String mStrOriginalTitle;

    @SerializedName("genre_ids")
    public String[] mLstGenreIds;

    @SerializedName("backdrop_path")
    public String mStrBackdropPath;

    @SerializedName("adult")
    public String mStrAdult;

    @SerializedName("overview")
    public String mStrOverview;

    @SerializedName("release_date")
    public String mStrReleaseDate;


    public Movie() {}

    public Movie(
            String strId,
            String strTitle,
            String strPosterPath,
            String strOverview,
            String strVoteAverage,
            String strReleaseDate,
            String strVoteCount,
            String strPopularity,
            String strVideo,
            String strOriginalLanguage,
            String strOriginalTitle,
            String strBackdropPath,
            String strAdult
            ) {

        mStrId = strId;
        mStrTitle = strTitle;
        mStrPosterPath = strPosterPath;
        mStrOverview = strOverview;
        mStrVoteAverage = strVoteAverage;
        mStrReleaseDate = strReleaseDate;
        mStrVoteCount = strVoteCount;
        mStrPopularity = strPopularity;
        mStrVideo = strVideo;
        mStrOriginalLanguage = strOriginalLanguage;
        mStrOriginalTitle = strOriginalTitle;
        mStrBackdropPath = strBackdropPath;
        mStrAdult = strAdult;
    }

    public String getmStrVoteCount() { return mStrVoteCount; }
    public void setmStrVoteCount(String mStrVoteCount) { this.mStrVoteCount = mStrVoteCount; }
    public String getmStrId() { return mStrId; }
    public void setmStrId(String mStrId) { this.mStrId = mStrId; }
    public String getmStrVideo() { return mStrVideo; }
    public void setmStrVideo(String mStrVideo) { this.mStrVideo = mStrVideo; }
    public String getmStrVoteAverage() { return mStrVoteAverage; }
    public void setmStrVoteAverage(String mStrVoteAverage) { this.mStrVoteAverage = mStrVoteAverage; }
    public String getmStrTitle() { return mStrTitle; }
    public void setmStrTitle(String mStrTitle) { this.mStrTitle = mStrTitle; }
    public String getmStrPopularity() { return mStrPopularity; }
    public void setmStrPopularity(String mStrPopularity) { this.mStrPopularity = mStrPopularity; }
    public String getmStrPosterPath() { return mStrPosterPath; }
    public void setmStrPosterPath(String mStrPosterPath) { this.mStrPosterPath = mStrPosterPath; }
    public String getmStrOriginalLanguage() { return mStrOriginalLanguage; }
    public void setmStrOriginalLanguage(String mStrOriginalLanguage) { this.mStrOriginalLanguage = mStrOriginalLanguage; }
    public String getmStrOriginalTitle() { return mStrOriginalTitle; }
    public void setmStrOriginalTitle(String mStrOriginalTitle) { this.mStrOriginalTitle = mStrOriginalTitle; }
    public String[] getmLstGenreIds() { return mLstGenreIds; }
    public void setmLstGenreIds(String[] mLstGenreIds) { this.mLstGenreIds = mLstGenreIds; }
    public String getmStrBackdropPath() { return mStrBackdropPath; }
    public void setmStrBackdropPath(String mStrBackdropPath) { this.mStrBackdropPath = mStrBackdropPath; }
    public String getmStrAdult() { return mStrAdult; }
    public void setmStrAdult(String mStrAdult) { this.mStrAdult = mStrAdult; }
    public String getmStrOverview() { return mStrOverview; }
    public void setmStrOverview(String mStrOverview) { this.mStrOverview = mStrOverview; }
    public String getmStrReleaseDate() { return mStrReleaseDate; }
    public void setmStrReleaseDate(String mStrReleaseDate) { this.mStrReleaseDate = mStrReleaseDate; }

}
