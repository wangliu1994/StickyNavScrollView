package com.winnie.stickynavscrollview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.adapter.RecyclerTestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by winnie on 2017/10/27.
 */

public class TestFragmentView extends FrameLayout {

    private List<String> mDatas = new ArrayList<String>();
    private String mTitle = "Defaut Value";
    private String[] mTitles = new String[] { "简介", "评价", "相关" };

    public TestFragmentView(@NonNull Context context, int i) {
        super(context);

        View viewTemp = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab, this);
        RecyclerView mRecyclerView = (RecyclerView) viewTemp.findViewById(R.id.id_stickynavlayout_innerscrollview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        for (int j = 0; j < 10; j++) {
            mDatas.add(mTitle + " -> " + j);
        }

        mRecyclerView.setAdapter(new RecyclerTestAdapter(getContext(), mDatas));

        TextView textView = viewTemp.findViewById(R.id.sticky_nav_tab_title);
        textView.setText(mTitles[i]);
    }
}
