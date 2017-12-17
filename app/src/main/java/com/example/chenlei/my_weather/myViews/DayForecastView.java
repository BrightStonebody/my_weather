package com.example.chenlei.my_weather.myViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.tool.WeatherToIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/18.
 */

public class DayForecastView extends View {

    private Paint mPaint;
    private float scale;
    private List<Weather.Daily> datas = new ArrayList<>();
    private int maxY = 0;
    private int minY = 100;

    public DayForecastView(Context context, @Nullable AttributeSet attrs) {
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

        //350dp
        float oneY = toPx(80) / (maxY - minY);
        int oneX = toPx(60);
        int marginStart = toPx(30);
        int marginTop = toPx(145);
        int bottom = toPx(340);
        int weekY=toPx(20);

        //设置水平居中
        mPaint.setTextAlign(Paint.Align.CENTER);

        int[] lastDays=new int[2];
        int[] lastNights=new int[2];

        for(int i=0;i<datas.size();i++){
            Weather.Daily data=datas.get(i);
            int x=marginStart+oneX*i;
            int dayY= (int) (marginTop
                    +oneY*(maxY-Integer.valueOf(data.day.temphigh)));
            mPaint.setColor(Color.rgb(0xff,0xe4,0xc4));
            canvas.drawCircle(x,dayY,10,mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(data.day.temphigh+"°",x,dayY-toPx(10),mPaint);
            int nightY= (int) (marginTop+toPx(15)
                    +oneY*(maxY-Integer.valueOf(data.night.templow)));
            mPaint.setColor(Color.rgb(0xee,0x82,0xee));
            canvas.drawCircle(x,nightY,10,mPaint);
            mPaint.setColor(Color.WHITE);

            canvas.drawText(data.night.templow+"°",x,nightY+toPx(20),mPaint);
           if(i!=0){
                mPaint.setColor(Color.rgb(0xff,0xe4,0xc4));
                canvas.drawLine(lastDays[0],lastDays[1],x,dayY,mPaint);
                mPaint.setColor(Color.rgb(0xee,0x82,0xee));
                canvas.drawLine(lastNights[0],lastNights[1],x,nightY,mPaint);
            }
            lastDays[0]=x;lastDays[1]=dayY;
            lastNights[0]=x;lastNights[1]=nightY;

            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(toPx(15));
            data.week="周"+data.week.substring(data.week.length()-1);
            canvas.drawText(data.week,x,weekY,mPaint);
            mPaint.setTextSize(toPx(15));
            canvas.drawText(data.date,x,weekY+toPx(20),mPaint);
            canvas.drawText(data.day.weather,x,weekY+toPx(40),mPaint);
            Bitmap weatherIconDay= BitmapFactory.decodeResource
                    (getContext().getResources(),WeatherToIcon.weatherToIcon(data.day.weather));
            canvas.drawBitmap(weatherIconDay,x-toPx(18),weekY+toPx(50),mPaint);

            Bitmap weatherIconNight= BitmapFactory.decodeResource
                    (getContext().getResources(),WeatherToIcon.weatherToIcon(data.night.weather));
            canvas.drawBitmap(weatherIconNight,x-toPx(18),bottom-toPx(60),mPaint);
            canvas.drawText(data.night.weather,x,bottom,mPaint);
        }

    }

    public void setDatas(List<Weather.Daily> datas) {
        this.datas = datas;
        Weather.Daily lastData=null;
        for (Weather.Daily data : datas) {
            if(data.date.equals("")) {
                datas.set(datas.indexOf(data),lastData);
                data=lastData;
            }
            int tempHigh = Integer.valueOf(data.day.temphigh);
            int tempLow=Integer.valueOf(data.night.templow);
            if (tempHigh > maxY)
                maxY = tempHigh;
            else if (tempLow < minY) {
                minY = tempLow;
            }
            lastData=data;
        }
        //给其余的东西预留一些空间
        maxY += 2;
        minY -= 2;
        invalidate();
    }

    private int toPx(int dp) {
        return (int) (dp * scale + 0.5f);
    }

}
