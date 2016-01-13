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

public class TabAdapter extends FragmentPagerAdapter {
	private Context context;

	public static final int[] TITLES = new int [] {R.string.csdn_industry,R.string.csdn_mobile,R.string.csdn_research,
			R.string.csdn_programmer_magazine,R.string.csdn_cloud_computing,R.string.android};

	public TabAdapter(FragmentManager fm,Activity activity)
	{
		super(fm);
		this.context = activity.getApplicationContext();
	}

	@Override
	public Fragment getItem(int arg0)
	{
		Log.v("CSDNTABadapter","----"+arg0+"---");
		ChildFragment fragment = new ChildFragment(Constants.CSDN_FRAGMENT,arg0);

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
