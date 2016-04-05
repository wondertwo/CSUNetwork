package com.wondertwo.csunetwork.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.wondertwo.csunetwork.R;

import java.util.ArrayList;

/**
 * ContentFra显示主页面内容fragment
 * Created by wondertwo on 2016/4/4.
 */
public class ContentFra extends BaseFragment {

    // 底部RadioButtonGroup导航栏
    private RadioGroup rgBottomBar;
    // ViewPager对象
    private ViewPager mViewPager;
    // 集合mPagerList，接收BasePager对象
    private ArrayList<BaseFragment> mPagerList;

    @Override
    public View initView() {
        // 加载ContentFra布局文件
        View view  = View.inflate(mActivity, R.layout.fragment_content, null);

        rgBottomBar = (RadioGroup) view.findViewById(R.id.rg_bottom_tab);// 拿到RadioGroup对象
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);// 拿到ViewPager对象

        return view;
    }

    /**
     * 初始化contentfragment数据
     */
    @Override
    public void initData() {
        rgBottomBar.check(R.id.rb_net);// 表示默认进入页面是wifi登录页

        mPagerList = new ArrayList<>();


        super.initData();
    }
}
