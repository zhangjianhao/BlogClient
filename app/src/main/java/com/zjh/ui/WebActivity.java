package com.zjh.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blogclient.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjh.utils.NetworkUtil;

public class WebActivity extends Activity {
    private WebView mWebView;
    private final static String TAG = "WebActivity";
    private LinearLayout backLinearLayout;
    private AnimationDrawable animation;
    private ImageView mCryIv;
    private LinearLayout notNetworkContent;
    private LinearLayout mLoadingLinearlayout;
    private ImageView loadingIv;
    private int loadCount = 0;
    private AnimationDrawable loadingAnimation;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private String url = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.webview_main);
        Intent intent = getIntent();
        url = intent.getStringExtra("newsLink");
        String title = intent.getStringExtra("title");
//        if (title.length()>10){
//            title = title.substring(0,10)+"...";
//        }
        TextView text = (TextView)findViewById(R.id.webview_title);
        text.setText(title);
        backLinearLayout = (LinearLayout) findViewById(R.id.webview_back);
        backLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCryIv = (ImageView)findViewById(R.id.not_network_cry_iv);
        notNetworkContent = (LinearLayout)findViewById(R.id.not_network_content);
        mLoadingLinearlayout = (LinearLayout)findViewById(R.id.is_loading_content);
        loadingIv = (ImageView)findViewById(R.id.data_loading_iv);
        mWebView = (WebView) findViewById(R.id.web_content);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, filter);

        if (NetworkUtil.isNetworkAvaliable(this)){
            mWebView.setVisibility(View.VISIBLE);
            notNetworkContent.setVisibility(View.GONE);
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.setWebChromeClient(new MyWebChromeClient());

            WebSettings webSettings = mWebView.getSettings();
            //允许执行javascript脚本
            webSettings.setJavaScriptEnabled(true);
            // 设置可以支持缩放
            webSettings.setSupportZoom(true);
            //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
            webSettings.setUseWideViewPort(true);
            //设置默认加载的可视范围是大视野范围
            webSettings.setLoadWithOverviewMode(true);
            //自适应屏幕
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setAllowFileAccess(true);
            webSettings.setBuiltInZoomControls(true);

            mWebView.loadUrl(url);

        }else  {
            mWebView.setVisibility(View.GONE);
            notNetworkContent.setVisibility(View.VISIBLE);
        }


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
    protected void onResume() {
        super.onResume();
        if (notNetworkContent.getVisibility()==View.VISIBLE&&animation == null){
            mCryIv.setBackgroundResource(R.anim.no_network_cry);
            // 通过ImageView对象拿到背景显示的AnimationDrawable
            animation = (AnimationDrawable) mCryIv.getBackground();
            animation.start();
            Log.v(TAG,"--------没有网络------");
        }
    }

    private class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.v(TAG,"进度"+newProgress+"--------");
            if (newProgress>=60){
                if (loadingAnimation!=null){
                    loadingAnimation.stop();
                    loadingAnimation = null;
                }
                mLoadingLinearlayout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.v(TAG, "---on start load开始加载------");
            loadCount++;
            //为了防止加载一个url时重复调用
            if (loadCount<2) {
                mLoadingLinearlayout.setVisibility(View.VISIBLE);

                loadingIv.setBackgroundResource(R.anim.data_loading);
                loadingAnimation = (AnimationDrawable) loadingIv.getBackground();
                loadingAnimation.start();
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.v(TAG, "---on finish load加载完成------");
            //监听此事件 不准确
//            if (loadingAnimation!=null){
//                loadingAnimation.stop();
//                loadingAnimation = null;
//            }
//            mLoadingLinearlayout.setVisibility(View.GONE);
//            mWebView.setVisibility(View.VISIBLE);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            if (loadingAnimation!=null){
                loadingAnimation.stop();
                loadingAnimation = null;
            }
            mLoadingLinearlayout.setVisibility(View.GONE);
//            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.v(TAG, "---on error加载错误------");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            loadCount = 0;
            return true;
        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        } else{
            finish();
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animation != null){
            animation.stop();
            animation = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //网络状况变化监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                if (NetworkUtil.isNetworkAvaliable(WebActivity.this)){
                    if (animation != null){
                        animation.stop();
                        animation = null;
                        notNetworkContent.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                        mWebView.loadUrl(url);
                    }
                }
            }
        }
    }
}
