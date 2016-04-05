package com.wondertwo.csunetwork.ui.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by wondertwo on 2016/4/5.
 */
public class NewsPager extends BaseContentPager {

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        // 设置标题栏内容
        tvTitle.setText("信息浏览页");
        // btnLeftMenu.setVisibility(View.GONE);
        setSlidingMenuEnable(true);// 打开侧边栏

        // 设置内容区布局
        TextView text = new TextView(mActivity);
        text.setText("信息浏览页");
        text.setTextColor(Color.RED);
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态的添加布局
        flContent.addView(text);
    }
}
