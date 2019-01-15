package com.devandroid.tmsearch.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideosRequest {

    @SerializedName("id")
    private int mId;

    @SerializedName("results")
    private ArrayList<Video> mVideos;

    public ArrayList<Video> getList() {
        return mVideos;
    }

    public int getSize() {
        if(mVideos == null) return 0;
        return mVideos.size();
    }
}
