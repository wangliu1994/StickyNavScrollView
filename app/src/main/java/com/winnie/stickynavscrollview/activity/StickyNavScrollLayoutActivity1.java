package com.winnie.stickynavscrollview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.winnie.library.stickynav.layout.StickyNavScrollLayout;
import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.fragment.TabFragment;


/**
 *
 * @author winnie
 * @date 2017/5/5
 */
public class StickyNavScrollLayoutActivity1 extends AppCompatActivity {

    private String[] mTitles = new String[]{"简介", "评价", "相关"};
    private LinearLayout mHeaderView;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private Fragment[] mFragments = new Fragment[mTitles.length];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav_scroll_layout1);

        final StickyNavScrollLayout mScrollView = findViewById(R.id.sticky_nav_layout);
        mHeaderView = findViewById(R.id.sticky_nav_head_view);
        mViewPager = findViewById(R.id.sticky_nav_tab_view);
        initData();

        mScrollView.postDelayed(() -> mScrollView.directlyScrollTo(0, 500), 2000);
    }

    private void initData() {
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = TabFragment.newInstance(mTitles[i]);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mHeaderView.postDelayed(() -> mHeaderView.addView(
                new View(
                        StickyNavScrollLayoutActivity1.this),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)), 5000);
    }

    public void image(View view) {
    }
}
