package com.wondertwo.csunetwork.ui.network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.adapter.ListviewAdapter;
import com.wondertwo.csunetwork.adapter.ViewHolder;
import com.wondertwo.csunetwork.async.AsyncTaskWarpper;
import com.wondertwo.csunetwork.async.AsyncWorkResult;
import com.wondertwo.csunetwork.async.listener.NotifyListener;
import com.wondertwo.csunetwork.newwork.NetConnectFactory;
import com.wondertwo.csunetwork.utils.ShowToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ShowResultActivity展示登录结果
 * Created by wondertwo on 2016/4/7.
 */
public class ShowResultActivity extends BaseNetworkActivity {

    private ListView lvLoginResult;
    private ProgressBar logoutWaitingProbar;
    public static final String INTRNT_EXTRA_NAME = "result";
    private JSONObject mJsonObject;
    private List<String> mlistData = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        lvLoginResult = (ListView) findViewById(R.id.lv_login_success);
        logoutWaitingProbar = (ProgressBar) findViewById(R.id.logout_waiting_probar);

        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            mJsonObject = new JSONObject(getIntent().getStringExtra(INTRNT_EXTRA_NAME));
            mlistData.add("账户总流量: " + mJsonObject.getInt("totalflow") + "MB");
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
                mlistData, R.layout.list_login_result_item) {
            @Override
            public void convert(ViewHolder helper, Object item) {
                helper.setText(R.id.list_login_result_item, (String) item);
            }
        });
    }

    /**
     * 下线方法和LoginActivity的下线方法有重复代码，下期重构掉或者统一下线入口
     */
    public void onLogoutBtnClicked(View view) {
        showProgressbar();
        AsyncTaskWarpper.getATWInstance().doAsyncWork(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return NetConnectFactory.getInstance(ShowResultActivity.this).doLogout();
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
                                    ShowToastUtils.showToastLong(ShowResultActivity.this, R.string.logout_success);
                                    gotoLoginActivity();
                                    break;
                                default:
                                    showError(arr[resultCode]);
                            }
                        } else
                            showLoginUnknowError();
                    } catch (JSONException e) {
                        showLoginUnknowError();
                    }
                } else {
                    showLogoutUnknowError();
                }
            }
        });
    }

    /**
     * “关于”文本框点击事件
     */
    public void onAboutClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(R.string.about_learn_more)
                .setPositiveButton(R.string.goto_author_blog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openBrowser("http://www.cnblogs.com/wondertwo/");
                    }
                })
                .setNegativeButton(R.string.goto_app_github, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openBrowser("https://github.com/wondertwo/CSUNetwork/");
                    }
                }).show();
    }

    /**
     * 隐藏显现等待进度条
     */
    private void dismissProgressbar() {
        if (logoutWaitingProbar != null && logoutWaitingProbar.isShown()) {
            logoutWaitingProbar.setVisibility(View.GONE);
        }
    }

    /**
     * 展示显现等待进度条
     */
    private void showProgressbar() {
        logoutWaitingProbar.setVisibility(View.VISIBLE);
    }

    /**
     * 跳转到LoginActivity
     */
    private void gotoLoginActivity() {
        Intent intent = new Intent(ShowResultActivity.this, LoginActivity.class);
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
}
