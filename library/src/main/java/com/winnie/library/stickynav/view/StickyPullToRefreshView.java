package com.winnie.library.stickynav.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.winnie.library.stickynav.base.StickyPullToRefreshBaseLayout;


/**
 *
 * @author winnie
 * @date 2017/5/19
 * 带悬浮吸顶的下拉刷新控件
 */
public class StickyPullToRefreshView extends StickyPullToRefreshBaseLayout<StickyNavScrollView> {

    public StickyPullToRefreshView(Context context) {
        super(context);
    }

    public StickyPullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected StickyNavScrollView createRefreshableView(Context context, TypedArray typedArray) {
        return new StickyNavScrollView(context, typedArray);
    }
}
