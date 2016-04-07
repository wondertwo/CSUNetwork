package com.wondertwo.csunetwork.utils;

import android.app.Activity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper;
import com.wondertwo.csunetwork.ui.network.BaseNetworkActivity;

/**
 * Created by wondertwo on 2016/4/7.
 */
public class UserApplication extends SlidingActivityHelper {

    private static BaseNetworkActivity networkActivityContext;
    private static UserApplication userApplication;
    private static SharedPrefsUtils sharedPrefsUtils;

    public UserApplication(Activity activity) {
        super(activity);
        networkActivityContext = (BaseNetworkActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefsUtils = new SharedPrefsUtils(networkActivityContext);
    }
    /*sharedPrefsUtils = new SharedPrefsUtils(networkActivityContext);*/


    public synchronized static UserApplication getInstance() {
        if (userApplication == null) {
            userApplication = new UserApplication(networkActivityContext);
        }
        return userApplication;
    }

    public static SharedPrefsUtils getSpUtil() {
        return sharedPrefsUtils;
    }
}
