package com.wondertwo.csunetwork.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.wondertwo.csunetwork.R;
import com.wondertwo.csunetwork.ui.contentpager.BaseContentPager;
import com.wondertwo.csunetwork.ui.contentpager.NewsPager;
import com.wondertwo.csunetwork.ui.contentpager.NetworkPager;

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
    // 集合mPagerList，接收BaseContentPager对象
    private ArrayList<BaseContentPager> mPagerList;
    // ContentFragment的布局
    private View contentView;

    /**
     * 初始化Fragment的布局
     */
    @Override
    public View initView() {
        rgBottomBar = (RadioGroup) contentView.findViewById(R.id.rg_bottom_tab);// 拿到RadioGroup对象
        mViewPager = (ViewPager) contentView.findViewById(R.id.vp_content);// 拿到ViewPager对象
        return contentView;
    }

    /**
     * Fragmnet依附的activity创建，初始化数据
     */
    @Override
    public void initData() {
        // 在初始化数据时，首先加载ContentFragment布局
        contentView = View.inflate(mActivity, R.layout.fragment_content_page, null);

        rgBottomBar.check(R.id.rb_net);// 表示默认打开进入wifi登录页

        mPagerList = new ArrayList<>();
        mPagerList.add(new NetworkPager(mActivity));
        mPagerList.add(new NewsPager(mActivity));

        // ViewPager设置适配器
        mViewPager.setAdapter(new ContentPagerAdapter());

        // 监听RadioGroup的选择事件
        rgBottomBar.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_net:
                                mViewPager.setCurrentItem(0, false);// 设置当前页面
                                break;
                            case R.id.rb_news:
                                mViewPager.setCurrentItem(1, false);// 设置当前页面
                                break;
                            default:
                                break;
                        }
                    }
                }
        );

        // 默认初始化新闻页数据
        getNewsPager().initData();

        // 设置ViewPager的页面变化监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // 当页面被选中，初始化页面数据
                mPagerList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    // 获取新闻页对象,并初始化新闻页数据
    public NetworkPager getNewsPager() {
        return (NetworkPager) mPagerList.get(0);
    }

    /**
     * ViewPager适配器
     */
    class ContentPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) { return view == object; }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseContentPager basePager = mPagerList.get(position);
            container.addView(basePager.mRootView);
            // basePager.initData();// 注意不能在这里initData(),否则会预加载一个页面
            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
