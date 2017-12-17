package com.example.chenlei.my_weather.myViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenlei on 2017/9/20.
 */

public class AirCircle extends View {
    private String quality=null;
    private Paint mPaint=null;
    private float scale;

    public void setData(String data) {
        this.quality = data;
        invalidate();
    }

    private int width;
    private int height;
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//         width = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
//        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式
//
//        height = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
//        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
//    }

    public AirCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scale=getResources().getDisplayMetrics().density;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(toPx(15));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(quality==null)
            return ;
        if(quality.equals("优"))
            mPaint.setColor(Color.rgb(0x9A,0xCD,0x32));
        else if(quality.equals("良"))
            mPaint.setColor(Color.rgb(0x00,0x9A,0xCD));
        else if(quality.equals("轻度污染"))
            mPaint.setColor(Color.rgb(0xFF,0xC1,0x25));
        else if(quality.equals("中度污染"))
            mPaint.setColor(Color.rgb(0xEE,0x3B,0x3B));
        else if(quality.equals("重度污染"))
                mPaint.setColor(Color.rgb(0x48,0x3d,0x8b));
        else if(quality.equals("严重污染"))
            mPaint.setColor(Color.rgb(11,23,70));
        else
            mPaint.setColor(Color.rgb(0x00,0x9A,0xCD));

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(toPx(8));

        canvas.translate(getWidth()/2,getHeight()/2);
        int r=Math.min(getWidth()/2,getHeight()/2);
        canvas.drawCircle(0,0,r-toPx(5),mPaint);

    }

    private int toPx(int dp) {
        return (int) (dp * scale + 0.5f);
    }


}
