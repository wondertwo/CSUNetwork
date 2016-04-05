package com.wondertwo.csunetwork.fragment;

import android.view.View;

import com.wondertwo.csunetwork.R;

/**
 * MenuFra侧滑菜单栏fragment
 * Created by wondertwo on 2016/4/4.
 */
public class LeftMenuFra extends BaseFragment {

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        return view;
    }
}
