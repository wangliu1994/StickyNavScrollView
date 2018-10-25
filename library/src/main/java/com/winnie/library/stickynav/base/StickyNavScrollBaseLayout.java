package com.winnie.library.stickynav.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.winnie.library.R;
import com.winnie.library.stickynav.constant.StickyConstant;
import com.winnie.library.stickynav.layout.StickyNestScrollChildLayout;
import com.winnie.library.stickynav.view.StickyNavScrollView;


/**
 * Created by winnie on 2017/5/10.
 * 通过嵌套滑动实现的悬浮吸顶菜单栏通用控件基类
 */

public abstract class StickyNavScrollBaseLayout extends LinearLayout implements NestedScrollingParent, IStickyBaseListener {
    private static final String TAG = StickyNavScrollView.class.getSimpleName();

    private OverScroller mScroller;

    private int mHeadViewHeight;
    private int mStickyNavMarginTop = 0;
    private int mTabViewHeight;

    //可以继续往下滑动
    private boolean canScrollToBottom = true;
    //可以继续往上滑动
    private boolean canScrollToTop = false;

    /*-------------------构造函数------------------------*/
    public StickyNavScrollBaseLayout(Context context) {
        super(context, null);
        mScroller = new OverScroller(getContext());
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);

        setNestedScrollingEnabled(true);
        setOrientation(VERTICAL);
        initView(context, null);
    }

    public StickyNavScrollBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(getContext());
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        setOrientation(VERTICAL);
        if(attrs!= null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StickyNav);
            initView(context, array);
            array.recycle();
        }else {
            initView(context, null);
        }

    }

    public StickyNavScrollBaseLayout(Context context, TypedArray typedArray) {
        super(context, null);
        mScroller = new OverScroller(getContext());
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        setOrientation(VERTICAL);
        initView(context, typedArray);
    }

    protected abstract void initView(Context context, TypedArray typedArray);
    protected abstract ViewGroup getStickyHeadView();
    protected abstract ViewGroup getStickyTabBar();
    protected abstract ViewGroup getStickyTabView();

    /*------------------------------悬浮吸顶--------------------------------*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        //getMeasuredHeight()在这里拿到的是屏幕内显示的高度
        mTabViewHeight = getMeasuredHeight() - getStickyTabBar().getMeasuredHeight() - mStickyNavMarginTop;
        ViewGroup.LayoutParams params = getStickyTabView().getLayoutParams();
        if(hasTabView()) {
            params.height = mTabViewHeight;
        }else {
            params.height = 0;
        }

        setMeasuredDimension(getMeasuredWidth(), getStickyHeadView().getMeasuredHeight()
                + getStickyTabBar().getMeasuredHeight() + getStickyTabView().getMeasuredHeight());

        mHeadViewHeight = getStickyHeadView().getMeasuredHeight();
        //getMeasuredHeight()在这里，因为setMeasuredDimension，所以拿到的是三部分高度之和
//        int measureHeight = getMeasuredHeight();
//        int height = getHeight();
//        int headViewHeight = getStickyHeadView().getMeasuredHeight();
//        int tabBarHeight = getStickyTabBar().getMeasuredHeight();
//        int tabViewHeight1 = getStickyTabView().getMeasuredHeight();
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if(direction < 0){
            return canScrollToTop;
        }
        if(direction > 0){
            return canScrollToBottom;
        }
        return super.canScrollVertically(direction);
    }

    private boolean hasTabView(){

        if(getStickyTabView() == null){
            return false;
        }

        if(getStickyTabView().getChildCount() <= 0) {
            return false;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeadViewHeight = getStickyHeadView().getMeasuredHeight();
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(x, y, oldx, oldy);
        }
    }

    private ScrollViewListener scrollViewListener;

    public void setScrollViewListener(ScrollViewListener listener) {
        this.scrollViewListener = listener;
    }

    public interface ScrollViewListener {
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }

    /**
     * 设置悬浮菜单离顶部的距离
     */
    public void setStickyNavTopMargin(int marginTop){
        mStickyNavMarginTop = marginTop;
        invalidate();
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, getMaxScrollHeight());
        invalidate();
    }

    //头部滑动最大值
    private int getMaxScrollHeight(){
        int scrollMaxHeight;
        if(hasTabView()) {
            scrollMaxHeight = mHeadViewHeight - mStickyNavMarginTop;
        }else {
            scrollMaxHeight = mHeadViewHeight - mStickyNavMarginTop - mTabViewHeight;
        }

        return scrollMaxHeight;
    }

    public void directlyScrollTo(int x, int y) {
        if (!mScroller.isFinished()) {
            //把之前没完成的fling停止了
            mScroller.abortAnimation();
        }
        scrollTo(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > getMaxScrollHeight()) {
            y = getMaxScrollHeight();
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

        if(getScrollY() < getMaxScrollHeight()){
            canScrollToTop = getScrollY() > 0;
        }else {
            canScrollToTop = true;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    //TODO 底部列表滑到第一个item时头部才可以展示出来
    public boolean isNavSticky() {
        return getScrollY() >= getMaxScrollHeight();
    }

    //TODO 底部列表滑到第一个item时头部才可以展示出来
    private boolean isTabViewScrollToTop(){
        boolean flag = true;
        ViewGroup tabView = getStickyTabView();
        if(tabView.getChildAt(0) != null){
            if(tabView.getChildAt(0) instanceof IStickyTabViewListener){
                flag = ((IStickyTabViewListener)tabView.getChildAt(0)).isScrollToTop();
            }
        }
        return flag;
    }

    private boolean isScrollToTop(View view){
        //XRecyclerView由于有headView  所以不能直接使用ViewCompat.canScrollVertically()来判断
//        if(view instanceof XRecyclerView){
//            RecyclerView.LayoutManager manager = ((XRecyclerView)view).getLayoutManager();
//            int firstVisibleItem = 1;
//            if (manager instanceof LinearLayoutManager) {
//                firstVisibleItem = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
//            } else if (manager instanceof GridLayoutManager) {
//                firstVisibleItem = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
//            }
//
//            View child = ((XRecyclerView)view).getChildAt(0);
//            if(child != null){
//                return child.getTop() == 0 && firstVisibleItem == 1;
//            }
//             return false;
//        }
        return !view.canScrollVertically(-1);
    }

    /*------------------------嵌套滑动----------------------------*/
    private NestedScrollingParentHelper mParentHelper;

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onStartNestedScroll");
        }
        if (!mScroller.isFinished()) {
            //把之前没完成的fling停止了
            mScroller.abortAnimation();
        }
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onNestedScrollAccepted");
        }
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onStopNestedScroll");
        }
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onNestedPreScroll");
        }

        if(getScrollY() < getMaxScrollHeight()){
            canScrollToTop = getScrollY() > 0;
            canScrollToBottom = true;
        }else {
            if(target instanceof StickyNestScrollChildLayout){
                canScrollToBottom = true;
            }else {
                canScrollToBottom = target.canScrollVertically(1);
            }
            canScrollToTop = true;
        }


        //往下滑，头部还没有完全滑出界面
        boolean hiddenTop = dy > 0 && getScrollY() < getMaxScrollHeight();
        //往上滑，底部列表已经滑到顶了
        boolean showTop = dy < 0 && getScrollY() >= 0 && isScrollToTop(target);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[0] = 0;
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onNestedScroll");
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onNestedFling");
        }
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "onNestedPreFling");
        }

        boolean hiddenTop = velocityY > 0 && getScrollY() < getMaxScrollHeight(); //往上滑，头部还没有完全滑出界面
        boolean showTop = velocityY < 0 && getScrollY() >= 0 && isScrollToTop(target);  //往下滑，底部列表已经滑到顶了

//        showTop = showTop && isTabViewScrollToTop();//底部列表滑到第一个item时头部才可以展示出来

        if (hiddenTop || showTop) {
            fling((int) velocityY);
            return true;
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        if(StickyConstant.IS_DEBUG) {
            Log.d(TAG, "getNestedScrollAxes");
        }
        return mParentHelper.getNestedScrollAxes();
    }

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
