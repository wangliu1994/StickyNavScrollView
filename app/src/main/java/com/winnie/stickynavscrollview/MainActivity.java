package com.winnie.stickynavscrollview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.activity.StickyNavPullToRefreshLayoutActivity;
import com.winnie.stickynavscrollview.activity.StickyNavPullToRefreshViewActivity;
import com.winnie.stickynavscrollview.activity.StickyNavScrollLayoutActivity1;
import com.winnie.stickynavscrollview.activity.StickyNavScrollLayoutActivity2;
import com.winnie.stickynavscrollview.activity.StickyNavScrollViewActivity1;
import com.winnie.stickynavscrollview.activity.StickyNavScrollViewActivity2;


/**
 * Created by winnie on 2017/5/25.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StickyScrollView(View view){
//        Intent i = new Intent(this, StickyScrollViewActivity.class);
//        startActivity(i);
    }

    public void StickyNavScrollView1(View view){
        Intent i = new Intent(this, StickyNavScrollViewActivity1.class);
        startActivity(i);
    }

    public void StickyNavScrollView2(View view){
        Intent i = new Intent(this, StickyNavScrollViewActivity2.class);
        startActivity(i);
    }

    public void StickyNavScrollLayout1(View view){
        Intent i = new Intent(this, StickyNavScrollLayoutActivity1.class);
        startActivity(i);
    }

    public void StickyNavScrollLayout2(View view){
        Intent i = new Intent(this, StickyNavScrollLayoutActivity2.class);
        startActivity(i);
    }

    public void StickyNavPullToRefreshView(View view){
        Intent i = new Intent(this, StickyNavPullToRefreshViewActivity.class);
        startActivity(i);
    }

    public void StickyNavPullToRefreshLayout(View view){
        Intent i = new Intent(this, StickyNavPullToRefreshLayoutActivity.class);
        startActivity(i);
    }

    public void ClipChild(View view){
//        Intent i = new Intent(this, StickyNavPullToRefreshLayoutActivity.class);
//        startActivity(i);
    }
}
