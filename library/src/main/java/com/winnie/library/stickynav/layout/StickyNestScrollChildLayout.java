package com.winnie.library.stickynav.layout;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.winnie.library.stickynav.constant.StickyConstant;


/**
 * Created by winnie on 2017/5/2.
 * 嵌套滑动子布局
 */
public class StickyNestScrollChildLayout extends LinearLayout implements NestedScrollingChild {
    private static final String TAG = StickyNestScrollChildLayout.class.getSimpleName();
    private static final int INVALID_POINTER = -1;

    private GestureDetector mGestureDetector;

    private VelocityTracker mVelocityTracker;//根绝Event的每一个Action计算抛事件的抛出速率
    private int mMinimumFlingVelocity;//抛事件的最小速率
    private int mMaximumFlingVelocity;//抛事件的最大速率

    private int[] mConsumed = new int[2];//嵌套滑动父布局消耗的距离
    private int[] mOffset = new int[2];//嵌套滑动后自己在父布局的位置

    private int mLastMotionX;//当前一次touchEvent的X值坐标
    private int mLastMotionY;//当前一次touchEvent的Y值坐标
    private int mNestedYOffset;//嵌套滑动的距离

    private boolean mIsBeingDragged = false;//是否被拖动
    private int mActivePointerId = INVALID_POINTER;//多手指点击的点击点
    private int mTouchSlop;//能让我们判断当前移动为一次Scroll的一个像素值

