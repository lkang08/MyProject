package com.lk.myproject.activity;

import android.app.Application;

public class App extends Application {

    private static App mApplication = null;
    public static App getInstance(){
        return mApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
    }
}
