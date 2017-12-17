package com.example.chenlei.my_weather.myViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chenlei.my_weather.R;

import java.io.File;

/**
 * Created by chenlei on 2017/9/23.
 */

public class BlurView extends RelativeLayout {

    private ImageView blured = null;
    private ImageView origin = null;
    private TextView bgChangeColor = null;

    public BlurView(Context context) {
        super(context);
        initView(context);
    }

    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray mTypedArray = context.obtainStyledAttributes
                (attrs, R.styleable.BlurView);
        mTypedArray.recycle();

    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.part_blur_view, this, true);
        blured = (ImageView) findViewById(R.id.blured);
        origin = (ImageView) findViewById(R.id.origin);
        bgChangeColor = (TextView) findViewById(R.id.bg_change_color);
    }


    public void doBlur(final int blur) {
        if (blur < 0 || blur > 100)
            return;
        //里面的图层先直接模糊到最大程度。然后改变外面图层的透明度
        //blur/125,保证在背景图片是浅色的情况下，依旧能分辨背景的基本图形
        final float alpha = (float) (1.0f - blur / 115.0);
        origin.setAlpha(alpha);
    }

    public void setImageResource(final int src, Activity activity) {
        String blurPicPath = null;
        String storePicPath = null;
        if (activity.isFinishing())
            return;
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences("set", Context.MODE_PRIVATE);
        boolean userSetBg = sharedPreferences.getBoolean("userSetBg", false);
        if (userSetBg) {
            bgChangeColor.setVisibility(GONE);
            blurPicPath = sharedPreferences.getString("blurPicPath", null);
            storePicPath = sharedPreferences.getString("storePicPath", null);
            Log.i("背景", "setImageResource: " + storePicPath);
            Log.i("背景", "setImageResource: " + blurPicPath);

            Bitmap bitmap= BitmapFactory.decodeFile(storePicPath);
            origin.setImageBitmap(bitmap);
            bitmap=BitmapFactory.decodeFile(blurPicPath);
            blured.setImageBitmap(bitmap);

//            Glide.with(activity).load(storePicPath)
//                    .crossFade()
//                    .error(R.mipmap.bg_sunny)
//                    .into(origin);
//
//            Glide.with(activity).load(blurPicPath)
//                    .crossFade()
//                    .error(R.mipmap.bg_sunny)
//                    .into(blured);
        } else {
            {
                if (src != 0) {
                    bgChangeColor.setVisibility(VISIBLE);

                    blurPicPath = getContext().getFilesDir() + "/blur/" + src + ".jpg";
                    File file = new File(blurPicPath);
                    if (file.exists()) {
                        Glide.with(activity)
                                .load(blurPicPath)
                                .placeholder(R.mipmap.bg_sunny)
                                .crossFade()
                                .into(blured);
                    } else {
                        Glide.with(activity).load(src)
                                .placeholder(R.mipmap.bg_sunny)
                                .crossFade()
                                .into(blured);
                    }

                    Glide.with(activity).load(src)
                            .placeholder(R.mipmap.bg_sunny)
                            .crossFade()
                            .into(origin);
                    Log.i("背景", "setImageResource: " + src);
                    Log.i("背景", "setImageResource: " + blurPicPath);
                }
            }
        }
    }
}
