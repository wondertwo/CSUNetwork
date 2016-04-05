package com.wondertwo.csunetwork.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseFragment是所有fragment的基类
 * Created by wondertwo on 2016/4/3.
 */
public abstract class BaseFragment extends Fragment {

    // fragment所依附的activity对象
    public Activity mActivity;

    /**
     * fragment创建，获取fragment所依附的activity对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    /**
     * 创建fragment的布局view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 创建fragment所依附Activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化view抽象方法
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public void initData() {}
}
