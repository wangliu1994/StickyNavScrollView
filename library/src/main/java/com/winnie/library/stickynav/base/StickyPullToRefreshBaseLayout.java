package com.winnie.library.stickynav.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.winnie.library.R;
import com.winnie.library.stickynav.refresh.Mode;
import com.winnie.library.stickynav.refresh.State;
import com.winnie.library.stickynav.refresh.StickyPullToRefreshHeader;
import com.winnie.library.stickynav.refresh.StickyPullToRefreshRotateHeader;


/**
 *
 * @author winnie
 * @date 2017/5/19
 * 带悬浮吸顶的下拉刷新容器基类
 */
public abstract class StickyPullToRefreshBaseLayout
        <T extends StickyNavScrollBaseLayout> extends LinearLayout {

    static final float FRICTION = 2.0f;

    private StickyPullToRefreshHeader mLoadingView;
    private T mRefreshableView;


    private int mTouchSlop;
    private float mLastMotionX, mLastMotionY;
    private float mInitialMotionX, mInitialMotionY;

    private boolean mIsBeingDragged = false;
    private State mState = State.getDefault();
    private Mode mMode = Mode.getDefault();

    private boolean mShowViewWhileRefreshing = true;
    private boolean mScrollingWhileRefreshingEnabled = false;
    private boolean mLayoutVisibilityChangesEnabled = true;
    private boolean mFilterTouchEvents = true;

    private Interpolator mScrollAnimationInterpolator;

    private OnRefreshListener mOnRefreshListener;
    private OnPullEventListener mOnPullEventListener;
    private OnPullDownListener mDownListener;

    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;

    public StickyPullToRefreshBaseLayout(Context context) {
        super(context);
        init(context, null);
    }

    public StickyPullToRefreshBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int height = 0;
//        int childCount = getChildCount();
//
//        for(int i= 0; i< childCount; i++){
//            height += getChildAt(i).getMeasuredHeight();
//        }
//        setMeasuredDimension(getMeasuredWidth(), height);
//    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //把headerView隐藏起来，其实用的是padding的方式 设置为负值 就到屏幕顶部的外面了
        post(new Runnable() {
            @Override
            public void run() {
                refreshLoadingViewsSize();
                requestLayout();
            }
        });
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        mRefreshableView.addView(child, index, params);
    }


    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);

        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        TypedArray stickyArray = context.obtainStyledAttributes(attrs, R.styleable.StickyNav);
        mRefreshableView = createRefreshableView(context, stickyArray);
        stickyArray.recycle();
        addViewInternal(mRefreshableView,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TypedArray refreshArray = context.obtainStyledAttributes(attrs, R.styleable.StickyPullToRefresh);
        mLoadingView = createLoadingView(context, refreshArray);
        refreshArray.recycle();

        updateUIForMode();
    }

    protected abstract T createRefreshableView(Context context, TypedArray typedArray);


    private StickyPullToRefreshHeader createLoadingView(Context context, TypedArray typedArray) {
        StickyPullToRefreshHeader loadingView = new StickyPullToRefreshRotateHeader(context, typedArray);
        loadingView.setVisibility(View.GONE);
        return loadingView;
    }


    /**
     * 使用addViewInternal()是因为 addView()被我重载了
     */
    private void addViewInternal(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    private void addViewInternal(View child, ViewGroup.LayoutParams params) {
        super.addView(child, -1, params);
    }

    private void updateUIForMode() {
        final LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        if (this == mLoadingView.getParent()) {
            removeView(mLoadingView);
        }
        if (mMode.showHeaderLoadingLayout()) {
            addViewInternal(mLoadingView, 0, params);
        }
    }

    private void refreshLoadingViewsSize() {
        final int maximumPullScroll = (int) (getHeaderSize() * 1f);

//        int pLeft = getPaddingLeft();
//        int pTop = getPaddingTop();
//        int pRight = getPaddingRight();
//        int pBottom = getPaddingBottom();

        if (mMode.showHeaderLoadingLayout()) {
            LayoutParams lp = (LayoutParams) mLoadingView.getLayoutParams();
            int pLeft = lp.leftMargin;
            int pTop = lp.topMargin;
            int pRight = lp.rightMargin;
            int pBottom = lp.bottomMargin;

            lp.height = maximumPullScroll;
            pTop = -maximumPullScroll;
            lp.setMargins(pLeft, pTop, pRight, pBottom);
        } else {
//            pTop = 0;
        }

//        setPadding(pLeft, pTop, pRight, pBottom);
    }

    public final StickyPullToRefreshHeader getHeaderLayout() {
        return mLoadingView;
    }

    public final T getRefreshableView() {
        return mRefreshableView;
    }

    private int getMaximumPullScroll() {
        return Math.round(getHeight() / FRICTION);
    }

    protected final int getHeaderSize() {
        //使用measure(0, 0)测量之后，获取的是头部内容部分的宽高
        mLoadingView.measure(0, 0);
        return mLoadingView.getMeasuredHeight();
    }

    private boolean isPullToRefreshEnabled() {
        return mMode.permitsPullToRefresh();
    }

    private boolean isReadyForPull() {
        if (mMode == Mode.PULL_FROM_START) {
            return mRefreshableView.getScrollY() == 0;
        }
        return false;
    }

    private boolean isRefreshing() {
        return mState == State.REFRESHING || mState == State.MANUAL_REFRESHING;
    }

    public final void setRefreshing() {
        setRefreshing(true);
    }

    private void setRefreshing(boolean doScroll) {
        if (!isRefreshing()) {
            setState(State.MANUAL_REFRESHING, doScroll);
        }
    }

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {

        if (!isPullToRefreshEnabled()) {
            return false;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPull()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    mIsBeingDragged = false;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
                    return true;
                }

                if (isReadyForPull()) {
                    final float y = event.getY();
                    final float x = event.getX();
                    final float diffY, diffX, absDiffY;

                    diffY = y - mLastMotionY;
                    diffX = x - mLastMotionX;
                    absDiffY = Math.abs(diffY);

                    if (absDiffY > mTouchSlop &&
                            (!mFilterTouchEvents || absDiffY > Math.abs(diffX))) {
                        //下滑才拦截
                        if (diffY >= 1f) {
                            mLastMotionY = y;
                            mLastMotionX = x;
                            mIsBeingDragged = true;
                        }
                    }
                }
                break;
            }
            default:
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public final boolean onTouchEvent(MotionEvent event) {
        if (!isPullToRefreshEnabled()) {
            return false;
        }

        if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //按下 开始下拉
                if (isReadyForPull()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    //开始下拉，移动
                    pullEvent();
                    return true;
                }
                break;
            }
            //停止下拉的时候
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    if (mState == State.RELEASE_TO_REFRESH && (null != mOnRefreshListener)) {
                        //放下手指开始回调，执行我们的回调任务
                        setState(State.REFRESHING, true);
                        return true;
                    }

                    if (isRefreshing()) {
                        smoothScrollTo(0);
                        return true;
                    }

                    //恢复到原来的UI状态
                    setState(State.RESET);

                    return true;
                }
                break;
            }
            default:
                break;
        }

        return false;
    }

    private void pullEvent() {
        final int newScrollValue;
        final int itemDimension;
        final float initialMotionValue, lastMotionValue;

        initialMotionValue = mInitialMotionY;
        lastMotionValue = mLastMotionY;

        //计算下拉移动了多少
        switch (mMode) {
            //下拉
            case PULL_FROM_START:
            default:
                newScrollValue = Math.round(Math.min(
                        initialMotionValue - lastMotionValue, 0) / FRICTION);
                itemDimension = getHeaderSize();
                break;
        }

        //显示HeaderView 得到移动的值，可以让LoadingView显示出来
        setHeaderScroll(newScrollValue);

        if (newScrollValue != 0 && !isRefreshing()) {
            float scale = Math.abs(newScrollValue) / (float) itemDimension;
            // 旋转左边的加载图片，显示文字和图片 这个地方最终会执行LoadingLayout中的 onPullImpl方法
            mLoadingView.onPull(scale);
        }

        //更新状态 包括2中 释放按下触摸，还有就是 没释放手的触摸
        if (mState != State.PULL_TO_REFRESH && itemDimension >= Math.abs(newScrollValue)) {
            setState(State.PULL_TO_REFRESH);
        } else if (mState == State.PULL_TO_REFRESH && itemDimension < Math.abs(newScrollValue)) {
            //下拉松手 可以松手了
            setState(State.RELEASE_TO_REFRESH);
        }
    }

    private void setHeaderScroll(int value) {

        if (mDownListener != null) {
            mDownListener.onPullDown(value);
        }

        final int maximumPullScroll = getMaximumPullScroll();
        value = Math.max(-maximumPullScroll, value);

        if (mLayoutVisibilityChangesEnabled) {
            //有位移才显示
            if (value < 0) {
                mLoadingView.setVisibility(View.VISIBLE);
            } else {
                mLoadingView.setVisibility(View.INVISIBLE);
            }
        }
        scrollTo(0, value);
    }

    public final void setMode(Mode mode) {
        if (mode != mMode) {
            mMode = mode;
            updateUIForMode();
        }
    }

    final void setState(State state, final boolean... params) {
        mState = state;

        switch (mState) {
            case RESET:
                onReset();
                break;
            case PULL_TO_REFRESH:
                onPullToRefresh();
                break;
            case RELEASE_TO_REFRESH:
                onReleaseToRefresh();
                break;
            case REFRESHING:
            case MANUAL_REFRESHING:
                onRefreshing(params[0]);
                break;
            case OVERSCROLLING:
            default:
                break;
        }

        if (null != mOnPullEventListener) {
            mOnPullEventListener.onPullEvent(mState, mMode);
        }
    }

    protected void onReset() {
        mIsBeingDragged = false;
        mLayoutVisibilityChangesEnabled = true;

        mLoadingView.reset();

        smoothScrollTo(0);
    }

    protected void onPullToRefresh() {
        mLoadingView.pullToRefresh();
    }

    protected void onReleaseToRefresh() {
        mLoadingView.releaseToRefresh();
    }

    protected void onRefreshing(final boolean doScroll) {
        mLoadingView.refreshing();

        if (doScroll) {
            if (mShowViewWhileRefreshing) {
                // Call Refresh Listener when the Scroll has finished
                OnSmoothScrollFinishedListener listener = new OnSmoothScrollFinishedListener() {
                    @Override
                    public void onSmoothScrollFinished() {
                        //回调接口执行
                        callRefreshListener();
                    }
                };

                smoothScrollTo(-getHeaderSize(), listener);

            } else {
                //回到原来的位置
                smoothScrollTo(0);
            }
        } else {

            callRefreshListener();//回调接口执行
        }
    }

    public void onRefreshComplete() {
        if (isRefreshing()) {
            setState(State.RESET);
        }
        if (mMode == Mode.MANUAL_REFRESH_ONLY) {
            setMode(Mode.PULL_FROM_START);
        }
    }

    private void callRefreshListener() {
        if (null != mOnRefreshListener) {
            //回调
            mOnRefreshListener.onRefresh();
        }
    }

    public static final int SMOOTH_SCROLL_DURATION_MS = 200;

    protected final void smoothScrollTo(int scrollValue) {
        smoothScrollTo(scrollValue, SMOOTH_SCROLL_DURATION_MS);
    }


    protected final void smoothScrollTo(int scrollValue, OnSmoothScrollFinishedListener listener) {
        smoothScrollTo(scrollValue, SMOOTH_SCROLL_DURATION_MS, 0, listener);
    }

    private void smoothScrollTo(int scrollValue, long duration) {
        smoothScrollTo(scrollValue, duration, 0, null);
    }

    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
                                      OnSmoothScrollFinishedListener listener) {
        if (null != mCurrentSmoothScrollRunnable) {
            mCurrentSmoothScrollRunnable.stop();
        }

        final int oldScrollValue;
        oldScrollValue = getScrollY();

        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(
                    oldScrollValue, newScrollValue, duration, listener);

            if (delayMillis > 0) {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
            } else {
                post(mCurrentSmoothScrollRunnable);
            }
        }
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private OnSmoothScrollFinishedListener mListener;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        SmoothScrollRunnable(int fromY, int toY, long duration,
                             OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
            mDuration = duration;
            mListener = listener;
        }

        @Override
        public void run() {
            /**
             * 第一次启动需要初始化mStartTime
             * 其余的做Y轴移动处理
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math
                        .round((mScrollFromY - mScrollToY) * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                setHeaderScroll(mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                ViewCompat.postOnAnimation(StickyPullToRefreshBaseLayout.this, this);
            } else {
                if (null != mListener) {
                    mListener.onSmoothScrollFinished();
                }
            }
        }

        void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }


    public interface OnPullDownListener {
        void onPullDown(int distance);
    }

    public void setOnPullDownListener(OnPullDownListener downListener) {
        this.mDownListener = downListener;
    }


    public interface OnRefreshListener {
        void onRefresh();
    }

    public final void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }


    public interface OnPullEventListener {
        void onPullEvent(State state, Mode direction);
    }

    public void setOnPullEventListener(OnPullEventListener listener) {
        mOnPullEventListener = listener;
    }
}
