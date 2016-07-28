package me.wondertwo.csu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 *
 * Created by wondertwo on 2016/4/9.
 */
public class PreferUtils {
    private Context context;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor edit = null;

    public PreferUtils(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public PreferUtils(Context context, String filename) {
        this(context, context.getSharedPreferences(filename,
                Context.MODE_WORLD_WRITEABLE));
    }

    public PreferUtils(Context context, SharedPreferences sp) {
        this.context = context;
        this.sp = sp;
        edit = sp.edit();
    }

    /**
     * 获取SharedPreferences对象
     */
    public SharedPreferences getSp(Context context) {
        return sp;
    }

    // Set

    // Boolean
    public void setValue(String key, boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }

    // Float
    public void setValue(String key, float value) {
        edit.putFloat(key, value);
        edit.commit();
    }

    // Integer
    public void setValue(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }
    // Long
    public void setValue(String key, long value) {
        edit.putLong(key, value);
        edit.commit();
    }

    // String
    public void setValue(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }

    // Get

    // Boolean
    public boolean getValue(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    // Float
    public float getValue(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    // Integer
    public int getValue(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    // Long
    public long getValue(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    // String
    public String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    // Delete
    public void remove(String key) {
        edit.remove(key);
        edit.commit();
    }

    public void clear() {
        edit.clear();
        edit.commit();
    }

    /**
     * 是否第一次启动应用
     *
     * @return
     */
    public boolean isFirstTimeStart() {
        if(sp == null){
            return true;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            int curVersion = info.versionCode;
            int lastVersion = getValue("version", 0);
            if (curVersion > lastVersion) {
                // 如果当前版本大于上次版本，该版本属于第一次启动
                // 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
                setValue("version",curVersion);
                return true;
            } else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 是否第一次安装应用
     *
     * @param context
     * @return
     */
    public boolean isFirstInstall(Context context) {
        int install = getValue("first_install", 0);
        if (install == 0)
            return true;
        setValue("first_install", 1);
        return false;
    }
}
