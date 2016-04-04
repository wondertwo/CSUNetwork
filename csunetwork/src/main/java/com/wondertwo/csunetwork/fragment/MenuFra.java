package com.wondertwo.csunetwork.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.wondertwo.csunetwork.R;

import java.util.ArrayList;

/**
 * MenuFra侧滑菜单栏fragment
 * Created by wondertwo on 2016/4/4.
 */
public class MenuFra extends BaseFragment {

    // 底部RadioButtonGroup导航栏
    private RadioGroup rgBottomBar;
    // 拿到ViewPager对象
    private ViewPager mViewPager;
    // 定义集合mPagerList，接收BasePager对象
    private ArrayList<BaseFragment> mPagerList;

    @Override
    public View initView() {
        View view  = View.inflate(mActivity, R.layout.fragment_content, null);

        rgBottomBar = (RadioGroup) view.findViewById(R.id.rg_bottom_tab);// 拿到RadioGroup对象
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);// 拿到ViewPager对象

        return view;
    }
}
