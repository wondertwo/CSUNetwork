package com.wondertwo.csunetwork.ui;

import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.wondertwo.csunetwork.R;

/**
 * BaseSlidingActivity侧滑菜单基类
 * Created by wondertwo on 2016/4/6.
 */
public class BaseSlidingActivity extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置后景布局
        setBehindContentView(R.layout.activity_sliding_menu);
        // 获取侧边栏对象
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);// 侧滑模式为全屏触摸
        // 设置屏幕预留宽度
        slidingMenu.setBehindOffset(160);
    }
}