    public StickyNestScrollChildLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

//        mGestureDetector = new GestureDetector(getContext(), gestureListener);
//        mGestureDetector.setOnDoubleTapListener(doubleTapListener);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mTouchSlop = configuration.getScaledTouchSlop();

        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        setOrientation(VERTICAL);
    }

    //触摸时调用滑动
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        MotionEvent vtev = MotionEvent.obtain(event);
//
//        final int actionMasked = MotionEventCompat.getActionMasked(event);
//
//        if (actionMasked == MotionEvent.ACTION_DOWN) {
//            mNestedYOffset = 0;
//        }
//        vtev.offsetLocation(0, mNestedYOffset);
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // 按下事件调用startNestedScroll
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//                mLastMotionX = (int) event.getX();
//                mLastMotionY = (int) event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final int index = event.findPointerIndex(0);
//                final int x = (int) (event.getX(index) + 0.5f);
//                final int y = (int) (event.getY(index) + 0.5f);
//                int dx = mLastMotionX - x;
//                int dy = mLastMotionY - y;
//                // 移动事件调用dispatchNestedPreScroll
//                if(dispatchNestedPreScroll(dx, dy, mConsumed, mOffset)){
//                    vtev.offsetLocation(0, mOffset[1]);
//                    mNestedYOffset += mOffset[1];
//                }
//
//                Log.d(TAG, "offset--x:" + mOffset[0] + ",offset--y:" + mOffset[1]);
//
//                if(dispatchNestedScroll(0, 0, 0, 0, mOffset)){
//                    vtev.offsetLocation(0, mOffset[1]);
//                    mNestedYOffset += mOffset[1];
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                int pointerId = event.getPointerId(0);
//                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
//                final float velocityY =  mVelocityTracker.getYVelocity(pointerId);
//                final float velocityX =  mVelocityTracker.getXVelocity(pointerId);
//
//                if ((Math.abs(velocityY) > mMinimumFlingVelocity)) {
//                    fling(0, -velocityY);
//                }
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
//                    mVelocityTracker = null;
//                }
//
//                // 弹起事件调用stopNestedScroll
//                stopNestedScroll();
//                break;
//            default:
//                break;
//        }
//        if (mVelocityTracker != null) {
//            mVelocityTracker.addMovement(vtev);
//        }
//        vtev.recycle();
//        return super.dispatchTouchEvent(event);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        //拦截拖动事件，不响应内部点击
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(event);
                mIsBeingDragged = false;
                mActivePointerId = event.getPointerId(0);

                mLastMotionX = (int) event.getX();
                mLastMotionY = (int) event.getY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = event.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(event);
                mNestedYOffset = 0;

                if (Math.abs((int) event.getY(pointerIndex) - mLastMotionY) > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                recycleVelocityTracker();
                stopNestedScroll();
                break;
            default:
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        initVelocityTrackerIfNotExists();

        MotionEvent vtev = MotionEvent.obtain(event);

        final int actionMasked = MotionEventCompat.getActionMasked(event);
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                mActivePointerId = event.getPointerId(0);

                mLastMotionX = (int) event.getX();
                mLastMotionY = (int) event.getY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                int deltaY = mLastMotionY - (int) event.getY();
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                if (dispatchNestedPreScroll(0, deltaY, mConsumed, mOffset)) {
                    deltaY -= mConsumed[1];
//                    vtev.offsetLocation(0, mOffset[1]);
                    mNestedYOffset += mOffset[1];
                }
                if (dispatchNestedScroll(0, 0, 0, 0, mOffset)) {
                    mLastMotionY -= mOffset[1];
//                    vtev.offsetLocation(0, mOffset[1]);
                    mNestedYOffset += mOffset[1];
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    final int velocityY = (int) -mVelocityTracker.getYVelocity(mActivePointerId);

                    if ((Math.abs(velocityY) > mMinimumFlingVelocity)) {
                        if (!dispatchNestedPreFling(0, velocityY)) {
                            boolean canScroll = canScrollVertically(-1) || canScrollVertically(1);
                            dispatchNestedFling(0, velocityY, canScroll);
                        }
                    }
                }
                recycleVelocityTracker();
                stopNestedScroll();
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                stopNestedScroll();
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            default:
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void fling(float velocityX, float velocityY) {
        if (!dispatchNestedPreFling(velocityX, velocityY)) {
            boolean canScroll = canScrollVertically(-1) || canScrollVertically(1);
            dispatchNestedFling(velocityX, velocityY, canScroll);
        }
    }

    /*---------------------------------android手势操作滑动效果触摸屏事件处理 ---------------------------------*/
    GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        /**
         * 用户轻触触屏：Touch down(仅一次)时触发
         * @param event  event为down时的MotionEvent
         * @return
         */
        public boolean onDown(MotionEvent event) {
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
            mLastMotionX = (int) event.getX();
            mLastMotionY = (int) event.getY();
            return true;
        }

        /**
         * 用户轻触触屏，且尚未松开或拖动：
         * 在Touch down(仅一次)之后一定时间（115ms）触发
         * @param e e为down时的MotionEvent
         */
        public void onShowPress(MotionEvent e) {

        }

        /**
         *用户（轻触触屏后）松开：Touch up(仅一次)时触发
         * @param e e为up时的MotionEvent
         * @return
         */
        public boolean onSingleTapUp(MotionEvent e) {
            stopNestedScroll();
            return true;
        }


        /**
         *用户轻触触屏，并拖动：按下并滑动时触发:
         * @param e1  e1为down(仅一次)时的MotionEvent
         * @param e2  e2为move(多个)时的MotionEvent
         * @param distanceX
         * @param distanceY
         * @return
         */
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            dispatchNestedPreScroll((int) distanceX, (int) distanceY, mConsumed, mOffset);
            if(StickyConstant.IS_DEBUG) {
                Log.d(TAG, "offset--x:" + mOffset[0] + ",offset--y:" + mOffset[1]);
            }
            dispatchNestedScroll(0, 0, 0, 0, mOffset);
            return true;
        }

        /**
         * 用户长按触屏(此View必须是可长按的： myView->setLongClickable(true);)：
         * 在Touch down之后一定时间（500ms）后，由多个down事件触发
         * @param e  e为down时的MotionEvent
         */
        public void onLongPress(MotionEvent e) {

        }

        /**
         * 用户按下触屏、快速移动后松开：按下并快速滑动一小段距离（多个move），up时触发
         * @param e1 e1为down(仅一次)时的MotionEvent
         * @param e2 e2为up(仅一次)时的MotionEvent
         * @param velocityX X的滑动速度, 像素/秒
         * @param velocityY Y的滑动速度, 像素/秒
         * @return
         */
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > mTouchSlop && Math.abs(velocityY) > mMinimumFlingVelocity) {
                fling((int) -velocityX, (int) -velocityY);
            }
            return true;
        }
    };

    GestureDetector.OnDoubleTapListener doubleTapListener = new GestureDetector.OnDoubleTapListener() {

        /**
         * 完成一次单击，并确定（300ms内）没有发生第二次单击事件后触发
         * @param e e为down时的MotionEvent
         * @return
         */
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        /**
         *第二次单击down时触发
         * @param e e为第一次down时的MotionEvent
         * @return
         */
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        /**
         *第二次单击down、move和up时都触发
         * @param e e为不同时机下的MotionEvent
         * @return
         */
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }
    };


    /*-----------------------------嵌套滑动------------------------------------*/
    private NestedScrollingChildHelper mChildHelper;

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "startNestedScroll");
        }

        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "stopNestedScroll");
        }

        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "dispatchNestedScroll");
        }

        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "dispatchNestedPreScroll");
        }

        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
