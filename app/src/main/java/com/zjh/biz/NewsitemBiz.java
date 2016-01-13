package com.zjh.biz;

import android.util.Log;

import com.zjh.bean.NewsItem;
import com.zjh.utils.UrlUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张建浩 on 2015/10/18.
 */
public class NewsitemBiz {
    private final static String TAG = NewsitemBiz.class.getName();

    public List<NewsItem> getNewsList(int newstype,int currentPage) throws IOException {
        List<NewsItem> list = new ArrayList();
        String url = UrlUtils.getUrl(newstype,currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);

            Document document = connection.get();
            Elements elements = document.select("div.unit");
            int size = elements.size();
            for (int i=0; i<size; i++){
                NewsItem item = new NewsItem();
                Element element = elements.get(i);
                Element title = element.select("h1").get(0).select("a").get(0);
                item.setTitle(title.text());
                Log.v(TAG,"title:"+item.getTitle());
                Element h4 = element.select("h4").get(0);
                String ago = h4.select("span.ago").get(0).text();
                String viewTime = h4.select("span.view_time").get(0).text();
                String comment = h4.select("span.num_recom").get(0).text();
                item.setInfo(ago+" "+viewTime+" "+comment);
                item.setNewLink(title.attr("href"));
                Log.v(TAG,"newslink"+item.getNewLink());
                Element dl = element.select("dl").get(0);
                String summary = dl.select("dd").get(0).text();
                if (summary==null)
                    item.setHasSummary(false);
                else {
                    item.setHasSummary(true);
                    item.setSummary(summary);
                }

                Elements imghrefs = dl.select("dt").get(0).select("a");
                if (imghrefs.size()==0){
                    item.setHasImagLink(false);
                }
                else{
                    String imglink = imghrefs.get(0).select("img").get(0).attr("src");
                    item.setHasImagLink(true);
                    item.setImagLink(imglink);
                }
                Log.v(TAG,"summary"+item.getSummary());
                Log.v(TAG,"info:"+item.getInfo());
                list.add(item);
            }

        return list;
    }

