package com.zjh.utils;

import android.graphics.Bitmap;

import com.example.blogclient.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by 张建浩 on 2015/10/19.
 */
public class ImageLoaderOptions {

    public static DisplayImageOptions getOptions(){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory()
                .cacheOnDisc()//设置下载的图片是否缓存在SD卡中

                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .build();//构建完成
        return options;
    }
}
