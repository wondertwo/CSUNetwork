package com.wondertwo.csunetwork.ui.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wondertwo.csunetwork.ui.MainActivity;

/**
 * 主页面的wifi登录页和信息浏览页的基类
 * Created by wondertwo on 2016/4/5.
 */
public abstract class BaseContentPager {

    // 接收Context对象activity
    public Activity mActivity;
    // 主页面根布局对象
    public View mRootView;
    // 标题栏显示的文字
    public TextView tvTitle;
    // 标题栏打开侧滑菜单的图片按钮
    public ImageButton btnLeftMenu;
    // 主页面内容区布局对象
    public FrameLayout flContent;

    // 构造方法
    public BaseContentPager(Activity activity) {
        this.mActivity = activity;
        initViews();
    }

    /**
     * 初始化布局initViews()抽象方法
     */
    public abstract void initViews();
    /*{
        mRootView = View.inflate(mActivity, R.layout.base_content_pager, null);

        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title_text);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_base_pager);
        btnLeftMenu = (ImageButton) mRootView.findViewById(R.id.ib_left_menu);

        btnLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }*/

    /**
     * 初始化数据initData()抽象方法
     */
    public abstract void initData();

    // 切换SlidingMenu的状态
    protected void toggleSlidingMenu() {
        MainActivity newsUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = newsUi.getSlidingMenu();
        slidingMenu.toggle();// 切换状态，显示时隐藏，隐藏时显示
    }

    // 设置侧滑菜单栏是否可以打开或关闭
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if (enable) {
            // 设置侧滑菜单的触摸打开模式为全屏触摸模式
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // 设置侧滑菜单的触摸打开模式为none，即关闭触摸打开
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
