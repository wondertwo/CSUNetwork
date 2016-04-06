package com.wondertwo.csunetwork.ui;

import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wondertwo.csunetwork.R;

/**
 *
 */
public class LoginActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置前景布局
        setContentView(R.layout.activity_content_page);
        // 设置后景布局
        setBehindContentView(R.layout.activity_left_menu);
        // 获取侧边栏对象
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);// 侧滑模式为全屏触摸
        // 设置屏幕预留宽度
        slidingMenu.setBehindOffset(160);
    }
}
