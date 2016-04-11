package com.wondertwo.csunetwork.ui.wechat;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.wondertwo.csunetwork.R;

/**
 * Created by wondertwo on 2016/4/10.
 */
public class WechatListActivity extends BaseWechatActivity {

    private WebView wechatList;
    private String CSU_WECHAT_LIST = "http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzA5NTI2MzExNA==#wechat_webview_type=1&wechat_redirect";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_list);

        wechatList = (WebView) findViewById(R.id.wv_wechat_list);
        wechatList.getSettings().setJavaScriptEnabled(true);
        wechatList.setWebChromeClient(new WebChromeClient());
        wechatList.loadUrl(CSU_WECHAT_LIST);
    }
}
