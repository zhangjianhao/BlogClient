package com.zjh.bean;

/**
 * Created by 张建浩 on 2015/10/18.
 */
public class NewsItem {
    private String title;
    private boolean hasSummary;
    private String summary;
    private boolean hasImagLink;
    private String info;
    private String imagLink;
    private String newLink;

    public String getNewLink() {
        return newLink;
    }

    public void setNewLink(String newLink) {
        this.newLink = newLink;
    }

    public boolean isHasSummary() {
        return hasSummary;
    }

    public void setHasSummary(boolean hasSummary) {
        this.hasSummary = hasSummary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isHasImagLink() {
        return hasImagLink;
    }

    public void setHasImagLink(boolean hasImagLink) {
        this.hasImagLink = hasImagLink;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImagLink() {
        return imagLink;
    }

    public void setImagLink(String imagLink) {
        this.imagLink = imagLink;
    }
}
