package com.zjh.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.blogclient.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zjh.adapter.NewsListAdapter;
import com.zjh.bean.NewsItem;
import com.zjh.biz.NewsitemBiz;
import com.zjh.config.Constants;
import com.zjh.dao.NewsItemDao;
import com.zjh.dao.NewsItemDaoImpl;
import com.zjh.ui.WebActivity;
import com.zjh.utils.NetworkUtil;
import com.zjh.utils.PreferenceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张建浩 on 2015/10/16.
 */
@SuppressLint("ValidFragment")
public class ChildFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2 ,AdapterView.OnItemClickListener{
    private final static String TAG = "ChildFragment";
    private int newstype;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private NewsListAdapter mNewsListAdapter;
    private List<NewsItem> newsList = new ArrayList<>();
    private int currentPage =1;
    private boolean isFist = true;
    private int fragmentType;
    private LinearLayout mLoadingLinearlayout;
    private LinearLayout mNoNetworkShow;
    private ImageView loadingIv;
    private NetworkChangeReceiver mNetworkChangeReceiver;



    public ChildFragment(int fragmentType,int newstype) {
        this.fragmentType = fragmentType;
        this.newstype = newstype;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFist){
            Log.v(TAG,"----"+fragmentType+"--"+newstype+"first come in-----");
            //判断前一次加载时间，两次时间间隔较短时，加载本地缓存
            long lastTime = PreferenceUtils.getRememberdTime(getActivity(),fragmentType,newstype);
            //刷新记录的时间
            PreferenceUtils.rememberOpenTime(getActivity(),fragmentType,newstype);
            //时间依据为一小时
            if (lastTime==0||((System.currentTimeMillis()-lastTime)/1000/60/60) >=1){
               //刷新
                System.out.println("last:"+lastTime+" curr:"+System.currentTimeMillis());
                Log.v(TAG, "刷新了" + ((System.currentTimeMillis() - lastTime) / 1000 / 60 / 60));
                new LoadingNewListTask().execute(Constants.REFRESH_DATA);
            }else {
                Log.v(TAG,"加载缓存"+((System.currentTimeMillis()-lastTime)/1000/60/60));
                new LoadingNewListTask().execute(Constants.REFRESH_LOCAL_CACHE);
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csdn_child_fragment,container,false);
        mLoadingLinearlayout = (LinearLayout) view.findViewById(R.id.is_loading_content);
        loadingIv = (ImageView)view.findViewById(R.id.data_loading_iv);
        mNoNetworkShow = (LinearLayout)view.findViewById(R.id.no_network_show);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(this);
        mListView = mPullRefreshListView.getRefreshableView();
        mNewsListAdapter = new NewsListAdapter(getActivity(),newsList);
        mListView.setAdapter(mNewsListAdapter);
        mListView.setOnItemClickListener(this);
        //注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(mNetworkChangeReceiver,filter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mNetworkChangeReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNewsListAdapter.notifyDataSetChanged();
        Log.v(TAG,"----"+fragmentType+"--"+newstype+"----onResume----");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "----" + fragmentType + "--" + newstype + "----onpause----");
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //下拉刷新
          new LoadingNewListTask().execute(Constants.REFRESH_DATA);
        Log.v(TAG, "-------点击了下拉刷新");

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //上拉加载更多
        new LoadingNewListTask().execute(Constants.LOAD_MORE_DATA);
        Log.v(TAG, "-------点击了上拉刷新");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NewsItem item = (NewsItem) mNewsListAdapter.getItem(position - 1);
        String newsLink = item.getNewLink();
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("newsLink", newsLink);
        intent.putExtra("title", item.getTitle());
        getActivity().startActivity(intent);

    }


    class LoadingNewListTask extends AsyncTask<Integer,Void,Integer>{
        private AnimationDrawable loadingAnimation;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isFist) {
                isFist = false;
                mPullRefreshListView.setVisibility(View.GONE);
                mLoadingLinearlayout.setVisibility(View.VISIBLE);
                Log.v(TAG,"加载动画出现了");
                loadingIv.setBackgroundResource(R.anim.data_loading);
                loadingAnimation = (AnimationDrawable) loadingIv.getBackground();
                loadingAnimation.start();
            }

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]){
                case Constants.REFRESH_DATA:
                    return refreshData();
                case Constants.LOAD_MORE_DATA:
                   return loadMoreData();
                case Constants.REFRESH_LOCAL_CACHE:
                    return refreshLocalCache();


            }
            return -1;
        }
      //执行完后调用
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (loadingAnimation!=null){
                loadingAnimation.stop();
                loadingAnimation = null;
            }
            mLoadingLinearlayout.setVisibility(View.GONE);
            mPullRefreshListView.setVisibility(View.VISIBLE);

            if (integer == Constants.REFRESH_SUCCESS){
                //加载成功
                Log.v(TAG, "-------加载新数据成功----");
                //刷新是重置数据
                mNewsListAdapter.setData(newsList);
                mNewsListAdapter.notifyDataSetChanged();

            }else if (integer == Constants.LOAD_MORE_DATA_SUCCESS){
                //加载更多时添加
                mNewsListAdapter.addAll(newsList);
                mNewsListAdapter.notifyDataSetChanged();
            } else if (integer == Constants.REFRESH_FAILURE||integer == Constants.LOAD_MORE_DATA_FAILURE){
                Log.v(TAG, "-------加载失败-----");
            } else if (integer == Constants.NOT_CONNECT_NETWORK){
                if (newsList.size()>0){
                    mNewsListAdapter.setData(newsList);
                    mNewsListAdapter.notifyDataSetChanged();
                }
                 mNoNetworkShow.setVisibility(View.VISIBLE);
            }
            mPullRefreshListView.onRefreshComplete();
        }
    }

    public int refreshData(){
        isFist = false;
        NewsItemDao dao = NewsItemDaoImpl.getInstance(getActivity());
        if (NetworkUtil.isNetworkAvaliable(getActivity())){

            try {
                currentPage = 1;
                fillnewsList(fragmentType, newstype);
                //清空数据库
                dao.deleteAll(fragmentType,newstype);
                //新数据存入数据库

                dao.addNewsItems(newsList,fragmentType,newstype);
                return Constants.REFRESH_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                return Constants.REFRESH_FAILURE;
            }
        }else {
            //网络未连接
             newsList = dao.getNewsList(fragmentType,newstype,currentPage);

            return Constants.NOT_CONNECT_NETWORK;
        }
    }

    public int loadMoreData(){
        NewsItemDao dao = NewsItemDaoImpl.getInstance(getActivity());
        if (NetworkUtil.isNetworkAvaliable(getActivity())){
            //有网络连接
            currentPage++;
            NewsitemBiz newsbiz = new NewsitemBiz();
       try {
             fillnewsList(fragmentType, newstype);
           Log.v(TAG, "加载更多");
           //当前页面存入数据库
           dao.addNewsItems(newsList,fragmentType,newstype);
           return Constants.LOAD_MORE_DATA_SUCCESS;
          } catch (Exception e) {
           e.printStackTrace();
           return Constants.LOAD_MORE_DATA_FAILURE;
          }

        }else {
            //网络未连接
            currentPage++;
            newsList.addAll(dao.getNewsList(fragmentType,newstype,currentPage));
            return Constants.NOT_CONNECT_NETWORK;
        }

    }

    public int refreshLocalCache(){
        NewsItemDao dao = NewsItemDaoImpl.getInstance(getActivity());
        newsList = dao.getNewsList(fragmentType,newstype,currentPage);
        return Constants.REFRESH_SUCCESS;
    }

    public void fillnewsList(int fragmentType,int newstype) throws IOException {
        NewsitemBiz newsbiz = new NewsitemBiz();
        switch (fragmentType){
            case Constants.CSDN_FRAGMENT:
                if (newstype == Constants.NEWS_ANDROID)
                    newsList = newsbiz.getCSDNAnddroidList(newstype,currentPage);
                else
                    newsList = newsbiz.getNewsList(newstype, currentPage);
                break;
            case Constants.DEV_FRAGMENT:
                if (newstype == Constants.DEV_INFOMATION){
                    Log.v(TAG,"newstype"+newstype+"currentpage"+currentPage);
                    newsList = newsbiz.getDevStoreList(newstype,currentPage);
                    Log.v(TAG,"--newlist2 size"+newsList.size());
                }

                else{
                    newsList = newsbiz.getDevStoreArticle(newstype, currentPage);
                    Log.v(TAG,"--newlist2 size"+newsList.size());
                }
                break;
            case Constants.EOE_FRAGMENT:
              newsList = newsbiz.getEoeNewsList(newstype,currentPage);
                break;
            case Constants.OSCHINA_FRAGMENT:
                if (newstype == Constants.OSCHINA_ANDROID_BLOGS) {
                    newsList = newsbiz.getOsChinaAndroidBlogsList(newstype,currentPage);
                }

                else {
                    newsList = newsbiz.getOsChinaBlogsList(newstype, currentPage);
                }
                break;
        }


    }

    private class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //网络状况变化监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                   if (NetworkUtil.isNetworkAvaliable(getActivity())){
                       mNoNetworkShow.setVisibility(View.GONE);
                       if (newsList.size()<1)
                           new LoadingNewListTask().execute(Constants.REFRESH_DATA);
                   }else {
                       mNoNetworkShow.setVisibility(View.VISIBLE);
                   }
            }
        }
    }
}
