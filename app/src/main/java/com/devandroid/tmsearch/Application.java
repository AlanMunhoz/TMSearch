package com.devandroid.tmsearch;

import com.devandroid.tmsearch.Firebase.FirebaseManager;
import com.devandroid.tmsearch.Util.Utils;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(getApplicationContext());
        FirebaseManager.init(getApplicationContext());
    }
}
