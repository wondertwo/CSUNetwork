package com.wondertwo.csunetwork.ui.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by wondertwo on 2016/4/5.
 */
public class BaseContentPager {

    // 定义Activity接收传入的Context对象的activity
    public Activity mActivity;
    // 定义主页面根布局对象
    public View mRootView;
    // 标题
    public TextView tvTitle;
    // 内容
    public FrameLayout flContent;
    // 左侧边栏图片按钮
    public ImageButton btnLeftMenu;

    // 构造方法
    public BaseContentPager(Activity activity) {
        this.mActivity = activity;
        initViews();
    }

    /**
     * 初始化布局
     */
    private void initViews() {

    }
}
