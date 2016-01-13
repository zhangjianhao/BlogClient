package com.zjh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zjh.bean.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张建浩 on 2015/10/20.
 */
public class NewsItemDaoImpl implements NewsItemDao {
    private final static String TAG = "NewsItemDapImpl";
    private Context context;
    private static NewsItemDaoImpl dao;
    private MyDBHelper databaseHelper;

    private NewsItemDaoImpl(Context context) {
        this.context = context;
        databaseHelper = new MyDBHelper(context);
    }
    public static NewsItemDaoImpl getInstance(Context context){
        if (dao == null)
            dao = new NewsItemDaoImpl(context);
        return dao;
    }

    @Override
    public boolean addNewsItems(List<NewsItem> items, int fragmenttype,int newstype) {
        SQLiteDatabase database = null;
        try{
            database = databaseHelper.getWritableDatabase();
            for (NewsItem item:items){
                ContentValues values = new ContentValues();
                values.put("title",item.getTitle());
                values.put("news_link",item.getNewLink());
                values.put("summary",item.getSummary());
                values.put("img_link",item.getImagLink());
                values.put("info",item.getInfo());
                values.put("fragmenttype",fragmenttype);
                values.put("newstype",newstype);
                database.insert("news_item_cache",null,values);
            }
        }catch (Exception e){
            return false;
        }finally {
            if (database != null){
                database.close();
                database = null;
            }

        }

        return true;
    }

    @Override
    public List<NewsItem> getNewsList(int fragmenttype,int newstype, int currentPage) {
        SQLiteDatabase database = null;
        List<NewsItem> list = new ArrayList<>();
        try{
            database = databaseHelper.getReadableDatabase();
            String sql  = "select * from news_item_cache where fragmenttype=? and newstype = ? limit ?,?";
            int offset = 10 * (currentPage - 1);
            Cursor cursor = database.rawQuery(sql, new String[]{fragmenttype+"",newstype + "", offset + "", offset + 10 + ""});
            NewsItem item = null;
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String newsLink = cursor.getString(cursor.getColumnIndex("news_link"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String imgLink = cursor.getString(cursor.getColumnIndex("img_link"));
                String info = cursor.getString(cursor.getColumnIndex("info"));
                item = new NewsItem();
                item.setTitle(title);
                item.setNewLink(newsLink);
                if (summary!= null){
                    item.setHasSummary(true);
                    item.setSummary(summary);
                }else item.setHasSummary(false);
                if (imgLink != null){
                    item.setHasImagLink(true);
                    item.setImagLink(imgLink);
                }
                else item.setHasImagLink(false);
                item.setInfo(info);
                list.add(item);
            }
        }catch (Exception e){
            e.printStackTrace();
            return list;
        }finally {
            if (database != null){
                database.close();
                database  = null;
            }

        }
        return list;
    }

    @Override
    public boolean deleteAll(int fragmenttype,int newstype) {
        SQLiteDatabase database = null;
        try{
            database = databaseHelper.getWritableDatabase();
            int row  = database.delete("news_item_cache","fragmenttype= ? and newstype = ?",new String[]{fragmenttype+"",newstype+""});
            Log.v(TAG, "-----" + row + "row has deleted newstype------"+newstype);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if (database != null){
                database.close();
                database  = null;
            }
        }
        return true;
    }
}
