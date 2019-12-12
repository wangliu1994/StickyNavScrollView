package com.winnie.library.stickynav.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.winnie.library.R;


/**
 *
 * @author winnie
 * @date 2017/5/23
 * 下拉刷新的头部
 */
@SuppressWarnings("unused")
public class StickyPullToRefreshRotateHeader extends StickyPullToRefreshHeader {

    static final int ROTATION_ANIMATION_DURATION = 1200;
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    private Animation mRotateAnimation;
    private Matrix mRefreshImageMatrix;

    private float mRotationPivotX, mRotationPivotY;

    private final boolean mRotateDrawableWhilePulling = true;


    public StickyPullToRefreshRotateHeader(Context context, TypedArray typedArray) {
        super(context, typedArray);
        init();
    }

    private void init(){
        mRefreshImageView.setScaleType(ImageView.ScaleType.MATRIX);
        mRefreshImageMatrix = new Matrix();
        mRefreshImageView.setImageMatrix(mRefreshImageMatrix);

        mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.default_ptr_rotate;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {
            mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
            mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
        }
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        float angle;
        if (mRotateDrawableWhilePulling) {
            angle = scaleOfLayout * 90f;
        } else {
            angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
        }

        mRefreshImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
        mRefreshImageView.setImageMatrix(mRefreshImageMatrix);
    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {
        mRefreshImageView.startAnimation(mRotateAnimation);
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {
        mRefreshImageView.clearAnimation();
        resetImageRotation();
    }

    private void resetImageRotation() {
        if (null != mRefreshImageMatrix) {
            mRefreshImageMatrix.reset();
            mRefreshImageView.setImageMatrix(mRefreshImageMatrix);
        }
    }
}
