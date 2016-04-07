package com.wondertwo.csunetwork.ui.network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.async.AsyncTaskWarpper;
import com.wondertwo.csunetwork.async.AsyncWorkResult;
import com.wondertwo.csunetwork.async.listener.NotifyListener;
import com.wondertwo.csunetwork.newwork.NetConnectFactory;
import com.wondertwo.csunetwork.user.NetworkConstant;
import com.wondertwo.csunetwork.utils.NetConnectUtils;
import com.wondertwo.csunetwork.utils.SharedPrefsUtils;
import com.wondertwo.csunetwork.utils.ShowToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

/**
 * LoginActivity登录页
 */
public class LoginActivity extends BaseNetworkActivity {

    private String userNumber; // 用户名
    private String userPassword; // 密码
    private EditText inputUserNumber; // 账户
    private EditText inputUserPassword; // 密码
    private ProgressBar loginLoadingProbar; // 进度条
    private SharedPrefsUtils sharedPrefsUtils; // SharedPreferences工具类

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置前景布局
        setContentView(R.layout.activity_login_page);

        sharedPrefsUtils = new SharedPrefsUtils(this);
        inputUserNumber = (EditText) findViewById(R.id.user_number);
        inputUserPassword = (EditText) findViewById(R.id.user_password);
        loginLoadingProbar = (ProgressBar) findViewById(R.id.login_loading_probar);

        // 获取用户的用户名和密码
        restoreUserInfo();
    }

    /**
     * 登录按钮点击事件
     */
    public void onLoginButtonClicked(View view) {
        userNumber = inputUserNumber.getText().toString().trim();
        userPassword = inputUserPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userNumber)) {
            doTextViewEmptyTip(inputUserNumber);
        } else if (TextUtils.isEmpty(userPassword)) {
            doTextViewEmptyTip(inputUserPassword);
        } else {
            performForLogin();
        }
    }

    /**
     * 登录前准备工作
     */
    private void performForLogin() {
        if (!NetConnectUtils.checkWifiConnection(this)) {
            /**
             * WIFI未连接，弹出提示连接框
             */
            new AlertDialog.Builder(this)
                    .setTitle(R.string.tip)
                    .setMessage(R.string.not_connected_wifi)
                    .setPositiveButton(R.string.goto_connect, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /**
                             * 打开WIFI设置页面
                             */
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton(R.string.system_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.this.finish();
                        }
                    }).show();
        } else {
            /**
             * WIFI登陆流程分为两步:
             * 1. 尝试加载一个网址来连接数字中南登陆页面，获取页面中brasAddress和userIntranetAddress两个值；
             * 2. 将加密的密码和账号连同第一步获取的两个值一起post给数字中南登陆页，实现登陆。
             */
            showProgressbar();
            AsyncTaskWarpper.getATWInstance().doAsyncWork(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    /**
                     * 保存用户登录信息
                     */
                    saveUserLoginInfo();
                    return NetConnectFactory.getInstance(LoginActivity.this).doLogin("01" + userNumber, userPassword);
                }
            }, new NotifyListener() {
                @Override
                public void onNotify(Object result) {
                    ShowToastUtils.showToastLong(LoginActivity.this, "取消");
                }
            }, new NotifyListener() {
                //登陆后的回调
                @Override
                public void onNotify(Object result) {
                    dismissProgressbar();
                    AsyncWorkResult ar = (AsyncWorkResult) result;
                    //有返回结果的情况
                    if (!TextUtils.isEmpty((String) ar.getArgs()[0])) {
                        try {
                            JSONObject jsonObject = new JSONObject((String) ar.getArgs()[0]);
                            String[] arr = getResources().getStringArray(R.array.login_tip);
                            int resultCode = -1;
                            if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < arr.length) {
                                switch (resultCode) {
                                    /**
                                     * 0代表登陆成功，10代表密码简单，这两种情况都是登陆成功地提示。
                                     */
                                    case 0:
                                    case 10:
                                        ShowToastUtils.showToastLong(LoginActivity.this, R.string.login_success);
                                        gotoLoginSuccessActivity(jsonObject.toString());
                                        break;
                                    case 1:
                                        if (jsonObject.has("resultDescribe"))
                                            showError(jsonObject.getString("resultDescribe"));
                                        break;
                                    default:
                                        showError(arr[resultCode]);
                                }
                            } else {
                                Log.e("TAG", "1");
                                showLoginUnknowError();
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "2");
                            showLoginUnknowError();
                        }
                    } else {
                        Log.e("TAG", "3");
                        showLoginUnknowError();
                    }
                }
            });
        }
    }

    /**
     * 跳转到登录成功展示页
     */
    private void gotoLoginSuccessActivity(String jsonResult) {
        Intent intent = new Intent();
        intent.setClass(this, ShowResultActivity.class);
        intent.putExtra(ShowResultActivity.INTRNT_EXTRA_NAME, jsonResult);
        startActivity(intent);
        this.finish();
    }

    /**
     * 展示进度条showProgressbar()
     */
    private void showProgressbar() { loginLoadingProbar.setVisibility(View.VISIBLE); }

    /**
     * 隐藏进度条dismissProgressbar()
     */
    private void dismissProgressbar() { loginLoadingProbar.setVisibility(View.GONE); }

    /**
     * 填充已保存的用户名和密码
     */
    public void restoreUserInfo() {
        userNumber = sharedPrefsUtils.getValue(NetworkConstant.SP_USER_NUMBER, "");
        userPassword = sharedPrefsUtils.getValue(NetworkConstant.SP_USER_PASSWORD, "");
        inputUserNumber.setText(userNumber);
        inputUserPassword.setText(userPassword);
    }

    /**
     * saveUserLoginInfo()保存用户登录信息
     */
    private void saveUserLoginInfo() {
        sharedPrefsUtils.setValue(NetworkConstant.SP_USER_NUMBER, userNumber);
        sharedPrefsUtils.setValue(NetworkConstant.SP_USER_PASSWORD, userPassword);
    }

    /**
     * 输入为空的提示
     */
    private void doTextViewEmptyTip(EditText et) {
        et.requestFocus(); // 获取焦点
        ShowToastUtils.showToastLong(this, getResources().getString(R.string.textview_empty));
    }
}
