package me.wondertwo.csu.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

import me.wondertwo.csu.R;
import me.wondertwo.csu.async.AsyncTaskWarpper;
import me.wondertwo.csu.async.listener.NotifyListener;
import me.wondertwo.csu.net.NetConnectFactory;
import me.wondertwo.csu.net.NetRequestResult;
import me.wondertwo.csu.net.NetworkConstant;
import me.wondertwo.csu.ui.drawer.DrawerFragment;
import me.wondertwo.csu.ui.skin.ChangeSkin;
import me.wondertwo.csu.ui.wifi.LoginSuccess;
import me.wondertwo.csu.utils.NetStateUtils;
import me.wondertwo.csu.utils.PreferUtils;
import me.wondertwo.csu.utils.SnackBarUtils;
import me.wondertwo.csu.utils.ToastUtils;
import me.wondertwo.csu.utils.statusbar.StatusBarUtils;

/**
 * Login Prepare Page
 *
 * Created by wondertwo on 2016/6/11.
 */
public class ContainerActivity extends TopLevelActivity {

    private String userNumber; // 账号
    private String userPassword; // 密码
    private EditText inputUserNumber; // EditText输入的账户
    private EditText inputUserPassword; // EditText输入的密码
    private ProgressBar loginLoadingProbar; // 进度条
    private DrawerLayout mDrawerLayout;
    private LinearLayout mSnackbarContainer;
    private DrawerFragment mDrawerFragment;
    private PreferUtils mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_container);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.container_drawer_layout);
        // match the status bar
        StatusBarUtils.setColorForDrawerLayout(this, mDrawerLayout, Color.parseColor(getString(R.string.container_bar_color)), 0);

        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.drawer_fragment);
        mDrawerFragment.setDrawerLayout(mDrawerLayout);
        /*mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });*/

        mSnackbarContainer = (LinearLayout) findViewById(R.id.container_layout);
        ImageButton mLeftButton = (ImageButton) findViewById(R.id.login_prepare_left);
        ImageButton mRightButton = (ImageButton) findViewById(R.id.login_prepare_right);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContainerActivity.this, ChangeSkin.class));
            }
        });
        mPreferences = new PreferUtils(this, "sp_container");
        loginLoadingProbar = (ProgressBar) findViewById(R.id.login_loading_probar);
        inputUserNumber = (EditText) findViewById(R.id.user_number);
        inputUserPassword = (EditText) findViewById(R.id.user_password);

        resettingUserInfo();
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
            /**
             * 判断WIFI是否连接
             */
            if (!NetStateUtils.checkWifiConnection(this)) {
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
                        .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContainerActivity.this.finish();
                            }
                        }).show();
            } else {
                prepareForLogin();
            }
        }
    }

    /**
     * 登录前准备工作
     */
    private void prepareForLogin() {

        showProgressbar();
        AsyncTaskWarpper.getATWInstance().doAsyncWork(new Callable<Object>() {
            // 回调方法，在异步任务的doInBackground()方法中执行，发出网络请求，并返回请求结果串
            @Override
            public Object call() throws Exception {
                saveUserLoginInfo();
                return NetConnectFactory.getNCFInstance(ContainerActivity.this).doLogin(userNumber, userPassword);
            }
        }, new NotifyListener() { // cancelListener,异步请求取消的监听,处理取消异常
            @Override
            public void onNotify(Object result) {
                ToastUtils.showToastShort(ContainerActivity.this, "取消");
            }
        }, new NotifyListener() { // finishListener,异步请求结束的监听,处理请求结束逻辑
            @Override
            public void onNotify(Object result) { // 此处接收的result参数，即为网络请求结果串
                dismissProgressbar();
                NetRequestResult ar = (NetRequestResult) result;
                if (!TextUtils.isEmpty((String) ar.getArgs()[0])) { // 有返回结果的情况
                    try {
                        JSONObject jsonObject = new JSONObject((String) ar.getArgs()[0]);
                        String[] arr = getResources().getStringArray(R.array.login_result_code);
                        int resultCode;
                        if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < arr.length) {
                            switch (resultCode) {
                                //0代表登陆成功，10代表密码简单，这两种情况都是登陆成功地提示
                                case 0:
                                case 10:
                                    ToastUtils.showToastShort(ContainerActivity.this, R.string.login_success);
                                    gotoShowResultActivity(jsonObject.toString());
                                    break;
                                case 1:
                                    if (jsonObject.has("resultDescribe")) {
                                        showAlertDialog(jsonObject.getString("resultDescribe"));
                                    }
                                    break;
                                default:
                                    SnackBarUtils.makeLong(mSnackbarContainer, arr[resultCode]);
                            }
                        } else {
                            SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_login_failed));
                        }
                    } catch (JSONException e) {
                        SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_login_failed));
                    }
                } else {
                    SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_login_failed));
                }
            }
        });
    }

                                /*case 0:
                                case 11:
                                    // 登陆成功
                                    SnackBarUtils.makeLong(mCoordinatorLayout, getString(R.string.login_success));
                                    break;
                                case 2:
                                    // 已在线
                                    SnackBarUtils.makeLong(mCoordinatorLayout, getString(R.string.online_try_logout));
                                    break;
                                case 12:
                                case 13:
                                    // wifi未连接
                                    Snackbar.make(mCoordinatorLayout, R.string.not_connected_wifi, Snackbar.LENGTH_LONG)
                                            .setAction(R.string.confirm, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                                }
                                            }).show();
                                    break;
                                default:
                                    // 未知错误
                                    SnackBarUtils.makeLong(mCoordinatorLayout, getString(R.string.unknown_error));
                                    break;*/


    /**
     * 已在线？尝试下线
     */
    public void onOnlineLogoutClicked(View view) {
        showProgressbar();
        AsyncTaskWarpper.getATWInstance().doAsyncWork(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return NetConnectFactory.getNCFInstance(ContainerActivity.this).doLogout();
            }
        }, new NotifyListener() {
            @Override
            public void onNotify(Object result) {

            }
        }, new NotifyListener() {
            @Override
            public void onNotify(Object result) {
                dismissProgressbar();
                NetRequestResult ar = (NetRequestResult) result;
                if (!TextUtils.isEmpty(ar.getArgs()[0].toString())) {
                    if ((ar.getArgs()[0]) instanceof Exception) {
                        Log.e("============", ar.toString());
                        SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknow_result_logout_failed));
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(ar.getArgs()[0].toString());
                            String[] arr = getResources().getStringArray(R.array.logout_result_code);
                            int resultCode;
                            if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < arr.length) {
                                switch (resultCode) {
                                    case 0:
                                        ToastUtils.showToastShort(ContainerActivity.this, R.string.logout_success);
                                        break;
                                    default:
                                        SnackBarUtils.makeLong(mSnackbarContainer, arr[resultCode]);
                                }
                            } else
                                SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknow_result_logout_failed));
                        } catch (JSONException e) {
                            SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknow_result_logout_failed));
                        }
                    }
                } else {
                    SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknow_result_logout_failed));
                }
            }
        });
    }

    /**
     * 跳转到登录成功展示页
     */
    private void gotoShowResultActivity(String jsonResult) {
        Intent intent = new Intent(this, LoginSuccess.class);
        intent.putExtra(LoginSuccess.INTRNT_EXTRA_NAME, jsonResult);
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
    private void dismissProgressbar() { loginLoadingProbar.setVisibility(View.INVISIBLE); }

    /**
     * 填充保存的用户名和密码
     */
    public void resettingUserInfo() {
        userNumber = mPreferences.getValue(NetworkConstant.SP_USER_NUMBER, "");
        userPassword = mPreferences.getValue(NetworkConstant.SP_USER_PASSWORD, "");
        inputUserNumber.setText(userNumber);
        inputUserPassword.setText(userPassword);
    }

    /**
     * saveUserLoginInfo()保存用户登录信息
     */
    private void saveUserLoginInfo() {
        userNumber = inputUserNumber.getText().toString().trim();
        userPassword = inputUserPassword.getText().toString().trim();
        mPreferences.setValue(NetworkConstant.SP_USER_NUMBER, userNumber);
        mPreferences.setValue(NetworkConstant.SP_USER_PASSWORD, userPassword);
    }

    /**
     * 输入为空的提示
     */
    private void doTextViewEmptyTip(EditText et) {
        et.requestFocus(); // 获取焦点
        ToastUtils.showToastShort(this, getResources().getString(R.string.textview_empty));
    }

    private void showAlertDialog(String content) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(content)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showAlertDialog(int content) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(content)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
