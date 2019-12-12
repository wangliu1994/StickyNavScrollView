package com.winnie.library.stickynav.base;

/**
 *
 * @author winnie
 * @date 2017/6/22
 * 悬浮吸顶底部列表实现该接口，用于判断是否滑动到列表顶部第一个Item处
 */

public interface IStickyTabViewListener {
    /**
     * 已经滑动到顶部
     * @return 已经滑动到顶部
     */
    boolean isScrollToTop();
}


