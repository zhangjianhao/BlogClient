package com.zjh.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


public class Welcome extends Activity {
    private AlphaAnimation animation;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        view = View.inflate(this, R.layout.welcome_main, null);
//        setContentView(view);
//        initData();
    }

    private void initData() {
        animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                redirectTo();
                finish();
            }
        });
    }


    public void redirectTo() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

   
}
