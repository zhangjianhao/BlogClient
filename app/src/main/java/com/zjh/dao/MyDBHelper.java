package com.zjh.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 张建浩 on 2015/10/20.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context) {
        super(context, "news_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table news_item_cache( _id integer primary key autoincrement,title text,summary text,news_link text," +
                "img_link text,info text,fragmenttype int,newstype int)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
