package com.wondertwo.csu.ui.wifi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wondertwo.csu.R;
import com.wondertwo.csu.adapter.ListviewAdapter;
import com.wondertwo.csu.adapter.ViewHolder;
import com.wondertwo.csu.async.AsyncTaskWarpper;
import com.wondertwo.csu.async.AsyncWorkResult;
import com.wondertwo.csu.async.listener.NotifyListener;
import com.wondertwo.csu.net.NetConnectFactory;
import com.wondertwo.csu.ui.ContainerActivity;
import com.wondertwo.csu.ui.TopLevelActivity;
import com.wondertwo.csu.utils.statusbar.StatusBarUtils;
import com.wondertwo.csu.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 *
 * Created by wondertwo on 2016/6/11.
 */
public class LoginSuccess extends TopLevelActivity {

    private ListView lvLoginResult; // 登录结果展示
    private ProgressBar logoutWaitingProbar; // 下线等待进度条
    public static final String INTRNT_EXTRA_NAME = "result";
    private JSONObject mJsonObject;
    private List<String> mlistData = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success);
        // match the status bar color
        StatusBarUtils.setColor(this, Color.parseColor(getString(R.string.container_bar_color)), 0);

        lvLoginResult = (ListView) findViewById(R.id.lv_login_success);
        logoutWaitingProbar = (ProgressBar) findViewById(R.id.logout_waiting_probar);
        ImageButton mLeftButton = (ImageButton) findViewById(R.id.login_success_left);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginPageActivity();
            }
        });

        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            mJsonObject = new JSONObject(getIntent().getStringExtra(INTRNT_EXTRA_NAME));
            mlistData.add("账户流量: " + mJsonObject.getInt("totalflow") + "MB");
            mlistData.add("已用流量: " + mJsonObject.getInt("usedflow") + "MB");
            mlistData.add("剩余流量: " + mJsonObject.getInt("surplusflow") + "MB");
            mlistData.add("剩余金额: " + mJsonObject.getDouble("surplusmoney") + "元");
            mlistData.add("当前IP地址: " + mJsonObject.getString("userIntranetAddress"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        lvLoginResult.setAdapter(new ListviewAdapter(getApplicationContext(),
                mlistData, R.layout.listview_item) {
            @Override
            public void convert(ViewHolder helper, Object item) {
                helper.setText(R.id.list_login_result_item, (String) item);
            }
        });
    }

    /**
     * 下线方法和LoginActivity的下线方法有重复代码，下期重构掉或者统一下线入口
     */
    public void onLogoutButtonClicked(View view) {
        showProgressbar();
        AsyncTaskWarpper.getATWInstance().doAsyncWork(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return NetConnectFactory.getNCFInstance(LoginSuccess.this).doLogout();
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
                String json = (String) ar.getArgs()[0];
                if (!TextUtils.isEmpty(ar.getArgs()[0].toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject((String) ar.getArgs()[0]);
                        String[] arr = getResources().getStringArray(R.array.logout_tip);
                        int resultCode = -1;
                        if (jsonObject.has("resultCode") && (resultCode = jsonObject.getInt("resultCode")) < arr.length) {
                            switch (resultCode) {
                                case 0:
                                    ToastUtils.showToastLong(LoginSuccess.this, R.string.logout_success);
                                    gotoLoginPageActivity();
                                    break;
                                default:
                                    showAlertDialog(arr[resultCode]);
                            }
                        } else
                            showAlertDialog(R.string.unknow_result_logout_failed);
                    } catch (JSONException e) {
                        showAlertDialog(R.string.unknow_result_logout_failed);
                    }
                } else {
                    showAlertDialog(R.string.unknow_result_logout_failed);
                }
            }
        });
    }

    /**
     * “关于app”图片按钮点击事件
     */
    public void onAppAboutIconClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(R.string.about_learn_more)
                .setPositiveButton(R.string.goto_app_introduce, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openBrowser("http://www.cnblogs.com/wondertwo/p/5392496.html");
                    }
                })
                .setNegativeButton(R.string.goto_author_blog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openBrowser("http://www.cnblogs.com/wondertwo/");
                    }
                }).show();
    }

    /**
     * 隐藏显现等待进度条
     */
    private void dismissProgressbar() {
        if (logoutWaitingProbar != null && logoutWaitingProbar.isShown()) {
            logoutWaitingProbar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 展示显现等待进度条
     */
    private void showProgressbar() {
        logoutWaitingProbar.setVisibility(View.VISIBLE);
    }

    /**
     * 跳转到LoginPageActivity
     */
    private void gotoLoginPageActivity() {
        Intent intent = new Intent(LoginSuccess.this, ContainerActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 打开系统浏览器访问链接
     */
    private void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
