package com.winnie.library.stickynav.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.winnie.library.R;
import com.winnie.library.stickynav.base.StickyNavScrollBaseLayout;
import com.winnie.library.stickynav.layout.StickyNestScrollChildLayout;

import java.util.ArrayList;

/**
 * Created by winnie on 2017/5/2.
 * 通过嵌套滑动实现的悬浮吸顶菜单栏通用控件
 */

public class StickyNavScrollView extends StickyNavScrollBaseLayout {

    private static final String TAG = StickyNavScrollView.class.getSimpleName();

    private StickyNestScrollChildLayout mNestedScrollChildLayout;

    private LinearLayout mStickyHeadViewLayout;//头部

    private FrameLayout mStickyTabBarLayout;//悬浮部

    private FrameLayout mStickyTabViewLayout;//底部


    public StickyNavScrollView(Context context) {
        super(context);
    }

    public StickyNavScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyNavScrollView(Context context, TypedArray typedArray) {
        super(context, typedArray);
    }

    @Override
    protected void initView(Context context, TypedArray typedArray){
        LayoutInflater.from(context).inflate(R.layout.sticky_nav_scrollview ,this);

        mStickyHeadViewLayout = (LinearLayout) findViewById(R.id.sticky_nav_head_view_layout);
        mStickyTabBarLayout = (FrameLayout) findViewById(R.id.sticky_nav_tab_bar_layout);
        mStickyTabViewLayout = (FrameLayout) findViewById(R.id.sticky_nav_tab_view_layout);

//        mStickyHeadViewLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        mStickyTabBarLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        if(typedArray != null) {
            final int headerID = typedArray.getResourceId(R.styleable.StickyNav_navHeadView, -1);
            final int tabBarID = typedArray.getResourceId(R.styleable.StickyNav_navTabBar, -1);
            final int tabViewID = typedArray.getResourceId(R.styleable.StickyNav_navTabView, -1);

            if (headerID != -1) {
                View headView = LayoutInflater.from(context).inflate(headerID, null);
                setHeadView(headView);
            }
            if (tabBarID != -1) {
                View tabBar = LayoutInflater.from(context).inflate(tabBarID, null);
                setTabBar(tabBar);
            }
            if (tabViewID != -1) {
                View tabView = LayoutInflater.from(context).inflate(tabViewID, null);
                setTabView(tabView);
            }
        }
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

    /*--------------------------内部设置了布局，不允许再当做容器使用---------------------------------*/
    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StickyNavScrollView can not host any more child");
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.addView(child, -1, params);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StickyNavScrollView can not host any more child");
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StickyNavScrollView can not host any more child");
        }

        super.addView(child, -1, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("StickyNavScrollView can not host any more child");
        }

        super.addView(child, index, params);
    }
    /*----------------------------添加头部布局---------------------------------*/
    public void setHeadView(View headView) {
        mStickyHeadViewLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mStickyHeadViewLayout.addView(headView, params);
    }

    public View getHeadView(){
        return mStickyHeadViewLayout.getChildAt(0);
    }

    public void addHeadView(View mHeadView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mStickyHeadViewLayout.addView(mHeadView, params);
    }

    public void removeHeadView(View mHeadView) {
        mStickyHeadViewLayout.removeView(mHeadView);
    }

    public void addHeadViews(ArrayList<View> mHeadViews) {
        for(View v:mHeadViews){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mStickyHeadViewLayout.addView(v, params);
        }
    }

    public void removeHeadViews(ArrayList<View> mHeadViews) {
        for(View v:mHeadViews){
            mStickyHeadViewLayout.removeView(v);
        }
    }


    public void setTabBar(View tabBar) {
        mStickyTabBarLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mStickyTabBarLayout.addView(tabBar, params);
    }

    public void removeTabBar() {
        mStickyTabBarLayout.removeAllViews();
    }

    public View getTabBar(){
        return mStickyTabBarLayout.getChildAt(0);
    }

    public void setTabView(View tabView) {
        mStickyTabViewLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mStickyTabViewLayout.addView(tabView, params);
    }

    public View getTabView(){
        return mStickyTabViewLayout.getChildAt(0);
    }

    public void removeTabView() {
        mStickyTabViewLayout.removeAllViews();
    }
}
