package com.wondertwo.csudigit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.wondertwo.csudigit.R;
import com.wondertwo.csudigit.ui.network.LoginPageActivity;

/**
 * SplashActivity闪屏页
 * Created by wondertwo on 2016/4/6.
 */
public class AppSplashActivity extends Activity {

    private ImageView ivSplashWifi_1;
    private ImageView ivSplashWifi_2;
    private ImageView ivSplashWifi_3;
    private ImageView ivSplashWifi_4;
    private AnimationSet set_2;
    private AnimationSet set_3;
    private AnimationSet set_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_splash);

        ivSplashWifi_1 = (ImageView) findViewById(R.id.iv_splash_wifi_1);
        ivSplashWifi_2 = (ImageView) findViewById(R.id.iv_splash_wifi_2);
        ivSplashWifi_3 = (ImageView) findViewById(R.id.iv_splash_wifi_3);
        ivSplashWifi_4 = (ImageView) findViewById(R.id.iv_splash_wifi_4);

        showSplashAnimation();
    }

    /**
     * 展示闪屏页动画
     */
    private void showSplashAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0.8f, 1f);
        ScaleAnimation scale = new ScaleAnimation(0.8f, 2f, 0.8f, 2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 1.0f);

        AnimationSet set_1 = new AnimationSet(true);
        set_1.addAnimation(scale);
        set_1.addAnimation(alpha);
        set_1.setDuration(800);
        set_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivSplashWifi_1.setVisibility(View.INVISIBLE);
                ivSplashWifi_2.setVisibility(View.VISIBLE);
                ivSplashWifi_2.startAnimation(set_2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        set_2 = new AnimationSet(true);
        set_2.addAnimation(scale);
        set_2.addAnimation(alpha);
        set_2.setDuration(800);
        set_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivSplashWifi_2.setVisibility(View.INVISIBLE);
                ivSplashWifi_3.setVisibility(View.VISIBLE);
                ivSplashWifi_3.startAnimation(set_3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        set_3 = new AnimationSet(true);
        set_3.addAnimation(scale);
        set_3.addAnimation(alpha);
        set_3.setDuration(800);
        set_3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivSplashWifi_3.setVisibility(View.INVISIBLE);
                ivSplashWifi_4.setVisibility(View.VISIBLE);
                ivSplashWifi_4.startAnimation(set_4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        set_4 = new AnimationSet(true);
        set_4.addAnimation(scale);
        set_4.addAnimation(alpha);
        set_4.setDuration(800);
        set_4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gotoLoginPageActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ivSplashWifi_1.setVisibility(View.VISIBLE);
        ivSplashWifi_1.startAnimation(set_1);
    }

    /**
     * 跳转到登录页面
     */
    private void gotoLoginPageActivity() {
        startActivity(new Intent(AppSplashActivity.this, LoginPageActivity.class));
        finish();
    }
}
