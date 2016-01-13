package com.zjh.utils;

import com.zjh.config.Constants;

/**
 * Created by 张建浩 on 2015/10/18.
 */
public class UrlUtils {

    public static final String NEWS_INDUSTY_URL = "http://news.csdn.net/news";
    public static final String NEWS_MOBILE_URL = "http://mobile.csdn.net/mobile";
    public static final String NEWS_RESEARCH_URL = "http://sd.csdn.net/sd";
    public static final String NEWS_PROGRAMMER_MAGAZINE_URL = "http://programmer.csdn.net/programmer";
    public static final String NEWS_CLOUD_COMPUTING_URL = "http://cloud.csdn.net/cloud";
    public static final String NEWS_ANDROID_URL = "http://www.csdn.net/tag/android/news";
    public static final String DEV_INFOMATION_URL = "http://www.devstore.cn/new/newsIndex";
    public static final String DEV_ARTICLE_URL = "http://www.devstore.cn/essay/essayHome";
    public static final String OSCHINA_BOLGS_URL = "http://www.oschina.net/blog";
    public static final String OSCHINA_ANDROID_BOLGS_URL = "http://www.oschina.net/android/1/android?type=3&sort=time";
    public static final String EOE_URL = "http://www.eoeandroid.com/";


    public static String getUrl(int newstype, int currentPage) {
        String url = null;
        switch (newstype) {
            case Constants.NEW_INDUSTY:
                url = NEWS_INDUSTY_URL+"/"+currentPage;
                break;
            case Constants.NEWS_MOBILE:
                url = NEWS_MOBILE_URL+"/"+currentPage;
                break;
            case Constants.NEWS_RESEARCH:
                url = NEWS_RESEARCH_URL+"/"+currentPage;
                break;

            case Constants.NEWS_PROGRAMMER_MAGAZINE:
                url = NEWS_PROGRAMMER_MAGAZINE_URL+"/"+currentPage;
                break;
            case Constants.NEWS_CLOUD_COMPUTING:
                url = NEWS_CLOUD_COMPUTING_URL+"/"+currentPage;
                break;
            case Constants.NEWS_ANDROID:
                url = NEWS_ANDROID_URL+"-"+currentPage;
                break;

        }
        return url;
    }
    public static String getDevUrl(int newstype,int currentPage){
        String url = null;
        switch (newstype){
            //dev 资讯
            case Constants.DEV_INFOMATION:
                url = DEV_INFOMATION_URL+"/"+currentPage+".html#list";
                break;
            //dev 文章
            case Constants.DEV_ARTICLE:
                url = DEV_ARTICLE_URL+"/"+currentPage+".html";
                break;
        }
        return url;
    }

    public static String getOSChinaUrl(int newstype,int currentPage){
        String url = null;
        switch (newstype){
            case Constants.OSCHINA_BLOGS:
                url = OSCHINA_BOLGS_URL+"?type=0&p="+currentPage;
                break;
            case Constants.OSCHINA_ANDROID_BLOGS:
                url = OSCHINA_ANDROID_BOLGS_URL+"&p="+currentPage;
                break;
            case Constants.OSCHINA_FRONT_BLOGS:
                //前端
                url = OSCHINA_BOLGS_URL+"?type=428612&p="+currentPage;
                break;
            case Constants.OSCHINA_SERVER_BLOGS:
                url = OSCHINA_BOLGS_URL+"?type=428640&p="+currentPage;
                break;
            case Constants.OSCHINA_GAME_BLOGS:
                url = OSCHINA_BOLGS_URL+"?type=429511&p="+currentPage;
                break;

            case Constants.OSCHINA_DATABASE_BLOGS:
                url = OSCHINA_BOLGS_URL+"?type=428610&p="+currentPage;
                break;

            case Constants.OSCHINA_SOFTWARE_BLOGS:
                url = OSCHINA_BOLGS_URL+"?type=428638&p="+currentPage;
                break;

        }
        return url;
    }

    public static String getEoeUrl(int newstype,int currentPage){
        String url = null;
        switch (newstype){
            //eoe 资讯
            case Constants.EOE_INFOMATION:
                url = EOE_URL+"forum-865-"+currentPage+".html";
                break;
            case Constants.EOE_FRESHMAN:
                url = EOE_URL+"forum-53-"+currentPage+".html";
                break;
            case Constants.EOE_SHARE_EXPERIENCE:
                url = EOE_URL+"forum-93-"+currentPage+".html";
                break;
            case Constants.EOE_EXAMPLE_COURSE:
                url = EOE_URL+"forum-27-"+currentPage+".html";
                break;
            case Constants.EOE_TECHNOLOGY_ASK:
                url = EOE_URL+"forum-45-"+currentPage+".html";
                break;
            case Constants.EOE_CODE_DOWNLOAD:
                url = EOE_URL+"forum-23-"+currentPage+".html";
                break;
            case Constants.EOE_FRAMEWORK_DEVELOP:
                url = EOE_URL+"forum-48-"+currentPage+".html";
                break;

        }
        return url;
    }






}
