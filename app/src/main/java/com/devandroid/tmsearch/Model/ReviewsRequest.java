package com.devandroid.tmsearch.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewsRequest {

    @SerializedName("id")
    private String mId;

    @SerializedName("page")
    private String mPages;

    @SerializedName("results")
    private ArrayList<Review> mReviews;

    public ArrayList<Review> getReviews() { return mReviews; }
    public void setReviews(ArrayList<Review> mReviews) { this.mReviews = mReviews; }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmPages(String mPages) {
        this.mPages = mPages;
    }

    public int getSize() {
        if(mReviews == null) return 0;
        return mReviews.size();
    }

    public Review getItem(int index) {
        if(mReviews == null) return null;
        return mReviews.get(index);
    }
}
