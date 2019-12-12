package com.winnie.stickynavscrollview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.winnie.library.stickynav.base.StickyNavScrollBaseLayout;
import com.winnie.library.stickynav.base.StickyPullToRefreshBaseLayout;
import com.winnie.library.stickynav.refresh.Mode;
import com.winnie.library.stickynav.refresh.State;
import com.winnie.library.stickynav.view.StickyNavScrollView;
import com.winnie.library.stickynav.view.StickyPullToRefreshView;
import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.fragment.TabFragment;


/**
 * Created by winnie on 2017/5/5.
 */

public class StickyNavPullToRefreshViewActivity extends AppCompatActivity {

    private String[] mTitles = new String[] { "简介", "评价", "相关" };
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private Fragment[] mFragments = new Fragment[mTitles.length];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav_pull_to_refresh_view);

        final StickyPullToRefreshView mPullView = (StickyPullToRefreshView) findViewById(R.id.sticky_nav_pull_layout);
        StickyNavScrollView mScrollView = mPullView.getRefreshableView();
        mViewPager = (ViewPager) mScrollView.getTabView();

        mPullView.setOnRefreshListener(new StickyPullToRefreshBaseLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      mPullView.onRefreshComplete();
                    }
                }, 3000);
        }
        });

        mPullView.setOnPullDownListener(new StickyPullToRefreshBaseLayout.OnPullDownListener() {
            @Override
            public void onPullDown(int distance) {

            }
        });

        mPullView.setOnPullEventListener(new StickyPullToRefreshBaseLayout.OnPullEventListener() {
            @Override
            public void onPullEvent(State state, Mode direction) {

            }
        });

        mPullView.getRefreshableView().setScrollViewListener(new StickyNavScrollBaseLayout.ScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {

            }
        });

        initDatas();

    }

    private void initDatas()
    {

        for (int i = 0; i < mTitles.length; i++)
        {
            mFragments[i] = TabFragment.newInstance(mTitles[i]);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
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
