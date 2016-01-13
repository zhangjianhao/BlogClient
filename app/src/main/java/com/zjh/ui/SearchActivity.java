package com.zjh.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogclient.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjh.adapter.NewsListAdapter;
import com.zjh.bean.NewsItem;
import com.zjh.biz.NewsitemBiz;
import com.zjh.config.Constants;
import com.zjh.utils.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张建浩 on 2015/10/24.
 */
public class SearchActivity extends Activity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2,AdapterView.OnItemClickListener{
    private final static String TAG = "SearchActivity";
    private EditText searchContentEt;
    private TextView searchActionTv;
    private LinearLayout backLinarLayout;
    private ImageView deleteContentIv;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private NewsListAdapter mNewsListAdapter;
    private List<NewsItem> mlist = new ArrayList<>();
    private int currentPage = 1;
    private int searchType = 1;
    private String keywords ;
    private boolean hasData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.search_main);
        searchType = getIntent().getIntExtra("type",1);
        mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.search_listview);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.setOnRefreshListener(this);
        mListView = mPullToRefreshListView.getRefreshableView();

        mNewsListAdapter = new NewsListAdapter(this,mlist);
        mListView.setAdapter(mNewsListAdapter);
        mListView.setOnItemClickListener(this);
        backLinarLayout = (LinearLayout)findViewById(R.id.search_back);
        backLinarLayout.setOnClickListener(this);
        searchActionTv = (TextView)findViewById(R.id.search_tv);
        searchActionTv.setOnClickListener(this);
        searchContentEt = (EditText)findViewById(R.id.search_content_et);
        searchContentEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(searchContentEt.getText()))
                    deleteContentIv.setVisibility(View.GONE);
                else
                    deleteContentIv.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        deleteContentIv = (ImageView)findViewById(R.id.delete_content);
        deleteContentIv.setOnClickListener(this);
        deleteContentIv.setVisibility(View.GONE);



    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.green_yellow);
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                //后退
                finish();
                break;
            case R.id.delete_content:
                //删除
                searchContentEt.setText("");
                break;
            case R.id.search_tv:
                //检查网络连接：
                if (!NetworkUtil.isNetworkAvaliable(this)){
                    Toast.makeText(SearchActivity.this,R.string.no_network,Toast.LENGTH_SHORT).show();
                    return ;
            }
                //开始搜索
                if (TextUtils.isEmpty(searchContentEt.getText())){
                    Toast.makeText(SearchActivity.this,R.string.please_input_search_content,Toast.LENGTH_SHORT).show();
                    return;
                }

                currentPage = 1;
                NewsItem item = new NewsItem();
                item.setHasSummary(true);
                item.setSummary(getString(R.string.is_searching));
                List<NewsItem> list = new ArrayList<>();
                list.add(item);
                mNewsListAdapter.setData(list);
                mNewsListAdapter.notifyDataSetChanged();
                Log.v(TAG,"------正在搜素搜中----");
                keywords = searchContentEt.getText().toString().trim();
                new SearchAsynTask().execute(keywords);
                break;

        }
    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
         //上拉加载更多
        if (keywords!=null){
            new SearchAsynTask().execute(keywords);
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG,"-------点击了"+hasData+"---");
        if (hasData) {
            NewsItem item = (NewsItem) mNewsListAdapter.getItem(position - 1);
            String newsLink = item.getNewLink();
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("newsLink", newsLink);
            intent.putExtra("title", item.getTitle());
            startActivity(intent);
        }
    }

    //异步任务
    private class SearchAsynTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            if (NetworkUtil.isNetworkAvaliable(SearchActivity.this)){
                NewsitemBiz biz = new NewsitemBiz();
                try {
                    if (searchType == 1)
                    mlist = biz.getSearchCSDNList(params[0],"blog",currentPage);
                    else mlist = biz.getSearchOschinaList(params[0],"blog",currentPage);
                    currentPage++;
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
                return 1;
            }else
                return Constants.NOT_CONNECT_NETWORK;
        }



        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == Constants.NOT_CONNECT_NETWORK){
                Toast.makeText(SearchActivity.this,R.string.no_network,Toast.LENGTH_SHORT).show();
            }else if (integer == -1){
                if (mlist.size()<1){
                    NewsItem item = new NewsItem();
                    item.setHasSummary(true);
                    item.setSummary(getString(R.string.searching_error));
                    List<NewsItem> list = new ArrayList<>();
                    list.add(item);
                    mNewsListAdapter.setData(list);
                    mNewsListAdapter.notifyDataSetChanged();
                }
                Toast.makeText(SearchActivity.this,R.string.searching_error,Toast.LENGTH_SHORT).show();
            }else {
                //搜索成功，更新界面
                if (currentPage ==2){
                    if (mlist.size()>0){
                        hasData = true;
                        mNewsListAdapter.setData(mlist);
                        mNewsListAdapter.notifyDataSetChanged();

                    }
                    else  if (mlist.size()<1){
                        hasData = false;
                        NewsItem item = new NewsItem();
                        item.setHasSummary(true);
                        item.setSummary(getString(R.string.not_found));
                        mlist.add(item);
                        mNewsListAdapter.setData(mlist);
                        mNewsListAdapter.notifyDataSetChanged();

                    }


                }else if (currentPage>2){
                    hasData = true;
                    mNewsListAdapter.addAll(mlist);
                    mNewsListAdapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
                }


            }

        }
    }
}
