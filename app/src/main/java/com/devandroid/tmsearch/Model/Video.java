package com.devandroid.tmsearch.Model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    private String mId;

    @SerializedName("iso_639_1")
    private String mIso_639_1;

    @SerializedName("iso_3166_1")
    private String mIso_3166_1;

    @SerializedName("key")
    private String mKey;

    @SerializedName("name")
    private String mName;

    @SerializedName("site")
    private String mSite;

    @SerializedName("size")
    private String mSize;

    @SerializedName("type")
    private String mType;

    public Video(
            String strId,
            String strIso_639_1,
            String strIso_3166_1,
            String strKey,
            String strName,
            String strSite,
            String strSize,
            String strType
    ) {

        mId = strId;
        mIso_639_1 = strIso_639_1;
        mIso_3166_1 = strIso_3166_1;
        mKey = strKey;
        mName = strName;
        mSite = strSite;
        mSize = strSize;
        mType = strType;
    }

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

}
