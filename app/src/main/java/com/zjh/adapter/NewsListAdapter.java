package com.zjh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blogclient.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zjh.bean.NewsItem;
import com.zjh.utils.ImageLoaderOptions;

import java.util.List;

/**
 * Created by 张建浩 on 2015/10/18.
 */
public class NewsListAdapter extends BaseAdapter {
    private Context context;
    private List<NewsItem> items;
    private LayoutInflater mLayoutInflater;
    private ImageLoader mImageLoader;

    public NewsListAdapter(Context context,List<NewsItem> items) {
        this.context = context;
        this.items = items;
        mLayoutInflater = LayoutInflater.from(context);
        mImageLoader = ImageLoader.getInstance();
        //防止重复初始化
        if (!mImageLoader.isInited())
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }
    public void setData(List<NewsItem> items){
        this.items.clear();
        this.items.addAll(items);
    }
    public void addAll(List<NewsItem> items){
        this.items.addAll(items);
    }

    public void addData(NewsItem item){
        this.items.add(item);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView =mLayoutInflater.inflate(R.layout.news_item,parent,false);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.news_item_title);
            holder.mSummary = (TextView)convertView.findViewById(R.id.new_item_summary);
            holder.mImg = (ImageView)convertView.findViewById(R.id.news_item_img);
            holder.mDate = (TextView)convertView.findViewById(R.id.new_item_date);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        NewsItem item = items.get(position);
        holder.mTitle.setText(item.getTitle());
        if (item.isHasSummary()) {
            holder.mSummary.setVisibility(View.VISIBLE);
            holder.mSummary.setText(item.getSummary());
        }else holder.mSummary.setVisibility(View.GONE);
        if (item.isHasImagLink()){
            holder.mImg.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(item.getImagLink(),holder.mImg, ImageLoaderOptions.getOptions());

        }else{
            holder.mImg.setVisibility(View.GONE);
        }
        holder.mDate.setText(item.getInfo());

        return convertView;
    }

    private final class ViewHolder
    {
        TextView mTitle;
        TextView mSummary;
        ImageView mImg;
        TextView mDate;
    }
}
