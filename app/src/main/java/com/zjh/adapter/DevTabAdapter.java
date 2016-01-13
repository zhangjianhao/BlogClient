package com.zjh.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.blogclient.R;
import com.zjh.config.Constants;
import com.zjh.view.ChildFragment;

/**
 * Created by 张建浩 on 2015/10/20.
 */
public class DevTabAdapter extends FragmentPagerAdapter{
    private final static String TAG = "DevTabAdapter";
    private Context context;
    public static final int[] TITLES = new int [] {R.string.information,R.string.dev_article};

    public DevTabAdapter(FragmentManager fm,Activity activity) {
        super(fm);
        this.context = activity;
    }

    @Override
    public Fragment getItem(int position) {
        ChildFragment fragment = new ChildFragment(Constants.DEV_FRAGMENT,position);
        Log.v(TAG,"-----devtadaper---"+position);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return context.getString(TITLES[position % TITLES.length]);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
