package com.wondertwo.csu.app;

import android.app.Application;

import cn.feng.skin.manager.loader.SkinManager;

/**
 *
 *
 * Created by wondertwo on 2016/6/11.
 */
public class SkinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initSkinLoader();
    }

    /**
     * initialize the skin loader
     */
    private void initSkinLoader() {
        // Must call init first
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().load();
    }
}
