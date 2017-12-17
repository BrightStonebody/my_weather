package com.example.chenlei.my_weather.myViews;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

public class GlideConfigModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
       builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);//和Picasso配置一样
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}