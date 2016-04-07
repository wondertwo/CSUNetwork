package com.wondertwo.csunetwork.ui.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.ui.BaseSlidingActivity;

/**
 * BaseNetworkActivity网络登录基类
 * Created by wondertwo on 2016/4/7.
 */
public class BaseNetworkActivity extends BaseSlidingActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    /**
     * 登录未知错误
     */
    protected void showLoginUnknowError() {
        showError(R.string.unknow_result_login_failed);
    }

    /**
     * 下线未知错误
     */
    protected void showLogoutUnknowError() {
        showError(R.string.unknow_result_logout_failed);
    }

    /**
     * 登录出错，int
     */
    protected void showError(int tip) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(tip)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    /**
     * 登录出错，String
     */
    protected void showError(String tip) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(tip)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }
}
