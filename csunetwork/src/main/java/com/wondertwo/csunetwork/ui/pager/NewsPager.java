package com.wondertwo.csunetwork.ui.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wondertwo.csunetwork.R;

/**
 * NetPager主页面wifi登录页
 * Created by wondertwo on 2016/4/5.
 */
public class NewsPager extends BaseContentPager {

    public NewsPager(Activity activity) {
        super(activity);
    }

    /**
     * 初始化布局的抽象方法initViews()
     */
    @Override
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.base_content_pager, null);

        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title_text);
        rlContent = (RelativeLayout) mRootView.findViewById(R.id.rl_base_pager);
        btnLeftMenu = (ImageButton) mRootView.findViewById(R.id.ib_left_menu);

        btnLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 初始化数据initData()
     */
    @Override
    public void initData() {
        // 设置标题栏内容
        tvTitle.setText("资讯");
        // btnLeftMenu.setVisibility(View.GONE);
        setSlidingMenuEnable(true);// 打开侧边栏

        // 设置内容区布局
        TextView text = new TextView(mActivity);
        text.setText("资讯浏览页");
        text.setTextColor(Color.RED);
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态的添加布局
        rlContent.addView(text);
    }

}
