package com.winnie.stickynavscrollview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winnie.stickynavscrollview.R;
import com.winnie.stickynavscrollview.adapter.CommonAdapter;
import com.winnie.stickynavscrollview.adapter.RecyclerTestAdapter;
import com.winnie.stickynavscrollview.adapter.TestPagerAdapter;
import com.winnie.stickynavscrollview.adapter.ViewHolder;
import com.winnie.stickynavscrollview.view.TestFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by winnie on 2017/10/26.
 */

public class StickyTabFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private String mTitle = "Defaut Value";

    private List<String> mDatas = new ArrayList<String>();

    private int index;


    private String[] mTitles = new String[] { "简介", "评价", "相关" };
    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mAdapter;
    private TabFragment[] mFragments = new TabFragment[mTitles.length];

    private TextView mTextView;


    public static StickyTabFragment newInstance(int index){
        StickyTabFragment fragment = new StickyTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index +1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        index = bundle.getInt("index");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sticky_nav_tab_fragment, container, false);

        mViewPager = view.findViewById(R.id.sticky_nav_tab_view);
//        mRecyclerView = view.findViewById(R.id.sticky_nav_tab_view);

        initViewPager1();

        mTextView = view.findViewById(R.id.sticky_nav_tab_bar);
        mTextView.setText("软件介绍" + index);
        return view;

    }

    //这样可以正常展示列表
    private void initSingleList(){

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int temp = index * 100;
        for (int i = 0; i < temp; i++) {
            mDatas.add(mTitle + " -> " + i);
        }

        mRecyclerView.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.item, mDatas) {
            @Override
            public void convert(ViewHolder holder, String o) {
                holder.setText(R.id.id_info, o);
            }
        });

        mRecyclerView.setAdapter(new RecyclerTestAdapter(getContext(), mDatas));
    }

    //这样可以正常展示列表
    private void initViewPager(){
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            TestFragmentView viewTemp = new TestFragmentView(getContext(), i);
            views.add(viewTemp);
        }

        mViewPager.setAdapter(new TestPagerAdapter(views));
        mViewPager.setCurrentItem(0);
    }


    //这样不能正常展示列表  解决方案：使用getChildFragmentManager()  而不是getActivity().getSupportFragmentManager()
    private void initViewPager1(){
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = TabFragment.newInstance(mTitles[i]);
        }

        mAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public TabFragment getItem(int position) {
                return mFragments[position];
            }

//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                Fragment view = mFragments[position];
//                return view;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
//            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mFragments.length);

    }
}
