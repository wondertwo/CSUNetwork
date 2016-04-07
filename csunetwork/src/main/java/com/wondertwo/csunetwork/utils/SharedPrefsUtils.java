package com.wondertwo.csunetwork.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by wondertwo on 2016/4/6.
 */
public class SharedPrefsUtils {
    private Context context;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;

    /**
     * 创建默认sp
     */
    public SharedPrefsUtils(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    /**
     * 通过文件名创建sp
     */
    public SharedPrefsUtils(Context context, String filename) {
        this(context, context.getSharedPreferences(filename, Context.MODE_WORLD_WRITEABLE));
    }

    /**
     * 通过sp创建sp
     */
    public SharedPrefsUtils(Context context, SharedPreferences sp) {
        this.context = context;
        this.sp = sp;
        editor = sp.edit();
    }

    public SharedPreferences getSPInstance() {
        return sp;
    }

    /**
     * Setter
     */
    // Boolean
    public void setValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setValue(int resKey, boolean value) {
        setValue(this.context.getString(resKey), value);
    }

    // Float
    public void setValue(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void setValue(int resKey, float value) {
        setValue(this.context.getString(resKey), value);
    }

    // Integer
    public void setValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void setValue(int resKey, int value) {
        setValue(this.context.getString(resKey), value);
    }

    // Long
    public void setValue(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void setValue(int resKey, long value) {
        setValue(this.context.getString(resKey), value);
    }

    // String
    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setValue(int resKey, String value) {
        setValue(this.context.getString(resKey), value);
    }

    /**
     * Getter
     */
    // Boolean
    public boolean getValue(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public boolean getValue(int resKey, boolean defaultValue) {
        return getValue(this.context.getString(resKey), defaultValue);
    }

    // Float
    public float getValue(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public float getValue(int resKey, float defaultValue) {
        return getValue(this.context.getString(resKey), defaultValue);
    }

    // Integer
    public int getValue(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public int getValue(int resKey, int defaultValue) {
        return getValue(this.context.getString(resKey), defaultValue);
    }

    // Long
    public long getValue(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public long getValue(int resKey, long defaultValue) {
        return getValue(this.context.getString(resKey), defaultValue);
    }

    // String
    public String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public String getValue(int resKey, String defaultValue) {
        return getValue(this.context.getString(resKey), defaultValue);
    }

    /**
     * Delete
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 判断是否第一次启动应用
     */
    public boolean isFirstStart() {
        if (sp == null) {
            return true;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            int curVersion = info.versionCode;
            int lastVersion = getValue("version", 0);
            if (curVersion > lastVersion) {
                /**
                 * 如果当前版本大于上次版本，该版本属于第一次启动
                 * 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
                 */
                setValue("version", curVersion);
                return true;
            } else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否第一次安装应用
     */
    public boolean isFirstInstall(Context context) {
        int install = getValue("first_install", 0);
        if (install == 0) {
            return true;
        }
        setValue("first_install", 1);
        return false;
    }
}
