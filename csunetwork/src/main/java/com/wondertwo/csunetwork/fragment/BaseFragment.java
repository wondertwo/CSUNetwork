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

    // 内容区fragment所依附的context对象activity
    public Activity mActivity;

    /**
     * fragment被创建，其次执行，获取fragment所依附的activity对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    /**
     * 创建fragment的布局view，最后执行
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 创建fragment所依附Activity，最先执行
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化view方法initView()
     */
    public View initView() { return null; }

    /**
     * 初始化数据initData()
     */
    public void initData() {}
}