    public List<NewsItem> getCSDNAnddroidList(int newstype,int currentPage) throws IOException {
        List<NewsItem> list = new ArrayList();
        String url = UrlUtils.getUrl(newstype,currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");
            Document document  = connection.get();
            Elements newList = document.select("div.line_list");
            int size = newList.size();
        NewsItem item = null;
            for (int i=0; i<size; i++){
                item = new NewsItem();
                Element news = newList.get(i);
                Element newTitle = news.select("a").get(0);
                if (newTitle.getElementsByClass("line_list_pic_news").size()==1){
                    //说明带有图片的新闻
                    Element picturenews = news.select("a.tit_list").get(0);
                    String title = picturenews.text();
                    Log.v(TAG,"title:"+title);
                    String newsLink = picturenews.attr("href");
                    Log.v(TAG,"newslink"+newsLink);
                    item.setTitle(title);
                    item.setNewLink(newsLink);
                    String summary = news.select("span.tag_summary").get(0).text();
                    Log.v(TAG,"summary:"+summary);
                    item.setHasSummary(true);
                    item.setSummary(summary);
                    String imagLink = news.select("a.line_list_pic_news").select("img").get(0).attr("src");
                    item.setHasImagLink(true);
                    item.setImagLink(imagLink);

                }else {
                    String title = newTitle.text();
                    String newsLink = newTitle.attr("href");
//                    String realLink = null;
//                    Elements firstNode = Jsoup.connect(newsLink).get().select("div.content_info");
//                    if (firstNode.size()>0){
//                        realLink = Jsoup.connect(newsLink).get().select("div.content_info").get(0).select("a[href]").get(0).attr("href");
//
//                    }

                    Elements info = news.select("div.dwon_words").get(0).select("span.tag_source");
//                    if (info == null){
//                        Log.v(TAG,"info is null");
//                    }
//                    if (info.get(2).text() == null){
//                        Log.v(TAG,"info2 is null");
//                    }
                    int infoSize = info.size();
                    String infos = null ;
                    for (int j = 0; j<infoSize; j++){
                        if (info.get(j).text() != null)
                        infos += info.get(j).text() + " ";
                    }

                    item.setInfo(infos);
                    item.setTitle(title);
                    item.setNewLink(newsLink);
                    item.setHasSummary(false);
                    item.setHasImagLink(false);
                }
                list.add(item);
            }
        return list;
    }

    public List<NewsItem> getDevStoreList(int newstype,int currentPage) throws IOException{
        List<NewsItem>list = new ArrayList<>();
        String url = UrlUtils.getDevUrl(newstype, currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");
        Document document  = connection.get();
        Elements news = document.select("div.share");
        Elements infos = document.select("dd.content");
        int size = news.size();

        NewsItem item = null;
        for (int i=3; i<size; i++){
            item = new NewsItem();
            Element content = infos.get(i-3);
            String time = content.select("span.time").get(0).text();
            String comment = content.select("a.comments_button01").text();
            Element newsitem = news.get(i);
            String title = newsitem.attr("bdText");
            String summary = newsitem.attr("bdDesc");
            String newsLink = newsitem.attr("bdUrl");
            String imgLink = newsitem.attr("bdPic");
            item.setTitle(title);
            item.setNewLink("http://www.devstore.cn"+newsLink);
            item.setHasSummary(true);
            item.setSummary(summary);
            item.setHasImagLink(true);
            item.setImagLink(imgLink);
            item.setInfo(time + " " + comment);
            list.add(item);
        }
        return list;


    }

    public List<NewsItem> getDevStoreArticle(int newstype,int currentPage) throws IOException{
        List<NewsItem>list = new ArrayList<>();
        String url = UrlUtils.getDevUrl(newstype, currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");
        Document document  = connection.get();
        Elements contents  = document.select("div.inner_article_list").select("dd.content");
        int size = contents.size();
        NewsItem item = null;
        for (int i=0; i<size; i++){
            item = new NewsItem();
            Element content = contents.get(i);
            Element a = content.select("h3").select("a").get(0);
            String title = a.text();
            String newsLink = a.attr("href");
            String summary  = content.select("div.div01").text();
            String time = content.select("span.time").get(0).text();
            String comment = content.select("a.comments_button01").get(0).text();
            String author = content.select("a.a01").get(0).text();
            String recommand = content.select("a.recommend_button01").get(0).text();
            item.setTitle(title);
            item.setNewLink("http://www.devstore.cn" + newsLink);
            item.setHasSummary(true);
            item.setSummary(summary);
            item.setHasImagLink(false);
            item.setInfo(author + " " + time + " " + comment + " " + recommand);
            list.add(item);
        }
        return list;
    }

    public List<NewsItem> getOsChinaBlogsList(int newstype,int currentPage) throws IOException{
        List<NewsItem>list = new ArrayList<>();
        String url = UrlUtils.getOSChinaUrl(newstype, currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");

        Document document  = connection.get();
        Element element = document.getElementById("RecentBlogs");
        Elements blogList = element.select("ul.BlogList").select("li");
        int size = blogList.size();
        NewsItem item = null;
        for (int i=0; i<size; i++){
            item = new NewsItem();
            Element blog = blogList.get(i);
            Element a = blog.select("h3").select("a").get(0);
            String title = a.text();
            String newsLink = a.attr("href");
            String summary = blog.select("p").get(0).text();
            String info = blog.select("div.date").text();

            item.setTitle(title);
            item.setNewLink(newsLink);
            item.setHasSummary(true);
            item.setSummary(summary);
            item.setHasImagLink(false);
            item.setInfo(info);
            list.add(item);

        }
        return list;
    }

    public List<NewsItem> getOsChinaAndroidBlogsList(int newstype,int currentPage) throws IOException{
        List<NewsItem>list = new ArrayList<>();
        String url = UrlUtils.getOSChinaUrl(newstype, currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");
        Document document  = connection.get();
        Elements elements = document.select("div.AndroidPosts").select("ul").get(1).select("li");
        int size = elements.size();
        NewsItem item = null;
        for (int i=0; i<size; i++){
            item = new NewsItem();
            Element blog = elements.get(i);
            Element a = blog.select("a").get(0);
            String title = a.text();
            String newsLink = a.attr("href");
            String info = blog.select("dd.remark").text();
            String summary = blog.select("dd.content").text();
            item.setTitle(title);
            item.setNewLink(newsLink);
            item.setHasSummary(true);
            item.setSummary(summary);
            item.setHasImagLink(false);
            item.setInfo(info);
            list.add(item);
        }

        return list;
    }

    public List<NewsItem> getEoeNewsList(int newstype,int currentPage) throws IOException{
        List<NewsItem>list = new ArrayList<>();
        String url = UrlUtils.getEoeUrl(newstype, currentPage);
        Connection connection = Jsoup.connect(url);
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");
        Document document  = connection.get();
        Elements elements = document.select("tbody[id^=normalthread");
        int size = elements.size();
        NewsItem item = null;
        for (int i=0; i<size; i++){
            item = new NewsItem();
            Element element = elements.get(i);
            Elements time = element.select("span[title]");
            String info = null;
            if (time.size()>0){
                info = time.get(0).html().replace("&nbsp;", "");
            }
            else
                info = element.select("em").get(2).select("a").text();
            Element news = element.select("th.new").get(0);
            Element a = news.select("a[class$=xst]").get(0);
            String title = a.text();
            String newsLink = "http://www.eoeandroid.com/"+a.attr("href");
            item.setTitle(title);
            item.setNewLink(newsLink);
            item.setHasSummary(false);
            item.setHasImagLink(false);
            item.setInfo(info);
            list.add(item);
        }
        return list;
    }

    /**
     * 搜索csdn内容
     * @param keywords 关键词
     * @param type 类型
     * @param currentPage 页数
     * @throws IOException
     */

    public List<NewsItem> getSearchCSDNList(String keywords,String type,int currentPage) throws IOException{
        List<NewsItem>list = new ArrayList<>();

        keywords = keywords.replace(" ", "+");
        keywords = URLEncoder.encode(keywords, "utf-8");
        System.out.println(keywords);
        Connection connection = Jsoup.connect("http://so.csdn.net/so/search/s.do?p="+currentPage+"&q="+keywords+"&domain=&t="+type+"&o=&s=");
        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");

        Document document = connection.get();
        Elements elements = document.select("div.search-list-con").select("dl.search-list");
        int size  = elements.size();
        NewsItem item = null;
        for (int i=0; i<size; i++){
            item = new NewsItem();
            Element element = elements.get(i);
            String title = element.select("a").get(0).text();
            String newsLink = element.select("a").get(0).attr("href");
            Element authorTime = element.select("dd.author-time").get(0);
            String author = authorTime.select("a").text();
            String html = authorTime.html();
            String info = html.replaceAll("<a.+</a>", author).replaceAll("&nbsp;&nbsp;","").replace("&nbsp;"," ").replace('\n', ' ');

            String summary = element.select("dd.search-detail").text();


            item.setTitle(title);
            item.setNewLink(newsLink);
            item.setHasSummary(true);
            item.setSummary(summary);
            item.setHasImagLink(false);
            item.setInfo(info);
            list.add(item);
        }
        return list;
    }
    public List<NewsItem> getSearchOschinaList(String keywords,String type,int currentPage) throws IOException{
        keywords = keywords.replace(" ", "+");
        keywords = URLEncoder.encode(keywords,"utf-8");
//		 Connection connection = Jsoup.connect("http://www.oschina.net/search?scope=blog&q=apk+%E5%8A%A0%E5%A3%B3&p=1");
        Connection connection = Jsoup.connect("http://www.oschina.net/search?scope="+type+"&q="+keywords+"&p="+currentPage);
//        connection.timeout(8000);
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1653.0 Safari/537.36");
        connection.header("Accept", "image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip,deflate,sdch");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        connection.header("Connection", "keep-alive");
        Document document = connection.get();
        Elements elements = document.select("li.obj_type_3");
        int size  = elements.size();
        Log.v(TAG,"size:"+size);
        System.out.println(size);
        List<NewsItem>list = new ArrayList<>();
        NewsItem item = null;
        for (int i=0; i<size; i++){
            item = new NewsItem();
            Element element = elements.get(i);
            Element a = element.select("h3").select("a").get(0);
            String title = a.text();
            String newsLink = a.attr("href");
            String summary = element.select("p.outline").get(0).text();
            String info = element.select("p.date").get(0).text();
            item.setTitle(title);
            item.setNewLink(newsLink);
            item.setHasSummary(true);
            item.setSummary(summary);
            item.setHasImagLink(false);
            item.setInfo(info);
            list.add(item);
        }
        return list;
    }




}
