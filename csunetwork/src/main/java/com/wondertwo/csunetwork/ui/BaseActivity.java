package com.wondertwo.csunetwork.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Activity基类
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
