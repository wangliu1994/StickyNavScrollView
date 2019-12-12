package com.winnie.library.stickynav.refresh;

/**
 *
 * @author winnie
 * @date 2017/5/19
 */
public enum Mode {

    /**
     *禁用下拉刷新
     */
    DISABLED(0x0),

    /**
     *下拉刷新
     */
    PULL_FROM_START(0x1),

    /**
     * 调用 setRefreshing()方法
     * {@link com.winnie.library.stickynav.layout.StickyPullToRefreshLayout#setRefreshing()}方法
     */
    MANUAL_REFRESH_ONLY(0x4);


    public static Mode mapIntToValue(final int modeInt) {
        for (Mode value : Mode.values()) {
            if (modeInt == value.getIntValue()) {
                return value;
            }
        }
        return getDefault();
    }

    public static Mode getDefault() {
        return PULL_FROM_START;
    }

    private int mIntValue;

    Mode(int modeInt) {
        mIntValue = modeInt;
    }

    /**
     * @return true 允许下拉刷新
     */
    public boolean permitsPullToRefresh() {
        return !(this == DISABLED || this == MANUAL_REFRESH_ONLY);
    }

    /**
     * @return true 可以下拉刷新
     */
    public boolean showHeaderLoadingLayout() {
        return this == PULL_FROM_START;
    }

    int getIntValue() {
        return mIntValue;
    }
}
