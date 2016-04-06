package com.wondertwo.csunetwork.ui;

import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.fragment.ContentFra;
import com.wondertwo.csunetwork.fragment.LeftMenuFra;

/**
 *
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_LEFT_MENU = "fragment_left";
    private static final String FRAGMENT_NEWS = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置前景布局
        setContentView(R.layout.activity_main);
        // 设置后景布局
        setBehindContentView(R.layout.activity_left_menu);
        // 获取侧边栏对象
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);// 侧滑模式为全屏触摸
        // 设置屏幕预留宽度
        slidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 2);

        // 初始化Fragment
        initFragment();
    }

    // 初始化Fragment
    private void initFragment() {
        // 获取FragmentManager
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction =
                fm.beginTransaction();// 开启fragment事务

        // 用Fragment实例替换空布局
        transaction.replace(R.id.fl_content, new ContentFra(), FRAGMENT_NEWS);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFra(), FRAGMENT_LEFT_MENU);

        transaction.commit();
    }
}
