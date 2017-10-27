package com.winnie.stickynavscrollview.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by winnie on 2017/5/5.
 */

public interface OnItemClickListener<T>
{
    void onItemClick(ViewGroup parent, View view, T t, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
