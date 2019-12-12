package com.winnie.stickynavscrollview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.winnie.library.stickynav.layout.StickyNavScrollLayout;
import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.fragment.TabFragment;


/**
 *
 * @author winnie
 * @date 2017/5/5
 */
public class StickyNavScrollLayoutActivity2 extends AppCompatActivity {

    private String[] mTitles = new String[] { "简介", "评价", "相关" };
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private Fragment[] mFragments = new Fragment[mTitles.length];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav_scroll_layout2);

        FrameLayout layout = findViewById(R.id.sticky_nav_layout);

        StickyNavScrollLayout mScrollView = new StickyNavScrollLayout(this);
        View mHeadView = LayoutInflater.from(this).inflate(R.layout.view_sticky_nav_head, null);
        mHeadView.setId(R.id.sticky_nav_head_view);
        View mTabBar = LayoutInflater.from(this).inflate(R.layout.view_sticky_tab_bar, null);
        mTabBar.setId(R.id.sticky_nav_tab_bar);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.sticky_nav_tab_view);

        mScrollView.addView(mHeadView);
        mScrollView.addView(mTabBar);
        mScrollView.addView(mViewPager);
        layout.addView(mScrollView);

//        mScrollView.setStickyNavTopMargin(200);

        initData();
    }

    private void initData() {
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = TabFragment.newInstance(mTitles[i]);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount()
            {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position)
            {
                return mFragments[position];
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }
}
