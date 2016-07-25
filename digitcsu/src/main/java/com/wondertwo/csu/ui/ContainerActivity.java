package com.wondertwo.csu.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.wondertwo.csu.R;
import com.wondertwo.csu.async.AsyncTaskWarpper;
import com.wondertwo.csu.async.AsyncWorkResult;
import com.wondertwo.csu.async.listener.NotifyListener;
import com.wondertwo.csu.constant.NetworkConstant;
import com.wondertwo.csu.net.NetConnectFactory;
import com.wondertwo.csu.ui.drawer.DrawerFragment;
import com.wondertwo.csu.ui.wifi.LoginSuccess;
import com.wondertwo.csu.ui.skin.ChangeSkin;
import com.wondertwo.csu.utils.NetStateUtils;
import com.wondertwo.csu.utils.PreferUtils;
import com.wondertwo.csu.utils.ToastUtils;
import com.wondertwo.csu.utils.statusbar.StatusBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

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
                return NetConnectFactory.getNCFInstance(ContainerActivity.this).doLogin(userNumber, userPassword);
            }
        }, new NotifyListener() {
            @Override
            public void onNotify(Object result) {
                ToastUtils.showToastShort(ContainerActivity.this, "取消");
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
                                    ToastUtils.showToastShort(ContainerActivity.this, R.string.login_success);
                                    gotoShowResultActivity(jsonObject.toString());
                                    break;
                                case 1:
                                    if (jsonObject.has("resultDescribe")) {
                                        showAlertDialog(jsonObject.getString("resultDescribe"));
                                    }
                                    break;
                                default:
                                    showAlertDialog(arr[resultCode]);
                            }
                        } else {
                            showAlertDialog(R.string.unknow_result_login_failed);
                        }
                    } catch (JSONException e) {
                        showAlertDialog(R.string.unknow_result_login_failed);
                    }
                } else {
                    showAlertDialog(R.string.unknow_result_login_failed);
                }
            }
        });
    }

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
                AsyncWorkResult ar = (AsyncWorkResult) result;
                if (!TextUtils.isEmpty(ar.getArgs()[0].toString())) {
                    if ((ar.getArgs()[0]) instanceof Exception) {
                        showAlertDialog(R.string.unknow_result_logout_failed);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(ar.getArgs()[0].toString());
                            String[] arr = getResources().getStringArray(R.array.logout_tip);
                            int resultCode = -1;
                            if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < arr.length) {
                                switch (resultCode) {
                                    case 0:
                                        ToastUtils.showToastShort(ContainerActivity.this, R.string.logout_success);
                                        break;
                                    default:
                                        showAlertDialog(arr[resultCode]);
                                }
                            } else
                                showAlertDialog(R.string.unknow_result_logout_failed);
                        } catch (JSONException e) {
                            showAlertDialog(R.string.unknow_result_logout_failed);
                        }
                    }
                } else {
                    showAlertDialog(R.string.unknow_result_logout_failed);
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

    /*private void showCompleteDialog(String string) {
        DialogPlus dialog = DialogUtil.createCompleteDialog(ContainerActivity.this,
                DialogUtil.holder,
                Gravity.CENTER,
                new SimpleAdapter(ContainerActivity.this),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {

                    }
                }, new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                    }
                }, new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                }, new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {

                    }
                });
        dialog.show();
    }*/

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
