package com.winnie.stickynavscrollview.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.winnie.library.stickynav.base.IStickyTabViewListener;


/**
 * Created by winnie on 2017/6/22.
 */

public class StickyTabViewPager extends ViewPager implements IStickyTabViewListener {

    public StickyTabViewPager(Context context) {
        super(context);
    }

    public StickyTabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTag(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean isScrollToTop() {
        Fragment fragment = ((FragmentPagerAdapter)getAdapter()).getItem(getCurrentItem());
//        View fragment = findViewWithTag(getCurrentItem());
        if(fragment instanceof IStickyTabViewListener) {
            return ((IStickyTabViewListener) fragment).isScrollToTop();
        }
        return true;
    }
}
