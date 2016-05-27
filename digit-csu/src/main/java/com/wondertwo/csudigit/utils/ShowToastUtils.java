package com.wondertwo.csudigit.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils显示吐司工具类
 * Created by wondertwo on 2016/4/6.
 */
public class ShowToastUtils {

    private ShowToastUtils() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 变量isShow记录是否已经显示Toast
     */
    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     */
    public static void showToastShort(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 短时间显示Toast
     */
    public static void showToastShort(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长时间显示Toast
     */
    public static void showToastLong(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 长时间显示Toast
     */
    public static void showToastLong(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 自定义Toast显示时间
     */
    public static void showToastCustom(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }

    /**
     * 自定义显示Toast时间
     */
    public static void showToastCustom(Context context, int message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }
}
