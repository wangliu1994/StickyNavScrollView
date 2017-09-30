package com.winnie.library.stickynav.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.winnie.library.R;
import com.winnie.library.stickynav.base.StickyNavScrollBaseLayout;


/**
 * Created by winnie on 2017/5/2.
 * 通过嵌套滑动实现的悬浮吸顶菜单栏通用控件
 */

public class StickyNavScrollLayout extends StickyNavScrollBaseLayout implements NestedScrollingParent {

    private static final String TAG = StickyNavScrollLayout.class.getSimpleName();

    private StickyNestScrollChildLayout mNestedScrollChildLayout;

    private LinearLayout mStickyHeadViewLayout;//头部

    private FrameLayout mStickyTabBarLayout;//悬浮部

    private FrameLayout mStickyTabViewLayout;//底部

//    private View mStickyHeadView;//头部
//
//    private View mStickyTabBar;//悬浮部
//
//    private View mStickyTabView;//底部

    /*-------------------构造函数------------------------*/
    public StickyNavScrollLayout(Context context) {
        super(context);
    }

    public StickyNavScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyNavScrollLayout(Context context, TypedArray typedArray) {
        super(context, typedArray);
    }

    @Override
    protected void initView(Context context, TypedArray typedArray) {
        LayoutInflater.from(context).inflate(R.layout.sticky_nav_scrollview, this);
        mStickyHeadViewLayout = (LinearLayout) findViewById(R.id.sticky_nav_head_view_layout);
        mStickyTabBarLayout = (FrameLayout) findViewById(R.id.sticky_nav_tab_bar_layout);
        mStickyTabViewLayout = (FrameLayout) findViewById(R.id.sticky_nav_tab_view_layout);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        mStickyHeadView = findViewById(R.id.sticky_nav_head_view);
//        mStickyTabBar = findViewById(R.id.sticky_nav_tab_bar);
//        mStickyTabView = findViewById(R.id.sticky_nav_tab_view);
    }


    @Override
    protected ViewGroup getStickyHeadView() {
        return mStickyHeadViewLayout;
    }

    @Override
    protected ViewGroup getStickyTabBar() {
        return mStickyTabBarLayout;
    }

    @Override
    protected ViewGroup getStickyTabView() {
        return mStickyTabViewLayout;
    }

    /*--------------------------内部设置了布局，要更改添加进来的布局---------------------------------*/
    @Override
    public void addView(View child) {
        int id = child.getId();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (id == R.id.sticky_root_view) {
            addViewInternal(child, params);
        } else {
            addChildView(child, params);
        }
    }

    @Override
    public void addView(View child, int index) {
        int id = child.getId();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (id == R.id.sticky_root_view) {
            addViewInternal(child, index, params);
        } else {
            addChildView(child, params);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        int id = child.getId();
        if (id == R.id.sticky_root_view) {
            addViewInternal(child, params);
        } else {
            addChildView(child, params);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int id = child.getId();
        if (id == R.id.sticky_root_view) {
            addViewInternal(child, index, params);
        } else {
            addChildView(child, params);
        }
    }

    /**
     * 使用addViewInternal()是因为 addView()被我重载了
     */
    private final void addViewInternal(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    private final void addViewInternal(View child, ViewGroup.LayoutParams params) {
        super.addView(child, -1, params);
    }

    private void addChildView(View child, ViewGroup.LayoutParams params) {
        int id = child.getId();
        if (id == R.id.sticky_nav_head_view) {
            mStickyHeadViewLayout.addView(child, params);
        } else if (id == R.id.sticky_nav_tab_bar) {
            mStickyTabBarLayout.addView(child, params);
        } else if (id == R.id.sticky_nav_tab_view) {
            mStickyTabViewLayout.addView(child, params);
        } else {
            mStickyHeadViewLayout.addView(child, params);
        }
    }
}
