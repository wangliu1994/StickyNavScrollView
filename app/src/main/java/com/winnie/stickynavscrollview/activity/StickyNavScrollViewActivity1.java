package com.winnie.stickynavscrollview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.winnie.library.stickynav.view.StickyNavScrollView;
import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.fragment.TabFragment;
import com.winnie.stickynavscrollview.view.StickyTabViewPager;


/**
 * Created by winnie on 2017/5/5.
 */

public class StickyNavScrollViewActivity1 extends AppCompatActivity {

    private String[] mTitles = new String[] { "简介", "评价", "相关" };
    private StickyTabViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private Fragment[] mFragments = new Fragment[mTitles.length];

    private StickyNavScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav_scroll_view1);

        mScrollView = (StickyNavScrollView) findViewById(R.id.sticky_nav_layout);
        mViewPager = (StickyTabViewPager) mScrollView.getTabView();
        mViewPager.setTag("haha");
        View view = mScrollView.findViewWithTag("haha");

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

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
//                ((View)object).setTag(position);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setTag(mAdapter.getItem(position));
                View view= mViewPager.findViewWithTag(mAdapter.getItem(position));
                if(!mScrollView.isNavSticky()){
                    ((TabFragment)mFragments[position]).scrollToTop();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
