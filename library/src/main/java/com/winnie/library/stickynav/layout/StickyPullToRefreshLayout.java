package com.winnie.library.stickynav.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.winnie.library.stickynav.base.StickyPullToRefreshBaseLayout;


/**
 * Created by winnie on 2017/5/19.
 * 带悬浮吸顶的下拉刷新控件
 */

public class StickyPullToRefreshLayout extends StickyPullToRefreshBaseLayout<StickyNavScrollLayout> {

    public StickyPullToRefreshLayout(Context context) {
        super(context);
    }

    public StickyPullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected StickyNavScrollLayout createRefreshableView(Context context, TypedArray typedArray) {
        StickyNavScrollLayout refreshView = new StickyNavScrollLayout(context, typedArray);
        return refreshView;
    }
}
