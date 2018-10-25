package com.winnie.stickynavscrollview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by winnie on 2017/7/4.
 */

public class CateGoryGridView extends GridView {

    public CateGoryGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CateGoryGridView(Context context) {
        super(context);
    }

    public CateGoryGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

