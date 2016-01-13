package com.zjh.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogclient.R;
import com.viewpagerindicator.TabPageIndicator;
import com.zjh.adapter.DevTabAdapter;

/**
 * Created by 张建浩 on 2015/10/20.
 */
public class DevFragment extends Fragment {
    private final static String TAG = "DevFragment";
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private FragmentPagerAdapter mFragmentPageAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.devstore_main_fragment, container, false);
            mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.dev_view_indicator);
            mViewPager = (ViewPager) view.findViewById(R.id.dev_view_pager);
            mFragmentPageAdapter = new DevTabAdapter(getChildFragmentManager(), getActivity());
            mViewPager.setAdapter(mFragmentPageAdapter);
            mTabPageIndicator.setViewPager(mViewPager, 0);
        return view;
    }
}
