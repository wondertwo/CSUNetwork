package com.wondertwo.csunetwork.ui.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wondertwo.csunetwork.R;

/**
 * NetworkPager主页面wifi登录页
 * Created by wondertwo on 2016/4/5.
 */
public class NetworkPager extends BaseContentPager {

    public NetworkPager(Activity activity) {
        super(activity);
    }

    /**
     * 初始化布局的抽象方法initViews()
     */
    @Override
    public void initViews() {
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
    }

    /**
     * 初始化数据initData()
     */
    @Override
    public void initData() {
        // 设置标题栏内容
        tvTitle.setText("WIFI");
        /*btnLeftMenu.setVisibility(View.GONE);*/
        // 设置侧边栏的状态为可以打开
        setSlidingMenuEnable(true);

        // 向FrameLayout中动态的添加布局
        flContent.addView(ShowLoginPage(mActivity));
    }

    /**
     * 设置数字中南登录页面布局
     */
    private RelativeLayout ShowLoginPage(Activity activity) {
        RelativeLayout layout = new RelativeLayout(activity);
        return layout;
    }
}
