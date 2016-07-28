package me.wondertwo.csu.app;

import android.app.Application;

import cn.feng.skin.manager.loader.SkinManager;
import me.wondertwo.csu.utils.PreferUtils;

/**
 *
 *
 * Created by wondertwo on 2016/6/11.
 */
public class MainApplication extends Application {

    private static MainApplication instance;
    private static PreferUtils spUtil;

    private MainApplication() {}

    @Override
    public void onCreate() {
        super.onCreate();
        spUtil = new PreferUtils(this);
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

    /**
     * @return  the PreferUtils obj
     */
    public static PreferUtils getSpUtil(){
        return spUtil;
    }

    /**
     * @return  the global Application obj
     */
    public synchronized static MainApplication getAppInstance() {
        if (null == instance) {
            instance = new MainApplication();
        }
        return instance;
    }
}
