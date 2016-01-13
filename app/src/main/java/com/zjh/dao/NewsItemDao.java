package com.zjh.dao;

import com.zjh.bean.NewsItem;

import java.util.List;

/**
 * Created by 张建浩 on 2015/10/20.
 */
public interface NewsItemDao {

    /**
     * 增加新闻条目
     * @param items
     * @param newstype
     * @return
     */
    public boolean addNewsItems(List<NewsItem> items,int fragmenttype,int newstype);

    /**
     * 根据新闻类型和页数从数据库中获取新闻列表
     * @param newstype
     * @param currentPage
     * @return LIst 新闻列表
     */
    public List<NewsItem> getNewsList(int fragmenttype,int newstype,int currentPage);

    /**
     * 删除指定类型的全部新闻
     * @param newstype
     * @return
     */
    public boolean deleteAll(int fragmenttype,int newstype);

}
