package com.wondertwo.csudigit.ui.network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wondertwo.csudigit.R;
import com.wondertwo.csudigit.utils.SharedPreferUtils;

/**
 * BaseNetworkActivity网络登录基类
 * Created by wondertwo on 2016/4/7.
 */
public class BaseNetworkActivity extends AppCompatActivity {

    private static SharedPreferUtils sharedPreferUtils;

    public BaseNetworkActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferUtils = new SharedPreferUtils(this);
    }

    /**
     * 获取SharedPreferences工具类对象
     */
    public static SharedPreferUtils getSpUtil() {
        return sharedPreferUtils;
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
                .setNegativeButton(R.string.system_exit, new DialogInterface.OnClickListener() {
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
                .setNegativeButton(R.string.system_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }
}
