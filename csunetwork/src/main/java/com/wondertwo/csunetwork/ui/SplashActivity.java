package com.wondertwo.csunetwork.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.ui.network.LoginPageActivity;

/**
 * SplashActivity闪屏页
 * Created by wondertwo on 2016/4/6.
 */
public class SplashActivity extends Activity {

    private ImageView ivSplashWifi;
    private ImageView ivSplashWechat;
    private ImageView ivSplashWeibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplashWifi = (ImageView) findViewById(R.id.iv_splash_wifi);
        ivSplashWechat = (ImageView) findViewById(R.id.iv_splash_wechat);
        ivSplashWeibo = (ImageView) findViewById(R.id.iv_splash_weibo);

        showSplashAnimation();
    }

    /**
     * 展示闪屏页动画
     */
    private void showSplashAnimation() {
        RotateAnimation rotate = new RotateAnimation(0, 1080,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alpha = new AlphaAnimation(0.4f, 1f);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(rotate);
        set.addAnimation(alpha);
        set.setDuration(3 * 1000);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, LoginPageActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivSplashWifi.startAnimation(set);
        ivSplashWechat.startAnimation(set);
        ivSplashWeibo.startAnimation(set);
    }
}
