package com.winnie.library.stickynav.refresh;

/**
 * Created by winnie on 2017/5/19.
 */

public enum State {

    /**
     *初始状态
     */
    RESET(0x0),

    /**
     * 下拉中，下拉的距离还没达到刷新要求的值
     */
    PULL_TO_REFRESH(0x1),

    /**
     *下拉中，下拉的距离达到刷新要求的值，松开手既可以刷新
     */
    RELEASE_TO_REFRESH(0x2),

    /**
     * 正在刷新
     */
    REFRESHING(0x8),

    /**
     * 调用 setRefreshing()方法
     * {@link com.winnie.stickynav.layout.StickyPullToRefreshLayout#setRefreshing()}方法
     */
    MANUAL_REFRESHING(0x9),

    /**
     * RefreshableView 调用fling()，，过度滚动
     */
    OVERSCROLLING(0x10);


    public static State mapIntToValue(final int stateInt) {
        for (State value : State.values()) {
            if (stateInt == value.getIntValue()) {
                return value;
            }
        }
        return RESET;
    }

    public static State getDefault() {
        return RESET;
    }

    private int mIntValue;

    State(int intValue) {
        mIntValue = intValue;
    }

    int getIntValue() {
        return mIntValue;
    }
}
