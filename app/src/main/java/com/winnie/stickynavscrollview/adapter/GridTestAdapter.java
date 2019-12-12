package com.winnie.stickynavscrollview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.winnie.stickynavscrollview.R;

import java.util.List;

/**
 *
 * @author winnie
 * @date 2017/7/4
 */
public class GridTestAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;

    public GridTestAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return R.layout.grid_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }
        return convertView;
    }
}
