package com.wondertwo.csunetwork.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.ui.network.LoginPageActivity;
import com.wondertwo.csunetwork.ui.setting.SettingPageActivity;
import com.wondertwo.csunetwork.ui.userinfo.UserLoginActivity;
import com.wondertwo.csunetwork.ui.wechat.WechatPageActivity;
import com.wondertwo.csunetwork.ui.weibo.WeiboPageActivity;

/**
 * BaseSlidingActivity侧滑菜单基类
 * Created by wondertwo on 2016/4/6.
 */
public class BaseSlidingActivity extends SlidingActivity {

    private ImageView ivSlidingUserPicture;
    private LinearLayout llSlidingNetwork;
    private LinearLayout llSlidingWechat;
    private LinearLayout llSlidingWeibo;
    private LinearLayout llSlidingSetting;

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
        slidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() * 2 / 5);

        /**
         * 点击用户头像登录页面
         */
        ivSlidingUserPicture = (ImageView) findViewById(R.id.iv_sliding_menu_user_picture);
        ivSlidingUserPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseSlidingActivity.this, UserLoginActivity.class));
            }
        });

        /**
         * 跳转到数字中南登录页面
         */
        llSlidingNetwork = (LinearLayout) findViewById(R.id.ll_sliding_menu_network);
        llSlidingNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseSlidingActivity.this, LoginPageActivity.class));
            }
        });

        /**
         * 跳转到中南大学微信页面
         */
        llSlidingWechat = (LinearLayout) findViewById(R.id.ll_sliding_menu_wechat);
        llSlidingWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseSlidingActivity.this, WechatPageActivity.class));
            }
        });

        /**
         * 跳转到中南大学微博页面
         */
        llSlidingWeibo = (LinearLayout) findViewById(R.id.ll_sliding_menu_weibo);
        llSlidingWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseSlidingActivity.this, WeiboPageActivity.class));
            }
        });

        /**
         * 跳转到用户设置页面
         */
        llSlidingSetting = (LinearLayout) findViewById(R.id.ll_sliding_menu_setting);
        llSlidingSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseSlidingActivity.this, SettingPageActivity.class));
            }
        });
    }
}
