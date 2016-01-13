package com.zjh.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogclient.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;
import com.zjh.view.CSDNFragment;
import com.zjh.view.DevFragment;
import com.zjh.view.EoeFragment;
import com.zjh.view.OSChinaFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    private final static String TAG = MainActivity.class.getName();
    private FragmentManager mFragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private int currentpage = 1;
    private CSDNFragment mCSDNFragment = null;
    private DevFragment mDevFragment = null;
    private TextView mToolBarTextView;
    private Toolbar mToolbar;
    private OSChinaFragment mOSChinaFragment = null;
    private EoeFragment mEoeFragment = null;
    private int quitCount = 0;
    private ImageView searchBlogsIv;
    private LinearLayout mADLinearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.activity_main);
        searchBlogsIv = (ImageView) findViewById(R.id.search_bolgs_iv);
        searchBlogsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("type", currentpage);
                startActivity(intent);
            }
        });
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mCSDNFragment = new CSDNFragment();
        transaction.replace(R.id.main_container, mCSDNFragment);
        transaction.commit();
        searchBlogsIv.setVisibility(View.VISIBLE);
        initToolbar();
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), getMenuObjects());



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


    }

    private List<MenuObject> getMenuObjects() {


        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.close);

        MenuObject csdnLogo = new MenuObject("CSDN");
        csdnLogo.setResource(R.drawable.csdn_logo2);
        csdnLogo.setScaleType(ImageView.ScaleType.FIT_XY);

        MenuObject eoeLogo = new MenuObject("eoe");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.eoe_logo);
        eoeLogo.setBitmap(b);
        eoeLogo.setScaleType(ImageView.ScaleType.FIT_XY);

        MenuObject devLogo = new MenuObject("DevStore");
        devLogo.setResource(R.drawable.devstore_logo);
        devLogo.setScaleType(ImageView.ScaleType.FIT_XY);

        MenuObject oschinaLogo = new MenuObject("OSChina");
        oschinaLogo.setResource(R.drawable.oschina_logo);
        oschinaLogo.setScaleType(ImageView.ScaleType.FIT_XY);


        menuObjects.add(close);
        menuObjects.add(csdnLogo);
        menuObjects.add(eoeLogo);
        menuObjects.add(devLogo);
        menuObjects.add(oschinaLogo);

        return menuObjects;
    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundResource(R.color.green_yellow);
        mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolBarTextView.setText("CSDN");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.context_menu:
                mMenuDialogFragment.show(mFragmentManager, "ContextMenuDialogFragment");
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClick(View view, int i) {
        switch (i) {
            case 1:
                //csdn
                if (currentpage != 1) {
                    currentpage = 1;

                    if (mCSDNFragment == null) {
                        mCSDNFragment = new CSDNFragment();
                    }
                    mToolBarTextView.setText("CSDN");
                    searchBlogsIv.setVisibility(View.VISIBLE);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mCSDNFragment);
                    transaction.commit();
                }
                break;
            case 2:
//                eoe
                if (currentpage != 2) {
                    currentpage = 2;
                    if (mEoeFragment == null)
                        mEoeFragment = new EoeFragment();
                    mToolBarTextView.setText("eoe");
                    searchBlogsIv.setVisibility(View.GONE);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mEoeFragment);
                    transaction.commit();
                }
                break;
            case 3:
                //devStore
                if (currentpage != 3) {
                    currentpage = 3;
                    Log.v("tag", "------devstore cliked");
                    if (mDevFragment == null) {
                        mDevFragment = new DevFragment();
                    }
                    mToolBarTextView.setText("DevStore");
                    searchBlogsIv.setVisibility(View.GONE);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mDevFragment);
                    transaction.commit();
                }
                break;
            case 4:
                if (currentpage != 4) {
                    currentpage = 4;
                    Log.v("tag", "------devstore cliked");
                    if (mOSChinaFragment == null) {
                        mOSChinaFragment = new OSChinaFragment();
                    }
                    searchBlogsIv.setVisibility(View.VISIBLE);
                    mToolBarTextView.setText(R.string.oschina);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, mOSChinaFragment);
                    transaction.commit();
                }
                break;
        }

    }


    @Override
    public void onMenuItemLongClick(View view, int i) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                quitCount++;
                if (quitCount == 1) {
                    Toast.makeText(getApplicationContext(), R.string.repeat_to_quit, Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (quitCount == 2)
                                finish();
                            else quitCount = 0;
                        }
                    }, 2000);

                    return false;
                }
                break;
            case KeyEvent.KEYCODE_HOME:
                mMenuDialogFragment.show(mFragmentManager, "ContextMenuDialogFragment");
                break;

        }

        return super.onKeyDown(keyCode, event);
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.zjh.ui/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.zjh.ui/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}
