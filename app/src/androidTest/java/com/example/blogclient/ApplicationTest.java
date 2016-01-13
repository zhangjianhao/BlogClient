package com.example.blogclient;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.zjh.bean.NewsItem;
import com.zjh.biz.NewsitemBiz;
import com.zjh.config.Constants;

import java.io.IOException;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testData(){
        NewsitemBiz biz = new NewsitemBiz();
        try {
            List<NewsItem> list = biz.getCSDNAnddroidList(Constants.NEW_INDUSTY,1);
            for (NewsItem item:list){
                Log.v("Test",item.getTitle());
                System.out.println(item.getTitle());
                System.out.println(item.getSummary());
                System.out.println(item.getInfo());
                if (item.isHasImagLink())
                System.out.println(item.getImagLink());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}