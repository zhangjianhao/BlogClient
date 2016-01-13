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
 * Created by 张建浩 on 2015/10/21.
 */
public class EoeTabAdapter extends FragmentPagerAdapter {
    private final static String TAG = "EoeTabAdapter";
    private Context context;
    public static final int[] TITLES = new int [] {R.string.information,
            R.string.freshman,R.string.share_experience,R.string.framework_develop,
            R.string.example_course,R.string.source_code_download,R.string.technology_ask};

    public EoeTabAdapter(FragmentManager fm,Activity activity) {
        super(fm);
        this.context = activity;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        Log.v(TAG, "----" + arg0 + "---");
        ChildFragment fragment = new ChildFragment(Constants.EOE_FRAGMENT,arg0);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return context.getString(TITLES[position % TITLES.length]);
    }

    @Override
    public int getCount()
    {
        return TITLES.length;
    }
}
