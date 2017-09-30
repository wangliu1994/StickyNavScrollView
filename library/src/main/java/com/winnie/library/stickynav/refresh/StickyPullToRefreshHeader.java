package com.winnie.library.stickynav.refresh;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winnie.library.R;


/**
 * Created by winnie on 2017/5/22.
 *下拉刷新的头部
 */

public abstract class StickyPullToRefreshHeader extends FrameLayout {

    protected ImageView mRefreshImageView;
    protected TextView mRefreshSubTextView;
    protected TextView mRefreshTextView;
    protected ProgressBar mPullToRefreshBar;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;

    protected boolean mUseIntrinsicAnimation;

    public StickyPullToRefreshHeader(Context context, TypedArray typedArray) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, this);

        mRefreshImageView = (ImageView) findViewById(R.id.pull_to_refresh_image);
        mRefreshTextView = (TextView) findViewById(R.id.pull_to_refresh_text);
        mRefreshSubTextView = (TextView) findViewById(R.id.pull_to_refresh_sub_text);
        mPullToRefreshBar = (ProgressBar) findViewById(R.id.pull_to_refresh_progress);

        mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
        mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
        mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);

        Drawable imageDrawable = null;
        if(typedArray!= null){
            if(typedArray.hasValue(R.styleable.StickyPullToRefresh_headerBackGround)){
                Drawable drawable = typedArray.getDrawable(R.styleable.StickyPullToRefresh_headerBackGround);
                if (drawable != null) {
                    ViewCompat.setBackground(this, drawable);
                }
            }

            if(typedArray.hasValue(R.styleable.StickyPullToRefresh_headerTextAppearance)){
                TypedValue styleId = new TypedValue();
                typedArray.getValue(R.styleable.StickyPullToRefresh_headerTextAppearance, styleId);
                setTextAppearance(styleId.data);
            }

            if(typedArray.hasValue(R.styleable.StickyPullToRefresh_headerSubTextAppearance)){
                TypedValue styleId = new TypedValue();
                typedArray.getValue(R.styleable.StickyPullToRefresh_headerSubTextAppearance, styleId);
                setSubTextAppearance(styleId.data);
            }

            if(typedArray.hasValue(R.styleable.StickyPullToRefresh_headerTextColor)){
                ColorStateList colors = typedArray.getColorStateList(R.styleable.StickyPullToRefresh_headerTextColor);
                if(colors != null) {
                    setTextColor(colors);
                };
            }

            if(typedArray.hasValue(R.styleable.StickyPullToRefresh_headerSubTextColor)){
                ColorStateList colors = typedArray.getColorStateList(R.styleable.StickyPullToRefresh_headerSubTextColor);
                if(colors != null) {
                    setSubTextColor(colors);
                };
            }

            if (typedArray.hasValue(R.styleable.StickyPullToRefresh_headerDrawable)) {
                imageDrawable = typedArray.getDrawable(R.styleable.StickyPullToRefresh_headerDrawable);
            }
        }

        if (imageDrawable == null) {
            imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        }
        setLoadingDrawable(imageDrawable);
        mPullToRefreshBar.setVisibility(GONE);

        reset();
    }

    public final void onPull(float scaleOfLayout) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
    }

    public void pullToRefresh() {

        mRefreshTextView.setText(mPullLabel);
        pullToRefreshImpl();
    }

    public void releaseToRefresh() {
        mRefreshTextView.setText(mReleaseLabel);
        releaseToRefreshImpl();
    }

    public void refreshing() {
        mRefreshTextView.setText(mRefreshingLabel);
        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mRefreshImageView.getDrawable()).start();
        }else {
            refreshingImpl();
        }
    }

    public void reset() {
        mRefreshTextView.setText(mPullLabel);
        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mRefreshImageView.getDrawable()).stop();
        } else {
            resetImpl();
        }

        if (null != mRefreshSubTextView) {
            if (TextUtils.isEmpty(mRefreshSubTextView.getText())) {
                mRefreshSubTextView.setVisibility(View.GONE);
            } else {
                mRefreshSubTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    public final void setLoadingDrawable(Drawable imageDrawable) {

        mRefreshImageView.setImageDrawable(imageDrawable);
        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

        onLoadingDrawableSet(imageDrawable);
    }

    public void setBgColor(int bgColor) {
        setBackgroundColor(bgColor);
    }

    public void setHeadTextColor(int textColor) {
        if (mRefreshTextView != null) {
            mRefreshTextView.setTextColor(textColor);
        }
        if (mRefreshSubTextView != null) {
            mRefreshSubTextView.setTextColor(textColor);
        }
    }

    private void setSubHeaderText(CharSequence label) {
        if (mRefreshSubTextView != null) {
            if (TextUtils.isEmpty(label)) {
                mRefreshSubTextView.setVisibility(View.GONE);
            } else {
                mRefreshSubTextView.setText(label);

                if (View.GONE == mRefreshSubTextView.getVisibility()) {
                    mRefreshSubTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setSubTextAppearance(int value) {
        if (mRefreshSubTextView != null) {
            mRefreshSubTextView.setTextAppearance(getContext(), value);
        }
    }

    private void setSubTextColor(ColorStateList color) {
        if (mRefreshSubTextView != null) {
            mRefreshSubTextView.setTextColor(color);
        }
    }

    private void setTextAppearance(int value) {
        if (mRefreshTextView != null) {
            mRefreshTextView.setTextAppearance(getContext(), value);
        }
        if (mRefreshSubTextView != null) {
            mRefreshSubTextView.setTextAppearance(getContext(), value);
        }
    }

    private void setTextColor(ColorStateList color) {
        if (mRefreshTextView != null) {
            mRefreshTextView.setTextColor(color);
        }
        if (mRefreshSubTextView != null) {
            mRefreshSubTextView.setTextColor(color);
        }
    }

    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract void resetImpl();
}
