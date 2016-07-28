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
import me.wondertwo.csu.app.MainApplication;
import me.wondertwo.csu.async.AsyncTaskWrapper;
import me.wondertwo.csu.async.listener.NotifyListener;
import me.wondertwo.csu.net.NetConnectFactory;
import me.wondertwo.csu.net.NetRequestResult;
import me.wondertwo.csu.net.NetworkConstant;
import me.wondertwo.csu.net.ParseResult;
import me.wondertwo.csu.ui.drawer.DrawerFragment;
import me.wondertwo.csu.ui.skin.ChangeSkin;
import me.wondertwo.csu.ui.wifi.LoginSuccess;
import me.wondertwo.csu.utils.NetStateUtils;
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
    private EditText inputUserNumber; // 账户
    private EditText inputUserPassword; // 密码
    private ProgressBar loginLoadingProbar; // 进度条
    private DrawerLayout mDrawerLayout;
    private LinearLayout mSnackbarContainer;
    private DrawerFragment mDrawerFragment;

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
        loginLoadingProbar = (ProgressBar) findViewById(R.id.login_loading_probar);
        inputUserNumber = (EditText) findViewById(R.id.user_number);
        inputUserPassword = (EditText) findViewById(R.id.user_password);

        resettingUserInfo();
        checkWifiConnection();
    }

    /**
     * 登录按钮点击事件
     */
    public void onLoginButtonClicked(View view) {
        if (!checkWifiConnection()) { // 检查 wifi 连接状态
            return;
        }
        userNumber = inputUserNumber.getText().toString().trim();
        userPassword = inputUserPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userNumber) || TextUtils.isEmpty(userPassword)) { // 验证输入非空
            SnackBarUtils.makeShort(mSnackbarContainer, getResources().getString(R.string.textview_empty)).warning();
            return;
        }
        showProgressbar();
        AsyncTaskWrapper.getATWInstance().doAsyncWork(new Callable<Object>() {
            // 回调方法，在异步任务的doInBackground()方法中执行，发出网络请求，并返回请求结果
            @Override
            public Object call() throws Exception {
                saveUserLoginInfo();
                return NetConnectFactory.getNCFInstance(ContainerActivity.this).doLogin(userNumber, userPassword);
            }
        }, new NotifyListener() { // cancelListener,异步请求取消的监听,处理取消异常
            @Override
            public void onNotify(Object result) {

            }
        }, new NotifyListener() { // finishListener,异步请求结束的监听,处理请求结束逻辑
            @Override
            public void onNotify(Object result) { // 此处接收的result参数，即为网络请求结果
                dismissProgressbar();
                NetRequestResult response = (NetRequestResult) result;
                JSONObject entity = ParseResult.getResponseEntity(response);
                int resultCode = getResultCode(response);
                switch (resultCode) { // 0表示登陆成功，10表示密码简单，这两种情况都表示登陆成功
                    case 0:
                    case 10:
                        ToastUtils.showToastShort(ContainerActivity.this, R.string.login_success);
                        gotoShowResultActivity(entity.toString());
                        break;
                    default:
                        SnackBarUtils.makeLong(mSnackbarContainer, ParseResult.parseLoginCode(resultCode)).show();
                        break;
                }
            }
        });
    }

    /**
     * 已在线？尝试下线
     */
    public void onOnlineLogoutClicked(View view) {
        if (!checkWifiConnection()) { // 检查 wifi 连接状态
            return;
        }
        showProgressbar();
        Log.e("progress bar", "--------------");
        /*AsyncTaskWrapper.getATWInstance().doAsyncWork(new Callable<Object>() {
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
                Log.e("=================",result.toString());
                dismissProgressbar();
                NetRequestResult response = (NetRequestResult) result;
                SnackBarUtils.makeLong(mSnackbarContainer, ParseResult.parseLogoutCode(getResultCode(response))).show();
            }
        });*/

        AsyncTaskWrapper.getATWInstance().doAsyncWork(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String result = NetConnectFactory.getNCFInstance(ContainerActivity.this).doLogout();
                Log.e("callable", result);
                return result;
            }
        }, new NotifyListener() {
            @Override
            public void onNotify(Object result) {

            }
        }, new NotifyListener() {
            @Override
            public void onNotify(Object result) {
                dismissProgressbar();
                Log.e("---result---", result.toString());
                NetRequestResult ar = (NetRequestResult) result;
                //String json = (String) ar.getArgs()[0];
                if (!TextUtils.isEmpty(ar.getArgs()[0].toString())) {
                    try {

                        Object o = ar.getArgs()[0];
                        Log.e("ar.getArgs()[0]", o.toString());

                        JSONObject jsonObject = new JSONObject(ar.getArgs()[0].toString());
                        String[] arr = getResources().getStringArray(R.array.logout_result_code);
                        int resultCode;
                        if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < arr.length) {
                            switch (resultCode) {
                                case 0:
                                    ToastUtils.showToastShort(ContainerActivity.this, R.string.logout_success);
                                    break;
                                default:
                                    SnackBarUtils.makeLong(mSnackbarContainer, arr[resultCode]).warning();
                            }
                        } else
                            SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_failed)).warning();
                    } catch (JSONException e) {
                        SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_failed)).warning();
                    }
                } else {
                    SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.net_no_response)).warning();
                }
            }
        });
    }

    /**
     * 判断 WIFI 是否打开，未打开则弹出snack bar跳转到WIFI设置页面
     */
    private boolean checkWifiConnection() {
        if (!NetStateUtils.checkWifiConnection(this)) {
            Log.e("check wifi connection", "wifi is not connected");
            SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.not_connected_wifi))
                    .warning("去设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
                        }
                    });
            return false;
        }
        return true;
    }

    /**
     * 根据网络请求结果计算结果码
     *
     * @param result
     * @return
     */
    private int getResultCode(NetRequestResult result) {
        if (!TextUtils.isEmpty((String) result.getArgs()[0])) { // 有返回结果的情况
            try {
                JSONObject jsonObject = new JSONObject((String) result.getArgs()[0]);
                int resultCode;
                if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < 16) {
                    return resultCode;
                } else {
                    SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_failed)).show();
                }
            } catch (JSONException e) {
                SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.unknown_result_failed)).show();
            }
        } else {
            SnackBarUtils.makeLong(mSnackbarContainer, getString(R.string.net_no_response)).show();
        }
        return -1; // 表示无返回结果
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
        userNumber = MainApplication.getSpUtil().getValue(NetworkConstant.SP_USER_NUMBER, "");
        userPassword = MainApplication.getSpUtil().getValue(NetworkConstant.SP_USER_PASSWORD, "");
        inputUserNumber.setText(userNumber);
        inputUserPassword.setText(userPassword);
    }

    /**
     * saveUserLoginInfo()保存用户登录信息
     */
    private void saveUserLoginInfo() {
        userNumber = inputUserNumber.getText().toString().trim();
        userPassword = inputUserPassword.getText().toString().trim();
        MainApplication.getSpUtil().setValue(NetworkConstant.SP_USER_NUMBER, userNumber);
        MainApplication.getSpUtil().setValue(NetworkConstant.SP_USER_PASSWORD, userPassword);
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
