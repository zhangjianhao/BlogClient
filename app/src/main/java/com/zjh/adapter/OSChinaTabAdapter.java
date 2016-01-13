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
public class OSChinaTabAdapter extends FragmentPagerAdapter {
    private final static String TAG = "OSChinaTabAdapter";
    private Context context;
    public static final int[] TITLES = new int [] {R.string.blogs,R.string.android,R.string.front_develop,R.string.server_develop,R.string.game,R.string.database,R.string.software_project};

    public OSChinaTabAdapter(FragmentManager fm,Activity activity) {
        super(fm);
        this.context = activity;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        Log.v(TAG, "----" + arg0 + "---");
        ChildFragment fragment = new ChildFragment(Constants.OSCHINA_FRAGMENT,arg0);
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
