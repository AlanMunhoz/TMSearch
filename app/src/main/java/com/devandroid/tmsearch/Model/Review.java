package com.devandroid.tmsearch.Model;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    @SerializedName("id")
    private String mId;

    @SerializedName("url")
    private String mUrl;

    public Review(String strAuthor, String strContent, String strId, String strUrl) {

        mAuthor = strAuthor;
        mContent = strContent;
        mId = strId;
        mUrl = strUrl;
    }

    public String getmAuthor() { return mAuthor; }
    public String getmContent() { return mContent; }
    public String getmId() { return mId; }
    public String getmUrl() { return mUrl; }
}
