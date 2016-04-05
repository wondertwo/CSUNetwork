package com.wondertwo.csunetwork.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager,拦截ViewPager的滑动事件,不能滑动
 * Created by wondertwo on 2016/3/8.
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 重写onTouchEvent(MotionEvent ev)方法，拦截ViewPager的触摸滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}