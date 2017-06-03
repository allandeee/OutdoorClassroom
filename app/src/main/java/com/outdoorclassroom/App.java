package com.outdoorclassroom;

import android.app.Application;
import android.content.Context;

/**
 * Created by Allan on 31-May-17.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext () {
        return mContext;
    }

}
