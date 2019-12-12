package com.winnie.stickynavscrollview.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.fragment.StickyTabFragment;

import java.util.ArrayList;

/**
 *
 * @author winnie
 * @date 2017/5/5
 */
public class StickyNavMultiTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav_mutl_tab);

        final ViewPager viewPager = findViewById(R.id.view_pager);

        final ArrayList<Fragment> list = new ArrayList<>();
        for(int i=0; i<3; i++){
            list.add(StickyTabFragment.newInstance(i));
        }

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), list));

        findViewById(R.id.button1).setOnClickListener(v -> viewPager.setCurrentItem(0));

        findViewById(R.id.button2).setOnClickListener(v -> viewPager.setCurrentItem(1));

        findViewById(R.id.button3).setOnClickListener(v -> viewPager.setCurrentItem(2));

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> list;

        MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }


        @Override
        public Fragment getItem(int position) {

            Fragment fragment = list.get(position);
//            if(fragment == null || fragment.isDetached()){
//                fragment = StickyTabFragment.newInstance(position);
//            }
//            list.set(position, fragment);

            return fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
