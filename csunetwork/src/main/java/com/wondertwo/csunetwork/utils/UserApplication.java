package com.wondertwo.csunetwork.utils;

import android.app.Application;

/**
 * Created by wondertwo on 2016/4/7.
 */
public class UserApplication extends Application {
    private static UserApplication instance;
    private static SharedPrefsUtils sharedPrefsUtils;

    public UserApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPrefsUtils = new SharedPrefsUtils(this);
    }

    public synchronized static UserApplication getInstance() {
        if (null == instance) {
            instance = new UserApplication();
        }
        return instance;
    }

    public static SharedPrefsUtils getSpUtil(){
        return sharedPrefsUtils;
    }

}
